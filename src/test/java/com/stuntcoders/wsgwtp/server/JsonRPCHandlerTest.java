package com.stuntcoders.wsgwtp.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;

import org.codehaus.jackson.node.ObjectNode;
import org.jukito.JukitoRunner;
import org.jukito.TestModule;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.inject.Inject;
import com.stuntcoders.wsgwtp.server.jsonrpc.JsonRPCResponseBuilder;
import com.stuntcoders.wsgwtp.server.jsonrpc.handler.JsonRPCHandler;
import com.stuntcoders.wsgwtp.server.jsonrpc.handler.JsonRPCHandlerFactory;

@RunWith(JukitoRunner.class)
public class JsonRPCHandlerTest {

    public static class Module extends TestModule {
        @Override
        protected void configureTest() {
        }
    }

    @Inject
    JsonRPCHandlerFactory jsonRPCHandlerFactory;

    Session sessionMock;
    Map<String, Future<?>> futures;

    private ExecutorService executorService;

    @Before
    public void setupExecutorService() throws IOException {

        // Executor service
        this.executorService = Executors.newCachedThreadPool();

        // Mock websocket Session
        sessionMock = mock(Session.class);

        // Mock futures
        this.futures = new HashMap<String, Future<?>>();
        Map<String, Object> userProperties = new HashMap<String, Object>();
        userProperties.put("futures", futures);

        when(sessionMock.getUserProperties()).thenReturn(userProperties);

        // Mock RemoteEndpoint.Basic
        RemoteEndpoint.Basic basicRemoteMock = mock(RemoteEndpoint.Basic.class);
        when(sessionMock.getBasicRemote()).thenReturn(basicRemoteMock);

        // Print text instead of sending to a remote
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                String text = invocation.getArguments()[0].toString();
                System.out.println(text);
                return null;
            }
        }).when(basicRemoteMock).sendText(anyString());
    }

    @After
    public void shutdownExecutorService() {
        futures.clear();
        this.executorService.shutdownNow();
    }

    @Test
    public void testClearFutureAfterExecution() throws InterruptedException {

        ObjectNode objectNodeExec = createRequestObjectNode();
        objectNodeExec.put("method", "exec");
        ObjectNode params = (ObjectNode) objectNodeExec.get("params");
        params.put("command", "pwd");

        JsonRPCHandler jsonRPCHandler = jsonRPCHandlerFactory.create(
                objectNodeExec, sessionMock);

        jsonRPCHandler.putFuture(executorService.submit(jsonRPCHandler
                .getCleanFutureThread()));

        Thread.sleep(1000);

        assertEquals(0, futures.size());
    }

    @Test
    public void testInterruptFuture() throws InterruptedException {

        ObjectNode objectNodeExec = createRequestObjectNode();
        objectNodeExec.put("method", "exec");
        ObjectNode params = (ObjectNode) objectNodeExec.get("params");
        params.put("command", "sleep 60");

        JsonRPCHandler jsonRPCHandlerExec = jsonRPCHandlerFactory.create(
                objectNodeExec, sessionMock);

        Future<?> execFuture = executorService.submit(jsonRPCHandlerExec
                .getCleanFutureThread());
        jsonRPCHandlerExec.putFuture(execFuture);

        Thread.sleep(1000);

        ObjectNode objectNodeInterrupt = createRequestObjectNode();
        objectNodeInterrupt.put("method", "interrupt");
        ObjectNode interruptParams = (ObjectNode) objectNodeInterrupt
                .get("params");
        interruptParams.put("id", objectNodeExec.get("id"));

        JsonRPCHandler jsonRPCHandlerInterrupt = jsonRPCHandlerFactory.create(
                objectNodeInterrupt, sessionMock);

        jsonRPCHandlerInterrupt.putFuture(executorService
                .submit(jsonRPCHandlerInterrupt.getCleanFutureThread()));

        Thread.sleep(1000);

        assertTrue(execFuture.isDone());
        assertEquals(0, futures.size());
    }

    private ObjectNode createRequestObjectNode() {
        ObjectNode objectNode = JsonRPCResponseBuilder.mapper
                .createObjectNode();

        objectNode.put("jsonrpc", "2.0");
        objectNode.put("id", UUID.randomUUID().toString());

        ObjectNode params = JsonRPCResponseBuilder.mapper.createObjectNode();
        objectNode.put("params", params);

        return objectNode;
    }
}
