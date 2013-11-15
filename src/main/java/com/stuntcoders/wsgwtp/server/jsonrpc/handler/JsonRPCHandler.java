package com.stuntcoders.wsgwtp.server.jsonrpc.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import javax.websocket.Session;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import com.stuntcoders.wsgwtp.server.jsonrpc.JsonRPCResponseBuilder;

public abstract class JsonRPCHandler implements Runnable {

    private String method;

    private JsonNode params;

    private String id;

    private Session session;

    public JsonRPCHandler(JsonNode jsonNode, Session session) {
        this.method = jsonNode.get("method").getTextValue();
        this.params = jsonNode.get("params");
        this.id = jsonNode.get("id").getTextValue();
        this.session = session;
    }

    public String getMethod() {
        return method;
    }

    public JsonNode getParams() {
        return params;
    }

    public String getId() {
        return id;
    }

    public Session getSession() {
        return session;
    }

    public Map<String, Future<?>> getFutures() {
        return JsonRPCHandler.getFuturesForSession(session);
    }

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
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    removeFuture(); // Remove done future from session
                }
            }
        };
    }

    public Future<?> getCurrentFuture() {
        return getFutures().get(id);
    }

    public void removeFuture() {
        getFutures().remove(id);
    }

    public static void cancelFuturesForSession(Session session) {
        Map<String, Future<?>> futures = JsonRPCHandler
                .getFuturesForSession(session);
        for (Future<?> future : futures.values()) {
            future.cancel(true);
        }
        session.getUserProperties().clear();
    }

    public Future<?> putFuture(Future<?> future) {
        return getFutures().put(id, future);
    }
}
