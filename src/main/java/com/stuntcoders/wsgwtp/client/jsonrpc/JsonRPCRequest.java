package com.stuntcoders.wsgwtp.client.jsonrpc;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.stuntcoders.wsgwtp.shared.GUID;

/**
 * JsonRPCRequest
 * 
 * @see http://www.jsonrpc.org/specification#request_object
 */
public class JsonRPCRequest {

    final private static int ID_LENGTH = 40;

    /**
     * Request object
     */
    private JSONObject jsonObject;

    /**
     * Params in JSON-RPC request
     */
    private JSONObject params;

    public JsonRPCRequest() {
        this(null);
    }

    /**
     * Specify method in constructor.
     * 
     * @param method
     */
    public JsonRPCRequest(String method) {
        jsonObject = new JSONObject();
        jsonObject.put("jsonrpc", new JSONString("2.0"));

        if (method != null) {
            jsonObject.put("method", new JSONString(method));
        }

        jsonObject.put("id", new JSONString(GUID.get(ID_LENGTH)));

        params = new JSONObject();
        jsonObject.put("params", params);
    }

    /**
     * Get request's id
     * 
     * @return
     */
    public String getId() {
        JSONString id = (JSONString) jsonObject.get("id");
        return id.stringValue();
    }

    /**
     * Add string parameter.
     * 
     * @param param
     * @param value
     */
    public void putParam(String param, String value) {
        putParam(param, new JSONString(value));
    }

    /**
     * Add JSONValue parameter.
     * 
     * @param param
     * @param value
     */
    public void putParam(String param, JSONValue value) {
        params.put(param, value);
    }

    /**
     * Return JSONObject as String.
     * 
     * @return
     */
    public String toJSON() {
        return jsonObject.toString();
    }
}
