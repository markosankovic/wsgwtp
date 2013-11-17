package com.stuntcoders.wsgwtp.server.jsonrpc.handler;

import javax.websocket.Session;

import org.codehaus.jackson.JsonNode;

public class JsonRPCHandlerInterrupt extends JsonRPCHandler {

    public JsonRPCHandlerInterrupt(JsonNode jsonNode, Session session) {
        super(jsonNode, session);
    }

    @Override
    public void run() {
        System.out.println(this);
        String id = getParams().get("id").getTextValue();
        System.out.println("Interrupt future with request id: " + id);
        getFutureById(id).cancel(true);
    }
}
