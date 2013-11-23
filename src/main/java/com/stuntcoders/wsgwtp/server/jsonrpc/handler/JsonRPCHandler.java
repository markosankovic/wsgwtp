package com.stuntcoders.wsgwtp.server.jsonrpc.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import javax.websocket.Session;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import com.stuntcoders.wsgwtp.server.jsonrpc.JsonRPCRequest;
import com.stuntcoders.wsgwtp.server.jsonrpc.JsonRPCResponseBuilder;

public abstract class JsonRPCHandler implements Runnable {

    private static Logger logger = Logger.getLogger(JsonRPCHandler.class);

    /**
     * JSON-RPC request.
     */
    private JsonRPCRequest jsonRPCRequest;

    /**
     * WebSocket Session.
     */
    private Session session;

    /**
     * JsonRPCHandler.
     * 
     * @param jsonRPCRequest
     * @param session
     */
    public JsonRPCHandler(JsonRPCRequest jsonRPCRequest, Session session) {
        this.jsonRPCRequest = jsonRPCRequest;
        this.session = session;
    }

    /**
     * Get method associated with JSON-RPC request.
     * 
     * @return
     */
    public String getMethod() {
        return jsonRPCRequest.getMethod();
    }

    /**
     * Get params associated with JSON-RPC request.
     * 
     * @return
     */
    public Map<String, Object> getParams() {
        return jsonRPCRequest.getParams();
    }

    /**
     * Get JSON-RPC request id.
     * 
     * @return
     */
    public String getId() {
        return (String) jsonRPCRequest.getId();
    }

    /**
     * Get WebSocket Session.
     * 
     * @return
     */
    public Session getSession() {
        return session;
    }

    /**
     * Get futures from WebSocket session of this handler.
     * 
     * @return
     */
    public Map<String, Future<?>> getFutures() {
        return JsonRPCHandler.getFuturesForSession(session);
    }

    /**
     * Get futures of any WebSocket session.
     * 
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Future<?>> getFuturesForSession(Session session) {
        return (HashMap<String, Future<?>>) session.getUserProperties().get(
                "futures");
    }

    /**
     * Wrap runnable JsonRPCHandler with code that removes future representing
     * this thread and notifies client that JSON-RPC request with certain id has
     * completed.
     * 
     * @return
     */
    public Thread getCleanFutureThread() {
        return new Thread() {
            public void run() {
                try {
                    JsonRPCHandler.this.run();
                } finally {
                    try {
                        ObjectNode response = JsonRPCResponseBuilder.result(
                                JsonRPCHandler.this.getId(), "jsonrpc-done");
                        getSession().getBasicRemote().sendText(
                                JsonRPCResponseBuilder.mapper
                                        .writeValueAsString(response));
                        logger.info("Clean future finally done: "
                                + JsonRPCHandler.this.getId());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    removeFuture(); // Remove done future from session
                }
            }
        };
    }

    /**
     * Get future by request id.
     * 
     * @param id
     * @return
     */
    public Future<?> getFutureById(String id) {
        return getFutures().get(id);
    }

    /**
     * Remove future of this handler from WebSocket session futures.
     */
    public void removeFuture() {
        getFutures().remove(getId());
        logger.info("Number of futures for " + session.getId() + " session: "
                + getFutures().size());
    }

    /**
     * Cancel all futures of a session.
     * 
     * @param session
     */
    public static void cancelFuturesForSession(Session session) {
        Map<String, Future<?>> futures = JsonRPCHandler
                .getFuturesForSession(session);
        for (Future<?> future : futures.values()) {
            future.cancel(true);
        }
    }

    /**
     * Add future to the list of futures for this handler.
     * 
     * @param future
     * @return
     */
    public Future<?> putFuture(Future<?> future) {
        return getFutures().put(getId(), future);
    }

    /**
     * Send serialized JSON to a WebSocket peer.
     * 
     * @param response
     */
    public void writeResponseAsStringToRemote(JsonNode response) {
        try {
            session.getBasicRemote().sendText(
                    JsonRPCResponseBuilder.mapper.writeValueAsString(response));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * String representation of this handler.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append(getClass().getName());
        sb.append(": ");
        sb.append(getId());

        return sb.toString();
    }
}
