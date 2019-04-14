package esp32.mock;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.JsonbHttpMessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.adapter.standard.StandardToWebSocketExtensionAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
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

import javax.websocket.OnMessage;


public class ClientHelper {

    private static Logger logger = Logger.getLogger(ClientHelper.class.getName());
    private final static WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
    public ListenableFuture<StompSession> connect() {
    	WebSocketTransportRegistration reg = new WebSocketTransportRegistration();

        Transport webSocketTransport = new WebSocketTransport(new StandardWebSocketClient());
        List<Transport> transports = Collections.singletonList(webSocketTransport);

        SockJsClient sockJsClient = new SockJsClient(transports);
        sockJsClient.setMessageCodec(new Jackson2SockJsMessageCodec());

        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);

        String url = "ws://{host}:{port}/rgbdata";
        return stompClient.connect(url, headers, new CustomSSHandler(), "localhost", 8080);
    }

    public void subscribeResponse(StompSession stompSession) throws ExecutionException, InterruptedException {
        stompSession.subscribe("/topic/response", new StompFrameHandler() {

            public Type getPayloadType(StompHeaders stompHeaders) {
                return ByteArrayModel.class;
            }

            public void handleFrame(StompHeaders stompHeaders, Object o) {
                logger.info("Received greeting " + ((ByteArrayResponseModel) o).getContent());
            }
        });
    }
    public void sendData(StompSession stompSession, byte[] bytes) {
    	ByteArrayModel model = new ByteArrayModel(bytes);
        ObjectMapper mapper = new ObjectMapper();
        try {
        	model.setData(bytes);
    		StompHeaders headers = new StompHeaders();
    		headers.setDestination("/app/rgbdata");
            stompSession.send(headers,mapper.writeValueAsBytes(model));
			
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }

    private class CustomSSHandler extends StompSessionHandlerAdapter {
    	
        public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
            logger.info("Now connected");
        }
    }  
}
