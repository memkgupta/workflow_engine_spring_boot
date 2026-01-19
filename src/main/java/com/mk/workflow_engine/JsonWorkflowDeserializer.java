package com.mk.workflow_engine;

import com.mk.workflow_engine.exceptions.WorkflowParseException;
import com.mk.workflow_engine.workflows.NodeDefinition;
import com.mk.workflow_engine.workflows.NodeParserFactory;
import com.mk.workflow_engine.workflows.WorkflowDefinition;
import com.mk.workflow_engine.workflows.enums.NodeTypeEnum;
import com.mk.workflow_engine.workflows.strategies.WorkflowDeserializer;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JsonWorkflowDeserializer
        implements WorkflowDeserializer<JsonNode, WorkflowDefinition> {
private final NodeParserFactory<JsonNode> nodeParserFactory;

    public JsonWorkflowDeserializer(NodeParserFactory<JsonNode> nodeParserFactory) {
        this.nodeParserFactory = nodeParserFactory;
    }

    @Override
    public WorkflowDefinition deserialize(JsonNode root) {

        if (!root.has("namespace") || !root.has("name") || !root.has("version")) {
            throw new WorkflowParseException("Workflow must contain namespace, name and version");
        }

        WorkflowDefinition def = new WorkflowDefinition();
        def.setNamespace(root.get("namespace").asText());
        def.setName(root.get("name").asText());
        def.setVersion(root.get("version").asInt());

        // depends_on
        if (root.has("depends_on")) {
            if (!root.get("depends_on").isArray()) {
                throw new WorkflowParseException("depends_on must be an array");
            }
            List<String> deps = new ArrayList<>();
            root.get("depends_on").forEach(n -> deps.add(n.asText()));
            def.setDependsOn(deps);
        }

        // nodes
        JsonNode nodes = root.get("nodes");
        if (nodes == null || !nodes.isObject()) {
            throw new WorkflowParseException("Workflow must contain nodes object");
        }

        Map<String, NodeDefinition> parsedNodes = new HashMap<>();

        nodes.forEachEntry((key,value) -> {
            parsedNodes.put(key, parseNode(key, value));
        });

        def.setNodes(parsedNodes.values().stream().toList());
        return def;
    }

    private NodeDefinition parseNode(String nodeName, JsonNode json) {

        if (!json.has("type")) {
            throw new WorkflowParseException("Node '" + nodeName + "' missing type");
        }

        String type = json.get("type").asString();
        NodeTypeEnum nodeType = NodeTypeEnum.valueOf(type);
        return nodeParserFactory.getParser(nodeType).parse(json);
//        switch (type) {
//            case "task" -> {
//                if (!json.has("next"))
//                    throw new WorkflowParseException("Task node '" + nodeName + "' missing next");
//
//                TaskNodeDefinition t = new TaskNodeDefinition();
//                t.setName(nodeName);
//                t.setType(type);
//                t.setNext(json.get("next").asText());
//                return t;
//            }
//
//            case "condition" -> {
//                if (!json.has("YES") || !json.has("NO"))
//                    throw new WorkflowParseException("Condition node '" + nodeName + "' missing YES/NO");
//
//                ConditionNodeDefinition c = new ConditionNodeDefinition();
//                c.setName(nodeName);
//                c.setType(type);
//                c.setYes(json.get("YES").asText());
//                c.setNo(json.get("NO").asText());
//                return c;
//            }
//
//            case "case" -> {
//                if (!json.has("cases") || !json.get("cases").isObject())
//                    throw new WorkflowParseException("Case node '" + nodeName + "' missing cases object");
//
//                CaseNodeDefinition cs = new CaseNodeDefinition();
//                cs.setName(nodeName);
//                cs.setType(type);
//
//                Map<String, String> caseMap = new HashMap<>();
//                json.get("cases").fields()
//                        .forEachRemaining(e -> caseMap.put(e.getKey(), e.getValue().asText()));
//
//                cs.setCases(caseMap);
//                return cs;
//            }
//
//            case "end" -> {
//                EndNodeDefinition e = new EndNodeDefinition();
//                e.setName(nodeName);
//                e.setType(type);
//                return e;
//            }
//
//            default -> throw new WorkflowParseException(
//                    "Unknown node type '" + type + "' in node '" + nodeName + "'");
//        }
    }
}
