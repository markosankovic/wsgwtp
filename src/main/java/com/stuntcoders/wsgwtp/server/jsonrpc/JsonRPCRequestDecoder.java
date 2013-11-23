package com.stuntcoders.wsgwtp.server.jsonrpc;

import java.io.IOException;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * Decode serialized JSON received from a client.
 */
public class JsonRPCRequestDecoder implements Decoder.Text<JsonRPCRequest> {

    @Override
    public void init(EndpointConfig config) {
        // NOP
    }

    @Override
    public void destroy() {
        // NOP
    }

    @Override
    public JsonRPCRequest decode(String s) throws DecodeException {

        ObjectMapper mapper = new ObjectMapper();
        JsonRPCRequest jsonRPCRequest = null;

        try {
            jsonRPCRequest = mapper.readValue(s, JsonRPCRequest.class);
        } catch (IOException e) {
            e.printStackTrace();
            /**
             * TODO Handle exceptions according to JSON-RPC 2.0 Specification,
             * e.g. -32700 Parse error Invalid JSON was received by the server.
             * An error occurred on the server while parsing the JSON text.
             */
        }

        return jsonRPCRequest;
    }

    @Override
    public boolean willDecode(String s) {
        return true;
    }
}
