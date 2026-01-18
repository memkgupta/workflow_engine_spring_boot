package com.mk.workflow_engine;


import com.mk.workflow_engine.annotations.Node;
import com.mk.workflow_engine.exceptions.WorkflowBuildException;
import com.mk.workflow_engine.exceptions.WorkflowConfigurationException;
import com.mk.workflow_engine.exceptions.WorkflowDependencyException;
import com.mk.workflow_engine.exceptions.WorkflowParseException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;


public class WorkflowFactory {
    private final HashMap<String,WorkflowNode> nodeMap;
    private final ResourcePatternResolver resolver;
    private final ObjectMapper objectMapper;
    private final TreeMap<String , WorkflowNode> workflows;
    private final Map<String, Integer> indegree = new HashMap<>();

    private final HashMap<String,List<String>> dependencyGraph;
    public WorkflowFactory(ResourcePatternResolver resolver, ObjectMapper objectMapper, ApplicationContext context)
    {
        this.resolver = resolver;
        this.objectMapper = objectMapper;
        this.nodeMap = new HashMap<>();
        this.workflows = new TreeMap<>();
        this.dependencyGraph = new HashMap<>();
       Map<String,WorkflowNode> beans =  context.getBeansOfType(WorkflowNode.class);
       for(WorkflowNode node :beans.values())
       {
           Node annotation = node.getClass().getAnnotation(Node.class);
           if(annotation == null)
           {
               throw new IllegalStateException(
                       "WorkflowNode bean " + node.getClass().getName() + " is missing @Node annotation"
               );
           }
           String workflowName = annotation.workflowName();
           String name = annotation.name();
           nodeMap.put(name,node);
           if(annotation.isRoot())
           {
               workflows.put(workflowName,node);
           }
       }
    }
    public WorkflowNode getWorkFlow(String name)
    {
        return workflows.get(name);
    }
    public void init() throws IOException {
        // scan all workflowNodes with root annotation
        List<JsonNode> json_workflows = new ArrayList<>();

        Resource[] resources =
                resolver.getResources("classpath:workflows/*.json");

        for (Resource resource : resources) {
            try (InputStream is = resource.getInputStream()) {
                JsonNode tree = objectMapper.readTree(is);

                if(!tree.has("name"))
                {
                    throw new WorkflowParseException("Workflow file missing name: " + resource.getFilename());
                }
                    json_workflows.add(tree);


            }
            catch (Exception e) {
                throw new WorkflowParseException("Failed to parse workflow file: " + resource.getFilename(), e);

            }
        }
        // build the dependency graph
        buildDependencyGraph(json_workflows);
        //  toposort the workflows
        json_workflows = topologicalSort(json_workflows);
        for(JsonNode node : json_workflows)
        {
            String name = node.get("name").stringValue();
            if(!workflows.containsKey(name))
            {
                throw new WorkflowConfigurationException("WorkflowNode not defined: " + name);

            }
            WorkflowNode root = build(node);
            workflows.put(name,root);
        }

    }
    public void buildDependencyGraph(List<JsonNode> json_workflows) {

        // Collect workflow names for fast lookup
        Set<String> workflowNames = json_workflows.stream()
                .map(w -> w.get("name").asString())
                .collect(Collectors.toSet());

        for (JsonNode json_workflow : json_workflows) {

            String name = json_workflow.get("name").asString();
            indegree.putIfAbsent(name, 0);

            JsonNode dependency = json_workflow.get("depends_on");

            if (dependency != null && dependency.isArray()) {

                dependency.forEach(dep -> {

                    if (!dep.isValueNode()) {
                        throw new IllegalStateException(
                                "depends_on must contain only string values in workflow '" + name + "'");
                    }

                    String depName = dep.asString();

                    // Validate dependency exists
                    if (!workflowNames.contains(depName)) {
                        throw new WorkflowDependencyException(
                                "Workflow '" + name + "' depends on missing workflow '" + depName + "'");
                    }

                    // Build reverse graph: dependency → dependent workflow
                    dependencyGraph.putIfAbsent(depName, new ArrayList<>());
                    dependencyGraph.get(depName).add(name);

                    // Increase indegree of current workflow
                    indegree.put(name, indegree.get(name) + 1);
                });
            }
        }
    }

