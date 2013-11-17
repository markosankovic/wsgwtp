package com.stuntcoders.wsgwtp.server.jsonrpc.handler;

import javax.websocket.Session;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;

/**
 * Interrupt previously started handler.
 */
public class JsonRPCHandlerInterrupt extends JsonRPCHandler {

    private static Logger logger = Logger
            .getLogger(JsonRPCHandlerInterrupt.class);

    public JsonRPCHandlerInterrupt(JsonNode jsonNode, Session session) {
        super(jsonNode, session);
    }

    @Override
    public void run() {
        String id = getParams().get("id").getTextValue();
        logger.info("Interrupt future with request id: " + id);
        getFutureById(id).cancel(true);
    }
}
