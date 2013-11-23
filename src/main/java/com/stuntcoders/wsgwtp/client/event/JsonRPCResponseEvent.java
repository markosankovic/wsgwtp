package com.stuntcoders.wsgwtp.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.stuntcoders.wsgwtp.client.jsonrpc.JsonRPCResponse;
import com.google.gwt.event.shared.HasHandlers;

public class JsonRPCResponseEvent extends
        GwtEvent<JsonRPCResponseEvent.JsonRPCResponseHandler> {

    public static Type<JsonRPCResponseHandler> TYPE = new Type<JsonRPCResponseHandler>();
    private JsonRPCResponse jsonRPCResponse;

    public interface JsonRPCResponseHandler extends EventHandler {
        void onJsonRPCResponse(JsonRPCResponseEvent event);
    }

    public JsonRPCResponseEvent(JsonRPCResponse jsonRPCResponse) {
        this.jsonRPCResponse = jsonRPCResponse;
    }

    public JsonRPCResponse getJsonRPCResponse() {
        return jsonRPCResponse;
    }

    @Override
    protected void dispatch(JsonRPCResponseHandler handler) {
        handler.onJsonRPCResponse(this);
    }

    @Override
    public Type<JsonRPCResponseHandler> getAssociatedType() {
        return TYPE;
    }

    public static Type<JsonRPCResponseHandler> getType() {
        return TYPE;
    }

    public static void fire(HasHandlers source, JsonRPCResponse jsonRPCResponse) {
        source.fireEvent(new JsonRPCResponseEvent(jsonRPCResponse));
    }
}
