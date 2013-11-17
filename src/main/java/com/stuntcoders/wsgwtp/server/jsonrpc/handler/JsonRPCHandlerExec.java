package com.stuntcoders.wsgwtp.server.jsonrpc.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.websocket.Session;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import com.stuntcoders.wsgwtp.server.jsonrpc.JsonRPCResponseBuilder;

/**
 * Execute arbitrary shell command.
 * 
 * TODO Buffer several lines of output together before sending to a client.
 */
public class JsonRPCHandlerExec extends JsonRPCHandler {

    private static Logger logger = Logger.getLogger(JsonRPCHandlerExec.class);

    public JsonRPCHandlerExec(JsonNode jsonNode, Session session) {
        super(jsonNode, session);
    }

    @Override
    public void run() {
        Process process = null;

        try {
            process = Runtime.getRuntime().exec(
                    getParams().get("command").getTextValue());
            process.waitFor();

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    process.getInputStream()));

            String line = reader.readLine();
            while (line != null) {
                logger.info(line);
                ObjectNode response = JsonRPCResponseBuilder.result(getId());
                response.put("result", line);

                writeResponseAsStringToRemote(response);

                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
