package com.stuntcoders.wsgwtp.server;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.PongMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

@ServerEndpoint(value = "/wsendpoint", configurator = WsEndpointConfigurator.class, decoders = JsonRPCRequestDecoder.class)
public class WsEndpoint {

    private static Set<Session> peers = Collections
            .synchronizedSet(new HashSet<Session>());

    @OnOpen
    public void onOpen(Session peer) {
        peers.add(peer);
    }

    @OnClose
    public void onClose(Session peer) {
        peers.remove(peer);
    }

    @OnMessage
    public void handleJsonRPCRequest(JsonNode jsonRequest, Session session) {

        ObjectMapper mapper = new ObjectMapper();

        try {
            if (session.isOpen()) {
                StringWriter writer = new StringWriter();
                ObjectNode result = JsonRPCResponseFactory.mapper.createObjectNode();
                result.put("status", "OK");
                ObjectNode response = JsonRPCResponseFactory.result(
                        jsonRequest.get("id").asText(), result);
                mapper.writeValue(writer, response);
                session.getBasicRemote().sendText(writer.toString());
            }
        } catch (IOException e) {
            try {
                session.close();
            } catch (IOException e1) {
                // Ignore
            }
        }
    }

    /**
     * Process a received pong. This is a NO-OP.
     * 
     * @param pm
     */
    @OnMessage
    public void echoPongMessage(PongMessage pm) {
        // NO-OP
    }
}
