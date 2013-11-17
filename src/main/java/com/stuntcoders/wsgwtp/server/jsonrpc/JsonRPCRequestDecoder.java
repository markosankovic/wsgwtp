package com.stuntcoders.wsgwtp.server.jsonrpc;

import java.io.IOException;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Decode serialized JSON received from a client.
 */
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
             * TODO Handle exceptions according to JSON-RPC 2.0 Specification,
             * e.g. -32700 Parse error Invalid JSON was received by the server.
             * An error occurred on the server while parsing the JSON text.
             */
        }

        return jsonNode;
    }

    @Override
    public boolean willDecode(String s) {
        return true;
    }
}
