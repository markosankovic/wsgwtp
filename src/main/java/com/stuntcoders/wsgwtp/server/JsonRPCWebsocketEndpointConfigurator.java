package com.stuntcoders.wsgwtp.server;

import javax.websocket.server.ServerEndpointConfig.Configurator;

import com.stuntcoders.wsgwtp.server.guice.GuiceServletConfigFactory;

/**
 * Instantiates WebSocket end-point with Guice injector so that @Inject can be
 * used in JsonRPCWebsocketEndpoint.
 */
public class JsonRPCWebsocketEndpointConfigurator extends Configurator {

    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass)
            throws InstantiationException {
        return (T) GuiceServletConfigFactory.getInjector().getInstance(
                endpointClass);
    }
}
