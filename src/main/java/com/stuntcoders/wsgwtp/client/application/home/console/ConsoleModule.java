package com.stuntcoders.wsgwtp.client.application.home.console;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ConsoleModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenterWidget(ConsolePresenter.class,
                ConsolePresenter.MyView.class, ConsoleView.class);
    }
}
