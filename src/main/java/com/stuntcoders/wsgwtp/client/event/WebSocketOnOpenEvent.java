package com.stuntcoders.wsgwtp.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.sksamuel.gwt.websockets.Websocket;
import com.google.gwt.event.shared.HasHandlers;

public class WebSocketOnOpenEvent extends
        GwtEvent<WebSocketOnOpenEvent.WebSocketOnOpenHandler> {

    public static Type<WebSocketOnOpenHandler> TYPE = new Type<WebSocketOnOpenHandler>();
    private Websocket websocket;

    public interface WebSocketOnOpenHandler extends EventHandler {
        void onWebSocketOnOpen(WebSocketOnOpenEvent event);
    }

    public WebSocketOnOpenEvent(Websocket websocket) {
        this.websocket = websocket;
    }

    public Websocket getWebsocket() {
        return websocket;
    }

    @Override
    protected void dispatch(WebSocketOnOpenHandler handler) {
        handler.onWebSocketOnOpen(this);
    }

    @Override
    public Type<WebSocketOnOpenHandler> getAssociatedType() {
        return TYPE;
    }

    public static Type<WebSocketOnOpenHandler> getType() {
        return TYPE;
    }

    public static void fire(HasHandlers source, Websocket websocket) {
        source.fireEvent(new WebSocketOnOpenEvent(websocket));
    }
}
