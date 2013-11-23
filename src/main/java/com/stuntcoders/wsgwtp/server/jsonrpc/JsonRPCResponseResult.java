package com.stuntcoders.wsgwtp.server.jsonrpc;

/**
 * Result JSON-RPC response object
 * 
 * @see http://www.jsonrpc.org/specification#response_object
 */
public class JsonRPCResponseResult extends JsonRPCResponse {

    /**
     * This member is REQUIRED on success.
     * 
     * This member MUST NOT exist if there was an error invoking the method.
     * 
     * The value of this member is determined by the method invoked on the
     * Server.
     */
    private Object result;

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
