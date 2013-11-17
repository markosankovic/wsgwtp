package com.stuntcoders.wsgwtp.server.jsonrpc.handler;

import javax.websocket.Session;

import org.codehaus.jackson.JsonNode;

import com.google.inject.Inject;

/**
 * Build JSONRPCHandler.
 * 
 * Concrete implementation of handler depends on the JSON-RPC request coming
 * from a client.
 */
public class JsonRPCHandlerFactory {

    @Inject
    public JsonRPCHandlerFactory() {
    }

    public JsonRPCHandler create(JsonNode jsonNode, Session session) {
        switch (jsonNode.get("method").asText()) {
        case "exec":
            return new JsonRPCHandlerExec(jsonNode, session);
        case "interrupt":
            return new JsonRPCHandlerInterrupt(jsonNode, session);
        default:
            return null;
        }
    }
}
