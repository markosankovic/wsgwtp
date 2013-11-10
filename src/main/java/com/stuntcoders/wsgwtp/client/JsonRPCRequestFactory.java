package com.stuntcoders.wsgwtp.client;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.stuntcoders.wsgwtp.shared.GUID;

public class JsonRPCRequestFactory {

    public static JSONObject request(String method, JSONValue params) {

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("jsonrpc", new JSONString("2.0"));
        jsonObject.put("method", new JSONString(method));
        jsonObject.put("id", new JSONString(GUID.get(40)));

        jsonObject.put("params", params);

        return jsonObject;
    }
}
