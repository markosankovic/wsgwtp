/**
 * Copyright 2012 ArcBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.stuntcoders.wsgwtp.client.application.home;

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.TextBox;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.sksamuel.gwt.websockets.Websocket;
import com.stuntcoders.wsgwtp.client.JsonRPCRequestFactory;
import com.stuntcoders.wsgwtp.client.application.ApplicationPresenter;
import com.stuntcoders.wsgwtp.client.event.JsonRPCResponseEvent;
import com.stuntcoders.wsgwtp.client.event.JsonRPCResponseEvent.JsonRPCResponseHandler;
import com.stuntcoders.wsgwtp.client.place.NameTokens;

public class HomePagePresenter extends
        Presenter<HomePagePresenter.MyView, HomePagePresenter.MyProxy>
        implements HomePageUiHandlers, JsonRPCResponseHandler {

    public interface MyView extends View, HasUiHandlers<HomePageUiHandlers> {
        TextBox getMessageTextBox();
    }

    @ProxyStandard
    @NameToken(NameTokens.home)
    public interface MyProxy extends ProxyPlace<HomePagePresenter> {
    }

    Websocket socket;

    @Inject
    public HomePagePresenter(EventBus eventBus, MyView view, MyProxy proxy,
            Websocket socket) {
        super(eventBus, view, proxy, ApplicationPresenter.SLOT_SetMainContent);

        getView().setUiHandlers(this);

        this.socket = socket;

        this.addRegisteredHandler(JsonRPCResponseEvent.TYPE, this);
    }

    @Override
    public void send() {

        JSONObject params = new JSONObject();
        params.put("subtrahend", new JSONNumber(23));
        params.put("minuend", new JSONNumber(42));

        JSONObject jsonObject = JsonRPCRequestFactory.request("exec", params);

        socket.send(jsonObject.toString());
    }

    @Override
    public void onJSONRPCResponse(JSONObject jsonObject) {
        Window.alert(jsonObject.toString());
    }
}
