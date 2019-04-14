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

package esp32.mock;

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


	private List<Mat> mv = new ArrayList<>();

	ClientHelper client = null;
	static ListenableFuture<StompSession> f = null;
	static StompSession stompSession = null;

	ClientHelper helloClient1 = null;
	static ListenableFuture<StompSession> f1 = null;
	static StompSession stompSession1 = null;

	WebSocketClient helloClient2 = null;
	static ListenableFuture<StompSession> f2 = null;
	static StompSession stompSession2 = null;

	public ScheduledTasks() {

		try {
			Initialize();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void Initialize() throws Exception {
		if (client == null) {
			

			client = new ClientHelper();

			f = client.connect();
			stompSession = f.get(1, TimeUnit.DAYS);

			log.info("Subscribing to greeting topic using session " + stompSession);
			client.subscribeResponse(stompSession);

			log.info("Sending esp32.mock message" + stompSession);

			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

			String imgPath = "/home/mesat/Downloads/maxresdefault.png";
			Mat image = Imgcodecs.imread(imgPath, Imgcodecs.IMREAD_UNCHANGED);
			image.convertTo(image, CvType.CV_8UC3);
			Core.split(image, mv);

		}

	}

	@Scheduled(fixedRate = 100, initialDelay = 500)
	public void reportCurrentTime() throws Exception {
		if (!stompSession.isConnected()) {
			client = null;
		}
		Initialize();
		byte[] data = new byte[(int) (mv.get(0).total() * mv.get(0).elemSize())];
		mv.get(0).get(0, 0, data);
    	ByteArrayModel model = new ByteArrayModel(data);
    	model.setWidth(mv.get(0).width());
    	model.setHeight(mv.get(0).height());
		client.sendData(stompSession, model);
		
		log.info("Time: {}", dateFormat.format(new Date()));
	}

	@Scheduled(fixedRate = 100, initialDelay = 500)
	public void reportCurrentTime2() throws Exception {
		if (!stompSession.isConnected()) {
			client = null;
		}
		Initialize();
		byte[] data2 = new byte[(int) (mv.get(1).total() * mv.get(1).elemSize())];
		mv.get(1).get(0, 0, data2);
    	ByteArrayModel model = new ByteArrayModel(data2);
    	model.setWidth(mv.get(1).width());
    	model.setHeight(mv.get(1).height());
		client.sendData(stompSession, model);
		
		log.info("Time: {}", dateFormat.format(new Date()));
	}

	@Scheduled(fixedRate = 100, initialDelay = 500)
	public void reportCurrentTime3() throws Exception {
		if (!stompSession.isConnected()) {
			client = null;
		}
		Initialize();
		byte[] data3 = new byte[(int) (mv.get(2).total() * mv.get(2).elemSize())];
		mv.get(2).get(0, 0, data3);
    	ByteArrayModel model = new ByteArrayModel(data3);
    	model.setWidth(mv.get(1).width());
    	model.setHeight(mv.get(1).height());
		client.sendData(stompSession, model);
		
		log.info("Time: {}", dateFormat.format(new Date()));
	}
}