    public  List<JsonNode> topologicalSort(List<JsonNode> original)
    {
        HashMap<String,Integer> indegs = new HashMap<>();
        Queue<String> q = new LinkedList<>();

        for(var e : dependencyGraph.entrySet())
        {
            indegs.put(e.getKey(),dependencyGraph.get(e.getKey()).size());
            if(indegs.get(e.getKey()) == 0)
            {
                q.offer(e.getKey());
            }
        }
        Map<String,JsonNode> jsonWorkflowMap = original.stream().collect(Collectors.toMap(node->node.get("name").asString(),
                node->node));
        List<JsonNode> res = new ArrayList<>();
        while(!q.isEmpty())
        {
            String nodeKey = q.poll();
            List<String> deps = dependencyGraph.getOrDefault(nodeKey, Collections.emptyList());
            res.add(jsonWorkflowMap.get(nodeKey));
            for(var e : deps)
            {
                indegs.put(e, indegs.get(e)-1);
                if(indegs.get(e) == 0)
                {
                    q.offer(e);
                }
            }
        }
        if (res.size() != original.size()) {
            throw new WorkflowDependencyException("Cycle detected in workflow dependency graph");

        }
        return res;
    }
    public WorkflowNode build(JsonNode node)
    {
        JsonNode nodes = node.get("nodes");
        if(nodes == null)
        {
            throw new WorkflowBuildException("Invalid workflow definition: missing nodes");
        }
        WorkflowNode root = new WorkflowNode() {
            @Override
            protected WorkflowNode getNext(WorkflowContext ctx) {
                return super.getNext(ctx);
            }
        };
        JsonNode start = nodes.get("start");
        if (start == null || !start.has("next")) {
            throw new WorkflowBuildException("Workflow start node missing next");
        }
    root.setNext(rec(start.get("next").asString(),nodes));
    return root;
    }
    public WorkflowNode rec(String nodeName,JsonNode tree)
    {   JsonNode node = tree.path(nodeName);
        if (node.isMissingNode()) {
            throw new WorkflowParseException("Node '" + nodeName + "' not found in workflow JSON");

        }
        if (!node.has("type")) {
            throw new WorkflowParseException("Node '" + nodeName + "' missing type");

        }
        String type = node.get("type").stringValue();
        if(type.equals("END")) return new WorkflowNode() {
            @Override
            public void setNext(WorkflowNode next) {

            }

            @Override
            protected WorkflowNode getNext(WorkflowContext ctx) {
               return null;
            }
        };

        WorkflowNode workflowNode = nodeMap.get(nodeName);
        if (workflowNode == null ) {
            throw new WorkflowConfigurationException("No WorkflowNode bean mapped for node: " + nodeName);

        }
        if(type.equals("condition"))
        {
            if (!node.has("YES") || !node.has("NO")) {
                throw new WorkflowBuildException("Condition node '" + nodeName + "' missing YES/NO branch");

            }
            ConditionNode cndNode = (ConditionNode) workflowNode;
            cndNode.setPositive(rec(node.get("YES").asString(),tree));
            cndNode.setNegative(rec(node.get("NO").asString(),tree));
        }
        else if(type.equals("case"))
        {
            if (!node.has("cases") || !node.get("cases").isObject()) {
                throw new WorkflowBuildException("Case node '" + nodeName + "' missing cases object");

            }
            CaseNode<Object> caseNode = (CaseNode<Object>) workflowNode;
            JsonNode cases = node.get("cases");
            Map<String, String> casesMap = new HashMap<>();
            cases.forEachEntry((key,value)->{
                casesMap.put(key,value.asString());
            });
            Map<Object, WorkflowNode> updated = new HashMap<>();

            for (Map.Entry<Object, WorkflowNode> entry : caseNode.getCases().entrySet()) {
                Object key = entry.getKey();
                WorkflowNode nxt;

                String target = casesMap.get(key);

                if (tree.has(target)) {
                    nxt = rec(target, tree);
                } else {
                    if (!workflows.containsKey(target)) {
                        throw new WorkflowDependencyException("Case node refers unknown workflow/node: " + target);

                    }
                    nxt = workflows.get(target);
                }

                updated.put(key, nxt);
            }

            caseNode.getCases().clear();
            caseNode.getCases().putAll(updated);

        }
       else {
            if (!node.has("next")) {
                throw new WorkflowBuildException("Node '" + nodeName + "' missing next");

            }
            workflowNode.setNext(rec(node.get("next").asString(),tree));
        }


        return workflowNode;
    }
}
