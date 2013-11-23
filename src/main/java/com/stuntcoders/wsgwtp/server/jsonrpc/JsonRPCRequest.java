package com.stuntcoders.wsgwtp.server.jsonrpc;

import java.util.Map;

/**
 * A RPC call is represented by sending a Request object to a Server.
 * 
 * @see http://www.jsonrpc.org/specification#request_object
 */
public class JsonRPCRequest {

    /**
     * A String specifying the version of the JSON-RPC protocol. MUST be exactly
     * "2.0".
     */
    private String jsonrpc;

    /**
     * A String containing the name of the method to be invoked. Method names
     * that begin with the word rpc followed by a period character (U+002E or
     * ASCII 46) are reserved for rpc-internal methods and extensions and MUST
     * NOT be used for anything else.
     */
    private String method;

    /**
     * A Structured value that holds the parameter values to be used during the
     * invocation of the method. This member MAY be omitted.
     */
    private Map<String, Object> params;

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

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }
}
