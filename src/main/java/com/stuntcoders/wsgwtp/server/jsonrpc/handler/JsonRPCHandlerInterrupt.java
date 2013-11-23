package com.stuntcoders.wsgwtp.server.jsonrpc.handler;

import javax.websocket.Session;

import org.apache.log4j.Logger;

import com.stuntcoders.wsgwtp.server.jsonrpc.JsonRPCRequest;

/**
 * Interrupt previously started handler.
 */
public class JsonRPCHandlerInterrupt extends JsonRPCHandler {

    private static Logger logger = Logger
            .getLogger(JsonRPCHandlerInterrupt.class);

    public JsonRPCHandlerInterrupt(JsonRPCRequest jsonRPCRequest,
            Session session) {
        super(jsonRPCRequest, session);
    }

    @Override
    public void run() {
        String id = getParams().get("id").toString();
        logger.info("Interrupt: " + id);
        getFutureById(id).cancel(true);
    }
}
