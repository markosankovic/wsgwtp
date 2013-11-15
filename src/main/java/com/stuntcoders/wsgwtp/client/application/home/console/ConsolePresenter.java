package com.stuntcoders.wsgwtp.client.application.home.console;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.sksamuel.gwt.websockets.Websocket;
import com.stuntcoders.wsgwtp.client.JsonRPCRequestBuilder;
import com.stuntcoders.wsgwtp.client.event.JsonRPCResponseEvent;
import com.stuntcoders.wsgwtp.client.event.JsonRPCResponseEvent.JsonRPCResponseHandler;

public class ConsolePresenter extends PresenterWidget<ConsolePresenter.MyView>
        implements ConsoleUiHandlers, JsonRPCResponseHandler {
    public interface MyView extends View, HasUiHandlers<ConsoleUiHandlers> {

        public void appendText(String text);
    }

    private Websocket websocket;

    private JSONObject jsonObject;

    @Inject
    ConsolePresenter(EventBus eventBus, MyView view, Websocket websocket) {
        super(eventBus, view);

        getView().setUiHandlers(this);

        this.websocket = websocket;
    }

    protected void onBind() {
        super.onBind();

        this.addRegisteredHandler(JsonRPCResponseEvent.TYPE, this);
    }

    protected void onHide() {
        super.onHide();
    }

    protected void onUnbind() {
        super.onUnbind();
    }

    protected void onReset() {
        super.onReset();
    }

    public void execute(String command) {
        JSONObject params = new JSONObject();
        params.put("command", new JSONString(command));

        jsonObject = JsonRPCRequestBuilder.request("exec", params);

        websocket.send(jsonObject.toString());
    }

    @Override
    public void interrupt() {
        JSONObject interruptJsonObject = JsonRPCRequestBuilder
                .interrupt(jsonObject.get("id"));
        websocket.send(interruptJsonObject.toString());
    }

    @Override
    public void onJSONRPCResponse(JSONObject jsonObject) {
        if (jsonObject.get("id").equals(
                ConsolePresenter.this.jsonObject.get("id"))) {
            JSONString result = (JSONString) jsonObject.get("result");

            if (result.stringValue().equals("jsonrpc-done")) {
                // NO-OP
            } else {
                getView().appendText(result.stringValue());
            }
        }
    }
}
