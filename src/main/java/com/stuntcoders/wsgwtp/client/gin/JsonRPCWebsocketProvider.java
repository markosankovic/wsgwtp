package com.stuntcoders.wsgwtp.client.gin;

import java.util.Date;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;
import com.sksamuel.gwt.websockets.Websocket;
import com.sksamuel.gwt.websockets.WebsocketListener;
import com.stuntcoders.wsgwtp.client.event.JsonRPCResponseEvent;
import com.stuntcoders.wsgwtp.client.event.WebSocketOnOpenEvent;
import com.stuntcoders.wsgwtp.client.jsonrpc.JsonRPCResponse;

public class JsonRPCWebsocketProvider implements Provider<Websocket> {

    private static Logger logger = Logger
            .getLogger(JsonRPCWebsocketProvider.class.getName());

    @Inject
    EventBus eventBus;

    @Override
    public Websocket get() {
        /**
         * Example: ws://localhost:8080/wsgwtp/
         */
        String websocketBaseURL = GWT.getHostPageBaseURL().replace("http://",
                "ws://");

        final Websocket websocket = new Websocket(websocketBaseURL
                + "/JsonRPCWebsocketEndpoint");

        websocket.addListener(new WebsocketListener() {

            @Override
            public void onClose() {
                logger.info("WebSocket connection closed on: " + new Date());
                eventBus.fireEvent(new WebSocketOnCloseEvent(websocket));
            }

            @Override
            public void onMessage(String msg) {
                logger.info("WebSocket message received: " + msg);

                JSONObject jsonObject = (JSONObject) JSONParser
                        .parseLenient(msg);

                JsonRPCResponse jsonRPCResponse = new JsonRPCResponse(
                        jsonObject);

                eventBus.fireEvent(new JsonRPCResponseEvent(jsonRPCResponse));
            }

            @Override
            public void onOpen() {
                logger.info("WebSocket connection opened on: " + new Date());
                eventBus.fireEvent(new WebSocketOnOpenEvent(websocket));
            }
        });

        websocket.open();

        return websocket;
    }
}
