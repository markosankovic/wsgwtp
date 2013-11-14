package com.stuntcoders.wsgwtp.server.jsonrpc.handler;

import javax.websocket.Session;

import org.codehaus.jackson.JsonNode;

public class JsonRPCHandlerFactory {

    public static JsonRPCHandler create(JsonNode jsonNode, Session session) {
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
