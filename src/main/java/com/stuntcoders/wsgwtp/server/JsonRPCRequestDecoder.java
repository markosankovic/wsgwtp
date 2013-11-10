package com.stuntcoders.wsgwtp.server;

import java.io.IOException;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

public class JsonRPCRequestDecoder implements Decoder.Text<JsonNode> {

    @Override
    public void init(EndpointConfig config) {
        // NOP
    }

    @Override
    public void destroy() {
        // NOP
    }

    @Override
    public JsonNode decode(String s) throws DecodeException {

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = null;

        try {
            jsonNode = mapper.readTree(s);
        } catch (IOException e) {
            /**
             * -32700 Parse error Invalid JSON was received by the server. An
             * error occurred on the server while parsing the JSON text.
             */
        }

        return jsonNode;
    }

    @Override
    public boolean willDecode(String s) {
        return true;
    }
}
