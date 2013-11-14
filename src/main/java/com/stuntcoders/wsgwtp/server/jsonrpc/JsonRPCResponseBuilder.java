package com.stuntcoders.wsgwtp.server.jsonrpc;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

/**
 * JsonRPCResponseBuilder
 * 
 * @see http://www.jsonrpc.org/specification#response_object
 */
public class JsonRPCResponseBuilder {

    public static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Response object
     * 
     * @param id
     * @param result
     * @return
     */
    public static ObjectNode result(String id, ObjectNode result) {
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("jsonrpc", "2.0");
        objectNode.put("result", result);
        objectNode.put("id", id);
        return objectNode;
    }

    /**
     * Response object without result
     * 
     * @param id
     * @return
     */
    public static ObjectNode result(String id) {
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("jsonrpc", "2.0");
        objectNode.put("id", id);
        return objectNode;
    }

    /**
     * Response object with result as String
     * 
     * @param id
     * @return
     */
    public static ObjectNode result(String id, String result) {
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("jsonrpc", "2.0");
        objectNode.put("result", result);
        objectNode.put("id", id);
        return objectNode;
    }

    /**
     * Error response object
     * 
     * @see http://www.jsonrpc.org/specification#error_object
     * 
     * @param id
     * @param error
     * @return
     */
    public static JsonNode error(String id, ObjectNode error) {
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("jsonrpc", "2.0");
        objectNode.put("error", error);
        objectNode.put("id", id);
        return objectNode;
    }
}
