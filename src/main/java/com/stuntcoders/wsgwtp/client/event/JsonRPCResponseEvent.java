package com.stuntcoders.wsgwtp.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.json.client.JSONObject;

public class JsonRPCResponseEvent extends
        GwtEvent<JsonRPCResponseEvent.JsonRPCResponseHandler> {

    public static Type<JsonRPCResponseHandler> TYPE = new Type<JsonRPCResponseHandler>();
    private JSONObject jsonObject;

    public interface JsonRPCResponseHandler extends EventHandler {
        void onJSONRPCResponse(JSONObject jsonObject);
    }

    public JsonRPCResponseEvent(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public JSONObject getJSONObject() {
        return jsonObject;
    }

    @Override
    protected void dispatch(JsonRPCResponseHandler handler) {
        handler.onJSONRPCResponse(jsonObject);
    }

    @Override
    public Type<JsonRPCResponseHandler> getAssociatedType() {
        return TYPE;
    }

    public static Type<JsonRPCResponseHandler> getType() {
        return TYPE;
    }

    public static void fire(HasHandlers source, JSONObject jsonObject) {
        source.fireEvent(new JsonRPCResponseEvent(jsonObject));
    }
}
