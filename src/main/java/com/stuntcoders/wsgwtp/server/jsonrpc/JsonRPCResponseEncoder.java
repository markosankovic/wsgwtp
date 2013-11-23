package com.stuntcoders.wsgwtp.server.jsonrpc;

import java.io.IOException;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import org.codehaus.jackson.map.ObjectMapper;

public class JsonRPCResponseEncoder implements Encoder.Text<JsonRPCResponse> {

    @Override
    public void init(EndpointConfig config) {
    }

    @Override
    public void destroy() {
    }

    @Override
    public String encode(JsonRPCResponse object) throws EncodeException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (IOException e) {
            e.printStackTrace();
            throw new EncodeException(object, e.getMessage());
        }
    }
}
