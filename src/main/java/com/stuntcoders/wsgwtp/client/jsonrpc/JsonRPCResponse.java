package com.stuntcoders.wsgwtp.client.jsonrpc;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class JsonRPCResponse {

    /**
     * Response object
     */
    private JSONObject jsonObject;

    /**
     * JsonRPCResponse
     * 
     * @param jsonObject
     */
    public JsonRPCResponse(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    /**
     * Get result as JSONValue
     * 
     * @return
     */
    public JSONValue getResult() {
        return jsonObject.get("result");
    }

    /**
     * Get result as String
     * 
     * @return
     */
    public String getResultAsString() {
        JSONString result = (JSONString) jsonObject.get("result");
        return result.stringValue();
    }

    /**
     * Get id. It is the same as the value of the id member in the Request
     * Object.
     * 
     * @return
     */
    public String getId() {
        JSONString id = (JSONString) jsonObject.get("id");
        return id.stringValue();
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
