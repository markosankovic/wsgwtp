package com.stuntcoders.wsgwtp.server.jsonrpc;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

/**
 * JsonRPCResponseBuilder.
 * 
 * Build different type of responses according to the JSON-RPC 2.0
 * Specification.
 * 
 * @see http://www.jsonrpc.org/specification#response_object
 */
public class JsonRPCResponseBuilder {

    public static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Response object.
     * 
     * @param id
     * @param result
     * @return
     */
    public static JsonRPCResponseResult result(String id, ObjectNode result) {
        JsonRPCResponseResult response = result(id);
        response.setResult(mapper.createObjectNode());
        return response;
    }

    /**
     * Response object without result.
     * 
     * @param id
     * @return
     */
    public static JsonRPCResponseResult result(String id) {
        JsonRPCResponseResult response = new JsonRPCResponseResult();
        response.setId(id);
        response.setJsonrpc("2.0");
        return response;
    }

    /**
     * Response object with result as String.
     * 
     * @param id
     * @return
     */
    public static JsonRPCResponseResult result(String id, String result) {
        JsonRPCResponseResult response = result(id);
        response.setResult(result);
        return response;
    }

    public static JsonRPCResponseError error() {
        // TODO Implement
        return null;
    }
}
