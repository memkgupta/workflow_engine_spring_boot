package com.mk.workflow_engine;


import com.mk.workflow_engine.annotations.Node;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
//todo add error handling
public class WorkflowFactory {
    private final HashMap<String,WorkflowNode> nodeMap;
    private final ResourcePatternResolver resolver;
    private final ObjectMapper objectMapper;
    private final TreeMap<String , WorkflowNode> workflows;
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
                    json_workflows.add(tree);


            }
        }
        // build the dependency graph
        buildDependencyGraph(json_workflows);
        //  toposort the workflows
        json_workflows = toposort(json_workflows);
        for(JsonNode node : json_workflows)
        {
            String name = node.get("name").stringValue();
            if(!workflows.containsKey(name))
            {
                throw new RuntimeException("WorkflowNode not defined");
            }
            WorkflowNode root = build(node);
            workflows.put(name,root);
        }

    }
    public void buildDependencyGraph(List<JsonNode> json_workflows)
    {
        for(JsonNode json_workflow : json_workflows)
        {
      JsonNode dependency =   json_workflow.get("depends_on");
      String name = json_workflow.get("name").asString();
      if(dependency!=null && dependency.isArray())
      {
        dependency.forEach((dep)->{
            if(dep.isValueNode())
            {
                if(dependencyGraph.containsKey(dep.asString()) && dependencyGraph.get(dep.asString()).contains(name))
                {
                    throw new RuntimeException("Circular dependency in workflows");
                }
                dependencyGraph.putIfAbsent(name,new ArrayList<>());
                dependencyGraph.get(name).add(dep.asString());
            }
        });
      }
        }
    }
    public  List<JsonNode> toposort(List<JsonNode> original)
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
            res.add(jsonWorkflowMap.get(nodeKey));
            for(var e : dependencyGraph.get(nodeKey))
            {
                indegs.put(e, indegs.get(e)-1);
                if(indegs.get(e) == 0)
                {
                    q.offer(e);
                }
            }
        }
        return res;
    }
    public WorkflowNode build(JsonNode node)
    {
        JsonNode nodes = node.get("nodes");
        if(nodes == null)
        {
            throw new RuntimeException("Invalid Workflow definition");
        }
        WorkflowNode root = new WorkflowNode() {
            @Override
            protected WorkflowNode getNext(WorkflowContext ctx) {
                return super.getNext(ctx);
            }
        };
        JsonNode start = nodes.get("start");
    root.setNext(rec(start.get("next").asString(),nodes));
    return root;
    }
    public WorkflowNode rec(String nodeName,JsonNode tree)
    {   JsonNode node = tree.path(nodeName);
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
        String name = node.asString();
        WorkflowNode workflowNode = nodeMap.get(name);
        if(type.equals("condition"))
        {
            ConditionNode cndNode = (ConditionNode) workflowNode;
            cndNode.setPositive(rec(node.get("YES").asString(),tree));
            cndNode.setNegative(rec(node.get("NO").asString(),tree));
        }
        else if(type.equals("case"))
        {
            CaseNode<Object> caseNode = (CaseNode<Object>) workflowNode;
            JsonNode cases = node.get("cases");
            Map<String, String> casesMap = new HashMap<>();
            cases.forEachEntry((key,value)->{
                casesMap.put(key,value.asString());
            });
            for(Map.Entry<Object,WorkflowNode> _case : caseNode.getCases().entrySet())
            {
                Object key = _case.getKey();
                WorkflowNode nxt = null;
                if(tree.has(casesMap.get((String)_case.getKey())))
                {
                    nxt = rec(casesMap.get((String)_case.getKey()),tree);
                }
                else {
                    nxt =  workflows.get(casesMap.get((String)_case.getKey()));
                }

                caseNode.getCases().put(key,nxt);
            }
        }
       else {
            workflowNode.setNext(rec(node.get("next").asString(),tree));
        }


        return workflowNode;
    }
}
