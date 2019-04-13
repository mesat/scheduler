package hello;

import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

public class CustomStompSessionHandler implements StompSessionHandler{
	private static final Logger log = LoggerFactory.getLogger(CustomStompSessionHandler.class);
	private static final String WEBSOCKET_TOPIC = "/topic";

	private StompSession session = null;
	@Override
	public Type getPayloadType(StompHeaders headers) {
		return ByteArrayModel.class;
	}

	@Override
	public void handleFrame(StompHeaders headers, Object payload) {
		ByteArrayResponseModel msg = (ByteArrayResponseModel) payload;
		log.info("Received : " + msg.getContent()+ " from : " + msg.getContent().length());
		
	}

	@Override
	public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
		this.setSession(session);
		session.subscribe(WEBSOCKET_TOPIC, this);
	    //session.send("/app/server-side", "");		
	}

	@Override
	public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload,
			Throwable exception) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleTransportError(StompSession session, Throwable exception) {
		// TODO Auto-generated method stub
		
	}

	public StompSession getSession() {
		return session;
	}

	public void setSession(StompSession session) {
		this.session = session;
	}

}
