package com.stuntcoders.wsgwtp.server;

import javax.websocket.server.ServerEndpointConfig.Configurator;

import com.stuntcoders.wsgwtp.server.guice.GuiceServletConfigFactory;

public class JsonRPCWebsocketEndpointConfigurator extends Configurator {

    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass)
            throws InstantiationException {
        return (T) GuiceServletConfigFactory.getInjector().getInstance(
                endpointClass);
    }
}
