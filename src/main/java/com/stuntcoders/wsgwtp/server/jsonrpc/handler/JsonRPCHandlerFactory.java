package com.stuntcoders.wsgwtp.server.jsonrpc.handler;

import javax.websocket.Session;

import com.google.inject.Inject;
import com.stuntcoders.wsgwtp.server.jsonrpc.JsonRPCRequest;

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

    public JsonRPCHandler create(JsonRPCRequest jsonRPCRequest, Session session) {
        switch (jsonRPCRequest.getMethod()) {
        case "exec":
            return new JsonRPCHandlerExec(jsonRPCRequest, session);
        case "interrupt":
            return new JsonRPCHandlerInterrupt(jsonRPCRequest, session);
        default:
            return null;
        }
    }
}
