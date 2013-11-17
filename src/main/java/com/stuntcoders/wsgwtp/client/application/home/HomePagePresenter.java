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

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.common.client.IndirectProvider;
import com.gwtplatform.common.client.StandardProvider;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.sksamuel.gwt.websockets.Websocket;
import com.stuntcoders.wsgwtp.client.application.ApplicationPresenter;
import com.stuntcoders.wsgwtp.client.application.home.console.ConsolePresenter;
import com.stuntcoders.wsgwtp.client.place.NameTokens;

public class HomePagePresenter extends
        Presenter<HomePagePresenter.MyView, HomePagePresenter.MyProxy>
        implements HomePageUiHandlers {

    public interface MyView extends View, HasUiHandlers<HomePageUiHandlers> {
        TextBox getExecuteTextBox();

        TabLayoutPanel getTabLayoutPanel();
    }

    IndirectProvider<ConsolePresenter> consolePresenterIndirectProvider;

    @ProxyStandard
    @NameToken(NameTokens.home)
    public interface MyProxy extends ProxyPlace<HomePagePresenter> {
    }

    @Inject
    public HomePagePresenter(EventBus eventBus, MyView view, MyProxy proxy,
            Provider<ConsolePresenter> consolePresenterProvider,
            Websocket websocket) {
        super(eventBus, view, proxy, ApplicationPresenter.SLOT_SetMainContent);

        getView().setUiHandlers(this);

        this.consolePresenterIndirectProvider = new StandardProvider<ConsolePresenter>(
                consolePresenterProvider);
    }

    @Override
    public void execute() {

        consolePresenterIndirectProvider
                .get(new AsyncCallback<ConsolePresenter>() {
                    @Override
                    public void onSuccess(ConsolePresenter consolePresenter) {
                        int tabCount = getView().getTabLayoutPanel()
                                .getWidgetCount();

                        String command = getView().getExecuteTextBox()
                                .getText();

                        getView().getTabLayoutPanel().add(
                                consolePresenter.getView().asWidget(),
                                command.length() <= 32 ? command : command
                                        .substring(0, 32));
                        getView().getTabLayoutPanel().selectTab(tabCount);

                        consolePresenter.execute(command);
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        // NO-OP
                    }
                });
    }

}
