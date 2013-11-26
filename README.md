GWTP WebSocket JSON-RPC Communication Example {#title}
=============================================

This project demonstrates part of the solution StuntCoders have used during development of web-based IDE for one of their client. From within the IDE users can initiate build and run simulation process. Simulation can run infinitely and during the execution its output is pushed back to a client over WebSocket.

We had already tried [Atmosphere](https://github.com/Atmosphere/atmosphere), but found it rather complex and not really fitting our needs. You can find the working example of GWTP with Atmosphere [here](https://github.com/sankovicmarko/gwtp-atmosphere-demo).

During development we had to find solutions and answers to several questions:

- How to format messages exchanged between client and server over WebSocket protocol?
- How to support request/response messaging over WebSocket protocol?
- How to support dependency injection in Server Endpoint?
- How to interrupt long running processes?
- How to notify client that request has completed?
- How to dynamically create GWTP presenters and insert their content into the page?

Answer to these questions can be found in the source code of this project and the rest of this document.

The example {#example}
-----------

The example program allows its users to run arbitrary shell command and receive its output over WebSocket.

![GWTP WebSocket JSON-RPC Communication Example Screenshot][1]

In order to run the project, do the usual build:

```
$ mvn install
```
You **MUST** run this project on Tomcat that supports WebSocket protocol. We tested the example on Tomcat 7.0.47.

Technology {#technology}
----------

- [GWTP](https://github.com/ArcBees/GWTP) as Model-View-Presenter framework
- [Guice](https://code.google.com/p/google-guice/) for dependency injection
- [Jackson](http://jackson.codehaus.org/) for server side JSON serialization/deserialization
- [JSR 356, Java API for WebSocket](http://docs.oracle.com/javaee/7/tutorial/doc/websocket.htm)
- [JSON-RPC 2.0 Specification](http://www.jsonrpc.org/specification)

Dependecy injection in Server Endpoint {#dependency-injection}
--------------------------------------

The way to support @Inject in Server Endpoint Class is to instantiate it with injector. This can be accomplished by specify an [Endpoint Configuration Class](http://docs.oracle.com/javaee/7/tutorial/doc/websocket010.htm#BABJAIGH). In our case [JsonRPCWebsocketEndpointConfigurator](https://bitbucket.org/markosankovic/wsgwtp/src/228b5412609acf8d99dc1dc0b3ac78208610d7a4/src/main/java/com/stuntcoders/wsgwtp/server/JsonRPCWebsocketEndpointConfigurator.java?at=master) is responsible for instantiating [Server Endpoint Class](http://docs.oracle.com/javaee/7/api/javax/websocket/server/ServerEndpoint.html).

```
@Override
public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
    return (T) GuiceServletConfigFactory.getInjector().getInstance(endpointClass);
}
```

Instance of injector is retrieved with call to static method:

```
public class GuiceServletConfigFactory {
    private static final Injector injector = Guice.createInjector(new ServerModule(), new DispatchServletModule());

    public static Injector getInjector() {
        return injector;
    }
}
```

You must annotate your Server Endpoint Class with:

```
@ServerEndpoint(value = "/JsonRPCWebsocketEndpoint", configurator = JsonRPCWebsocketEndpointConfigurator.class, decoders = JsonRPCRequestDecoder.class, encoders = JsonRPCResponseEncoder.class)
```

Message Exchange and JSON-RPC 2.0 Specification {#json-rpc}
-----------------------------------------------

WebSocket is full-duplex communication channel and by design doesn't support request/response type of message exchange. You as a developer are responsible for building request/response type of messaging on top of WebSocket protocol.

We started of with custom request/response format but soon enough we found out about the amazing [JSON-RPC 2.0 Specification](http://www.jsonrpc.org/specification).

Request frame example:

```
{"jsonrpc":"2.0", "method":"exec", "id":"K468Hw7kZHsE6ncPLEm0OuiB0iGPetzE5jxmZnyz", "params":{"command":"ls"}}
```
Response frames example:

```
{"jsonrpc":"2.0","id":"K468Hw7kZHsE6ncPLEm0OuiB0iGPetzE5jxmZnyz","result":"Dropbox"}
{"jsonrpc":"2.0","id":"K468Hw7kZHsE6ncPLEm0OuiB0iGPetzE5jxmZnyz","result":"Downloads"}
```

Notice that server responds with the same frame identifier: ```K468Hw7kZHsE6ncPLEm0OuiB0iGPetzE5jxmZnyz```.

Handling JSON-RPC Request {#json-rpc-request}
-------------------------

Client sends serialized request through previously opened WebSocket connection:

```
request = new JsonRPCRequest("exec");
request.putParam("command", "ls -l");
websocket.send(request.toJSON());
```

Request frame received from client is automatically deserialized into [JsonRPCRequest](https://bitbucket.org/markosankovic/wsgwtp/src/228b5412609acf8d99dc1dc0b3ac78208610d7a4/src/main/java/com/stuntcoders/wsgwtp/server/jsonrpc/JsonRPCRequest.java?at=master) with [JsonRPCRequestDecoder](https://bitbucket.org/markosankovic/wsgwtp/src/228b5412609acf8d99dc1dc0b3ac78208610d7a4/src/main/java/com/stuntcoders/wsgwtp/server/jsonrpc/JsonRPCRequestDecoder.java?at=master). Specific [JsonRPCHandler](https://bitbucket.org/markosankovic/wsgwtp/src/228b5412609acf8d99dc1dc0b3ac78208610d7a4/src/main/java/com/stuntcoders/wsgwtp/server/jsonrpc/handler/JsonRPCHandler.java?at=master) is created depending on the JSON-RPC method received. Handling request is non-blocking. Every received request is executed in a separate thread.

```
@OnMessage
public void handleJsonRPCRequest(JsonRPCRequest jsonRPCRequest, Session session) {
    JsonRPCHandler jsonRPCHandler = jsonRPCHandlerFactory.create(jsonRPCRequest, session);
    jsonRPCHandler.putFuture(executorService.submit(jsonRPCHandler.getCleanFutureThread()));
}
```

Reference to list of executing threads for each session is stored in session's [user properties](https://javaee-spec.java.net/nonav/javadocs/javax/websocket/Session.html#getUserProperties()). We wrap JsonRPCHandler runnable with another thread that's responsible for cleaning up both completed and interrupted threads.

```
public Thread getCleanFutureThread() {
   return new Thread() {
       public void run() {
           try {
               JsonRPCHandler.this.run();
            } finally {
                removeFuture(); // Remove done future from session
            }
        }
    };
}
```

Handling JSON-RPC Response {#json-rpc-response}
--------------------------

Response frame received from server is deserialized into JsonRPCResponse.

```
JSONObject jsonObject = (JSONObject) JSONParser.parseLenient(msg);
JsonRPCResponse jsonRPCResponse = new JsonRPCResponse(jsonObject);
```

New JsonRPCResponseEvent is fired on EventBus carrying deserialized JsonRPCResponse instance:

```
eventBus.fireEvent(new JsonRPCResponseEvent(jsonRPCResponse));
```

Presenters will usually check if the received JsonRPCResponse matches the request they previously sent:

```
@Override
public void onJsonRPCResponse(JsonRPCResponseEvent event) {
    if (event.getJsonRPCResponse().getId().equals(request.getId())) {
        ...
    }
}
```

------------------------------------------------------

> Provided by [StuntCoders](http://stuntcoders.com//).

  [1]: https://lh4.googleusercontent.com/-4y5dt8qXLdQ/UpMfAFM0wCI/AAAAAAAACrU/FDvvAgqVJlQ/s144/Screenshot%252520from%2525202013-11-25%25252010%25253A55%25253A06.png