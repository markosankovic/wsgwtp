package com.stuntcoders.wsgwtp.server;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

public class JsonRPCResponseFactory {

    public static final ObjectMapper mapper = new ObjectMapper();

    public static ObjectNode result(String id, ObjectNode result) {

        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("jsonrpc", "2.0");
        objectNode.put("result", result);
        objectNode.put("id", id);

        return objectNode;
    }

    public static JsonNode error(String id, ObjectNode error) {

        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("jsonrpc", "2.0");
        objectNode.put("error", error);
        objectNode.put("id", id);

        return objectNode;
    }
}
