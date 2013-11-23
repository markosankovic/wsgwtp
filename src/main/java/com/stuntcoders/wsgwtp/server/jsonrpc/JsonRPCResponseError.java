package com.stuntcoders.wsgwtp.server.jsonrpc;

/**
 * Error JSON-RPC response object
 * 
 * @see http://www.jsonrpc.org/specification#response_object
 */
public class JsonRPCResponseError extends JsonRPCResponse {

    /**
     * This member is REQUIRED on error.
     * 
     * This member MUST NOT exist if there was no error triggered during
     * invocation.
     * 
     * The value for this member MUST be an Object as defined in section 5.1.
     */
    private Object error;

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }
}
