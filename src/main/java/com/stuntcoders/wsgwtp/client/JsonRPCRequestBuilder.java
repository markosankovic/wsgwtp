package com.stuntcoders.wsgwtp.client;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.stuntcoders.wsgwtp.shared.GUID;

public class JsonRPCRequestBuilder {

    final private static int ID_LENGTH = 40;

    public static JSONObject request(String method, JSONValue params) {

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("jsonrpc", new JSONString("2.0"));
        jsonObject.put("method", new JSONString(method));
        jsonObject.put("id", new JSONString(GUID.get(ID_LENGTH)));

        jsonObject.put("params", params);

        return jsonObject;
    }

    public static JSONObject interrupt(JSONValue id) {

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("jsonrpc", new JSONString("2.0"));
        jsonObject.put("method", new JSONString("interrupt"));
        jsonObject.put("id", new JSONString(GUID.get(ID_LENGTH)));

        JSONObject params = new JSONObject();
        params.put("id", id); // id of previous request to interrupt

        jsonObject.put("params", params);

        return jsonObject;
    }
}
