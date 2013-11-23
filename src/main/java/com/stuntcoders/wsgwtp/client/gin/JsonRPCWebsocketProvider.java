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

        Websocket socket = new Websocket(websocketBaseURL
                + "/JsonRPCWebsocketEndpoint");

        socket.addListener(new WebsocketListener() {

            @Override
            public void onClose() {
                logger.info("WebSocket connection closed on: " + new Date());
            }

            @Override
            public void onMessage(String msg) {
                eventBus.fireEvent(new JsonRPCResponseEvent(
                        (JSONObject) JSONParser.parseLenient(msg)));
            }

            @Override
            public void onOpen() {
                logger.info("WebSocket connection opened on: " + new Date());
            }
        });

        socket.open();

        return socket;
    }
}
