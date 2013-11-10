package com.stuntcoders.wsgwtp.client.gin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;
import com.sksamuel.gwt.websockets.Websocket;
import com.sksamuel.gwt.websockets.WebsocketListener;
import com.stuntcoders.wsgwtp.client.event.JsonRPCResponseEvent;

public class WebsocketProvider implements Provider<Websocket> {

    @Inject
    EventBus eventBus;

    @Override
    public Websocket get() {
        /**
         * Example: ws://localhost:8080/wsgwtp/
         */
        String wsBaseURL = GWT.getHostPageBaseURL().replace("http://", "ws://");

        Websocket socket = new Websocket(wsBaseURL + "/wsendpoint");

        socket.addListener(new WebsocketListener() {

            @Override
            public void onClose() {
                Window.alert("onClose");
            }

            @Override
            public void onMessage(String msg) {
                eventBus.fireEvent(new JsonRPCResponseEvent(
                        (JSONObject) JSONParser.parseLenient(msg)));
            }

            @Override
            public void onOpen() {
                // Window.alert("onOpen");
            }
        });

        socket.open();

        return socket;
    }
}
