package com.stuntcoders.wsgwtp.server;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.PongMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.stuntcoders.wsgwtp.server.jsonrpc.JsonRPCRequest;
import com.stuntcoders.wsgwtp.server.jsonrpc.JsonRPCRequestDecoder;
import com.stuntcoders.wsgwtp.server.jsonrpc.handler.JsonRPCHandler;
import com.stuntcoders.wsgwtp.server.jsonrpc.handler.JsonRPCHandlerFactory;

@ServerEndpoint(value = "/JsonRPCWebsocketEndpoint", configurator = JsonRPCWebsocketEndpointConfigurator.class, decoders = JsonRPCRequestDecoder.class)
public class JsonRPCWebsocketEndpoint {

    private static Logger logger = Logger
            .getLogger(JsonRPCWebsocketEndpoint.class);

    @Inject
    JsonRPCHandlerFactory jsonRPCHandlerFactory;

    /**
     * Creates a thread pool that creates new threads as needed. Use for
     * executing client commands.
     */
    private static final ExecutorService executorService = Executors
            .newCachedThreadPool();

    @OnOpen
    public void onOpen(Session session) {
        /**
         * Session will never timeout due to inactivity.
         */
        session.setMaxIdleTimeout(0);

        /**
         * Each session has futures (handlers) associated with. All handlers are
         * executed in a separate thread. Futures are mapped by request id.
         */
        session.getUserProperties().put("futures",
                new HashMap<String, Future<?>>());
    }

    @OnClose
    public void onClose(Session session) {
        /**
         * Cancel all running handlers associated with this session.
         */
        JsonRPCHandler.cancelFuturesForSession(session);
    }

    @OnMessage
    public void handleJsonRPCRequest(JsonRPCRequest jsonRPCRequest,
            Session session) {
        // Build concrete request handler
        JsonRPCHandler jsonRPCHandler = jsonRPCHandlerFactory.create(
                jsonRPCRequest, session);
        logger.info(jsonRPCHandler.toString());
        // Execute request handler
        jsonRPCHandler.putFuture(executorService.submit(jsonRPCHandler
                .getCleanFutureThread()));
    }

    /**
     * Process a received pong. This is a NO-OP.
     * 
     * @param pm
     */
    @OnMessage
    public void echoPongMessage(PongMessage pm) {
        // NO-OP
    }
}
