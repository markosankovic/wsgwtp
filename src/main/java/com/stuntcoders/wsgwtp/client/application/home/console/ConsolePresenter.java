package com.stuntcoders.wsgwtp.client.application.home.console;

import com.google.gwt.user.client.ui.Button;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.sksamuel.gwt.websockets.Websocket;
import com.stuntcoders.wsgwtp.client.event.JsonRPCResponseEvent;
import com.stuntcoders.wsgwtp.client.event.JsonRPCResponseEvent.JsonRPCResponseHandler;
import com.stuntcoders.wsgwtp.client.jsonrpc.JsonRPCRequest;

public class ConsolePresenter extends PresenterWidget<ConsolePresenter.MyView>
        implements ConsoleUiHandlers, JsonRPCResponseHandler {
    public interface MyView extends View, HasUiHandlers<ConsoleUiHandlers> {

        void appendText(String text);

        Button getInterruptButton();

        void removeInterruptButton();
    }

    private Websocket websocket;

    private JsonRPCRequest request;

    @Inject
    ConsolePresenter(EventBus eventBus, MyView view, Websocket websocket) {
        super(eventBus, view);

        getView().setUiHandlers(this);

        this.websocket = websocket;
    }

    protected void onBind() {
        super.onBind();

        this.addRegisteredHandler(JsonRPCResponseEvent.getType(), this);
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

        request = new JsonRPCRequest("exec");
        request.putParam("command", command);

        getView().appendText(request.toJSON());

        websocket.send(request.toJSON());
    }

    @Override
    public void interrupt() {
        JsonRPCRequest interrupt = new JsonRPCRequest("interrupt");
        interrupt.putParam("id", request.getId());

        getView().appendText(interrupt.toJSON());

        websocket.send(interrupt.toJSON());
    }

    @Override
    public void onJsonRPCResponse(JsonRPCResponseEvent event) {
        if (event.getJsonRPCResponse().getId().equals(request.getId())) {
            String result = event.getJsonRPCResponse().getResultAsString();
            if (result.equals("exec-done")) {
                getView().appendText(event.getJsonRPCResponse().toJSON());
            } else {
                getView().appendText(result);
            }
            getView().removeInterruptButton();
        }
    }
}
