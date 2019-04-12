package hello;

import org.springframework.http.converter.json.JsonbHttpMessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.web.socket.sockjs.frame.Jackson2SockJsMessageCodec;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

/**
 * Created by nick on 30/09/2015.
 */
public class Hello {

    private static Logger logger = Logger.getLogger(Hello.class.getName());

    private final static WebSocketHttpHeaders headers = new WebSocketHttpHeaders();

    public ListenableFuture<StompSession> connect() {

        Transport webSocketTransport = new WebSocketTransport(new StandardWebSocketClient());
        List<Transport> transports = Collections.singletonList(webSocketTransport);

        SockJsClient sockJsClient = new SockJsClient(transports);
        sockJsClient.setMessageCodec(new Jackson2SockJsMessageCodec());

        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);

        String url = "ws://{host}:{port}/hello";
        return stompClient.connect(url, headers, new MyHandler(), "localhost", 8080);
    }

    public void subscribeGreetings(StompSession stompSession) throws ExecutionException, InterruptedException {
        stompSession.subscribe("/topic/response", new StompFrameHandler() {

            public Type getPayloadType(StompHeaders stompHeaders) {
                return ByteArrayModel.class;
            }

            public void handleFrame(StompHeaders stompHeaders, Object o) {
                logger.info("Received greeting " + ((ByteArrayResponseModel) o).getContent());
            }
        });
    }

    public void sendHello(StompSession stompSession, byte[] bytes) {
    	ByteArrayModel model = new ByteArrayModel();
        ObjectMapper mapper = new ObjectMapper();
        try {
        	String jsonHello = "{ \"name\" : \"Nick\" }";
        	model.setData(bytes);
            //stompSession.send("/app/hello",mapper.writeValueAsBytes(model));
            stompSession.send("/app/hello",mapper.writeValueAsBytes(model));
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }

    private class MyHandler extends StompSessionHandlerAdapter {
        public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
            logger.info("Now connected");
        }
    }  
}
