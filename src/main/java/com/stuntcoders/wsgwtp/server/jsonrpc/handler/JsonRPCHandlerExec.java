package com.stuntcoders.wsgwtp.server.jsonrpc.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.websocket.Session;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import com.stuntcoders.wsgwtp.server.jsonrpc.JsonRPCResponseBuilder;

public class JsonRPCHandlerExec extends JsonRPCHandler {

    public JsonRPCHandlerExec(JsonNode jsonNode, Session session) {
        super(jsonNode, session);
    }

    @Override
    public void run() {
        Process process = null;

        try {
            process = Runtime.getRuntime().exec(
                    getParams().get("command").getTextValue());
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                process.getInputStream()));

        try {
            String line = reader.readLine();
            while (line != null) {
                ObjectNode response = JsonRPCResponseBuilder.result(getId());

                response.put("result", line);

                try {
                    getSession().getBasicRemote().sendText(
                            JsonRPCResponseBuilder.mapper
                                    .writeValueAsString(response));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
