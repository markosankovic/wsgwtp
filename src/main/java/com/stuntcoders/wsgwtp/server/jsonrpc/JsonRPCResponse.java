package com.stuntcoders.wsgwtp.server.jsonrpc;

/**
 * When a RPC call is made, the Server MUST reply with a Response, except for in
 * the case of Notifications. The Response is expressed as a single JSON Object.
 * 
 * @see http://www.jsonrpc.org/specification#response_object
 */
public abstract class JsonRPCResponse {

    /**
     * A String specifying the version of the JSON-RPC protocol. MUST be exactly
     * "2.0".
     */
    private String jsonrpc;

    /**
     * An identifier established by the Client that MUST contain a String,
     * Number, or NULL value if included. If it is not included it is assumed to
     * be a notification. The value SHOULD normally not be Null and Numbers
     * SHOULD NOT contain fractional parts.
     * 
     * The Server MUST reply with the same value in the Response object if
     * included. This member is used to correlate the context between the two
     * objects.
     */
    private Object id;

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }
}
