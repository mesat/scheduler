/*
 * Copyright 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hello;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

@Component
@Configuration
@ComponentScan
public class ScheduledTasks {

	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	private WebSocketStompClient stompClient = null;
	private StompSessionHandler sessionHandler = null;
	private static final String WEBSOCKET_URI = "ws://localhost:8080/websocket";
	private static final String WEBSOCKET_TOPIC = "/topic";
	

	private List<Mat> imageList = new ArrayList<>();
	private List<Mat> mv = new ArrayList<>();

	Hello helloClient = null;
	static ListenableFuture<StompSession> f = null;
	static StompSession stompSession = null;

	public ScheduledTasks() {

		try {
			Initialize();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void Initialize() throws Exception {
		if (helloClient == null) {
			/*
			 * WebSocketContainer container = ContainerProvider.getWebSocketContainer();
			 * client = new StandardWebSocketClient(container); stompClient = new
			 * WebSocketStompClient(client); stompClient.setMessageConverter(new
			 * MappingJackson2MessageConverter());
			 */

			/*
			 * sessionHandler = new CustomStompSessionHandler();
			 * //stompClient.connect("localhost:8080", sessionHandler); //new
			 * Scanner(System.in).nextLine(); // Don't close immediately.
			 * 
			 * List<Transport> transports = new ArrayList<Transport>(2); transports.add(new
			 * WebSocketTransport(new StandardWebSocketClient())); //transports.add(new
			 * RestTemplateXhrTransport()); SockJsClient sockJsClient = new
			 * SockJsClient(transports); stompClient = new
			 * WebSocketStompClient(sockJsClient); stompClient.setMessageConverter(new
			 * MappingJackson2MessageConverter()); //DefaultStompFrameHandler stompHandler =
			 * new DefaultStompFrameHandler(); try { stompClient.connect(WEBSOCKET_URI,
			 * sessionHandler); stompClient.start(); //new Scanner(System.in).nextLine();
			 * 
			 * } finally { }
			 */

			helloClient = new Hello();

			f = helloClient.connect();
			stompSession = f.get(1, TimeUnit.DAYS);

			log.info("Subscribing to greeting topic using session " + stompSession);
			helloClient.subscribeGreetings(stompSession);

			log.info("Sending hello message" + stompSession);
			//helloClient.sendHello(stompSession);

			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

			String imgPath = "/home/mesat/Downloads/maxresdefault.jpg";
			Mat image = Imgcodecs.imread(imgPath, Imgcodecs.IMREAD_UNCHANGED);
			image.convertTo(image, CvType.CV_8UC3);
			// Imgproc.cvtColor(image, image, Imgproc.COLOR_RGB2Lab);
			Core.split(image, mv);

		}

		//
	}

	 @Scheduled(fixedRate = 50,initialDelay=5000)
	public void reportCurrentTime() throws Exception {
		if (!stompSession.isConnected()) {
			helloClient = null;
		}
		Initialize();
		byte[] data = new byte[mv.get(0).rows()];
		mv.get(0).get(0, 0, data);
		
		helloClient.sendHello(stompSession,data);
		/*
		 * StompSession session = ((CustomStompSessionHandler)
		 * sessionHandler).getSession(); session.send("/app/server-side", new
		 * ByteArrayModel());
		 */
		log.info("The time is now {}", dateFormat.format(new Date()));
	}

	// @Scheduled(fixedRate = 50,initialDelay=5000)
	public void reportCurrentTime2() throws Exception {
		if (!stompSession.isConnected()) {
			helloClient = null;
		}
		Initialize();
		byte[] data = new byte[mv.get(1).rows()];
		mv.get(1).get(0, 0, data);
		
		helloClient.sendHello(stompSession,data);
		/*
		 * StompSession session = ((CustomStompSessionHandler)
		 * sessionHandler).getSession(); session.send("/app/server-side", new
		 * ByteArrayModel());
		 */
		log.info("The time is now {}", dateFormat.format(new Date()));
	}

	// @Scheduled(fixedRate = 50,initialDelay=5000)
	public void reportCurrentTime3() throws Exception {
		if (!stompSession.isConnected()) {
			helloClient = null;
		}
		Initialize();
		byte[] data = new byte[mv.get(2).rows()];
		mv.get(2).get(0, 0, data);
		
		helloClient.sendHello(stompSession,data);
		/*
		 * StompSession session = ((CustomStompSessionHandler)
		 * sessionHandler).getSession(); session.send("/app/server-side", new
		 * ByteArrayModel());
		 */
		log.info("The time is now {}", dateFormat.format(new Date()));
	}
}
