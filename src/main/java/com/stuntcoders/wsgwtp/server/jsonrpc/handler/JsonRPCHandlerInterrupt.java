package com.stuntcoders.wsgwtp.server.jsonrpc.handler;

import javax.websocket.Session;

import org.codehaus.jackson.JsonNode;

public class JsonRPCHandlerInterrupt extends JsonRPCHandler {

    public JsonRPCHandlerInterrupt(JsonNode jsonNode, Session session) {
        super(jsonNode, session);
    }

    @Override
    public void run() {
        getCurrentFuture().cancel(true);
        removeFuture();
    }
}
