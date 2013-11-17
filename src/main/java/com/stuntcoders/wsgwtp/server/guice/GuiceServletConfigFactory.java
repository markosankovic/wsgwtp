package com.stuntcoders.wsgwtp.server.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Keeps reference to injector. This injector can be accessed from any part of
 * the application by call to
 * {@linkplain GuiceServletConfigFactory#getInjector()}.
 */
public class GuiceServletConfigFactory {

    private static final Injector injector = Guice.createInjector(
            new ServerModule(), new DispatchServletModule());

    public static Injector getInjector() {
        return injector;
    }
}
