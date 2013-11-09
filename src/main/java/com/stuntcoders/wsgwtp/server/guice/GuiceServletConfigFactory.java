package com.stuntcoders.wsgwtp.server.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class GuiceServletConfigFactory {

    private static final Injector injector = Guice.createInjector(
            new ServerModule(), new DispatchServletModule());

    public static Injector getInjector() {
        return injector;
    }
}
