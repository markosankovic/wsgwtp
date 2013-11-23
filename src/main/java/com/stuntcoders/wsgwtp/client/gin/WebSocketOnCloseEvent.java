package com.stuntcoders.wsgwtp.client.gin;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.sksamuel.gwt.websockets.Websocket;
import com.google.gwt.event.shared.HasHandlers;

public class WebSocketOnCloseEvent extends
        GwtEvent<WebSocketOnCloseEvent.WebSocketOnCloseHandler> {

    public static Type<WebSocketOnCloseHandler> TYPE = new Type<WebSocketOnCloseHandler>();
    private Websocket websocket;

    public interface WebSocketOnCloseHandler extends EventHandler {
        void onWebSocketOnClose(WebSocketOnCloseEvent event);
    }

    public WebSocketOnCloseEvent(Websocket websocket) {
        this.websocket = websocket;
    }

    public Websocket getWebsocket() {
        return websocket;
    }

    @Override
    protected void dispatch(WebSocketOnCloseHandler handler) {
        handler.onWebSocketOnClose(this);
    }

    @Override
    public Type<WebSocketOnCloseHandler> getAssociatedType() {
        return TYPE;
    }

    public static Type<WebSocketOnCloseHandler> getType() {
        return TYPE;
    }

    public static void fire(HasHandlers source, Websocket websocket) {
        source.fireEvent(new WebSocketOnCloseEvent(websocket));
    }
}
