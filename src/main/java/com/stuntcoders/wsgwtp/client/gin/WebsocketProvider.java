package com.stuntcoders.wsgwtp.client.gin;

import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;
import com.sksamuel.gwt.websockets.Websocket;
import com.sksamuel.gwt.websockets.WebsocketListener;

public class WebsocketProvider implements Provider<Websocket> {

    @Inject
    EventBus eventBus;

    @Override
    public Websocket get() {

        Websocket socket = new Websocket(
                "ws://localhost:8080/wsgwtp-1.0-SNAPSHOT/wsendpoint");

        socket.addListener(new WebsocketListener() {

            @Override
            public void onClose() {
                Window.alert("onClose");
            }

            @Override
            public void onMessage(String msg) {
                Window.alert(msg);
            }

            @Override
            public void onOpen() {
                Window.alert("onOpen");
            }
        });

        socket.open();

        return socket;
    }
}
