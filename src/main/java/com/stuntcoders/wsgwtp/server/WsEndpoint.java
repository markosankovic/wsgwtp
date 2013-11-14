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

import org.codehaus.jackson.JsonNode;

import com.stuntcoders.wsgwtp.server.jsonrpc.JsonRPCRequestDecoder;
import com.stuntcoders.wsgwtp.server.jsonrpc.handler.JsonRPCHandler;
import com.stuntcoders.wsgwtp.server.jsonrpc.handler.JsonRPCHandlerFactory;

@ServerEndpoint(value = "/wsendpoint", configurator = WsEndpointConfigurator.class, decoders = JsonRPCRequestDecoder.class)
public class WsEndpoint {

    /**
     * Creates a thread pool that creates new threads as needed. Use for
     * executing client commands.
     */
    private static final ExecutorService executorService = Executors
            .newCachedThreadPool();

    @OnOpen
    public void onOpen(Session session) {
        session.getUserProperties().put("futures",
                new HashMap<String, Future<?>>());
    }

    @OnClose
    public void onClose(Session session) {
        JsonRPCHandler.cancelFuturesForSession(session);
    }

    @OnMessage
    public void handleJsonRPCRequest(JsonNode jsonNode, Session session) {
        JsonRPCHandler jsonRPCHandler = JsonRPCHandlerFactory.create(jsonNode,
                session);

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
