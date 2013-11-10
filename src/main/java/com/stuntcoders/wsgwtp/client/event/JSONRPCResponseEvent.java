package com.stuntcoders.wsgwtp.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.json.client.JSONObject;

public class JSONRPCResponseEvent extends
        GwtEvent<JSONRPCResponseEvent.JSONRPCResponseHandler> {

    public static Type<JSONRPCResponseHandler> TYPE = new Type<JSONRPCResponseHandler>();
    private JSONObject jsonObject;

    public interface JSONRPCResponseHandler extends EventHandler {
        void onJSONRPCResponse(JSONObject jsonObject);
    }

    public JSONRPCResponseEvent(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public JSONObject getJSONObject() {
        return jsonObject;
    }

    @Override
    protected void dispatch(JSONRPCResponseHandler handler) {
        handler.onJSONRPCResponse(jsonObject);
    }

    @Override
    public Type<JSONRPCResponseHandler> getAssociatedType() {
        return TYPE;
    }

    public static Type<JSONRPCResponseHandler> getType() {
        return TYPE;
    }

    public static void fire(HasHandlers source, JSONObject jsonObject) {
        source.fireEvent(new JSONRPCResponseEvent(jsonObject));
    }
}
