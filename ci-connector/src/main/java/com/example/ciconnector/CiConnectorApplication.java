package com.example.ciconnector;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Random;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CiConnectorApplication implements CommandLineRunner {

	String topic        = "test/espruino";
	//String content      = "Message from MqttPublishSample";
	int qos             = 2;
	String broker       = "tcp://ec2-34-243-3-198.eu-west-1.compute.amazonaws.com:1883";
	String clientId     = "JavaSample";
	MemoryPersistence persistence = new MemoryPersistence();

	public static void main(String[] args) {
		SpringApplication.run(CiConnectorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("hello world");

		try {
			MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			System.out.println("Connecting to broker: "+broker);
			sampleClient.connect(connOpts);
			System.out.println("Connected");
			while (true) {

				Random random = new Random();
				Boolean chance = random.nextBoolean();

				BuildStatus status = chance ? BuildStatus.SUCCESS : BuildStatus.FAIL;

				BuildInfo buildInfo = new BuildInfo(status);
				ObjectMapper objectMapper = new ObjectMapper();

				String content = objectMapper.writeValueAsString(buildInfo);


				System.out.println("Publishing message: "+content);
				MqttMessage message = new MqttMessage(content.getBytes());
				message.setQos(qos);
				sampleClient.publish(topic, message);
				System.out.println("Message published");

				Thread.sleep(1 * 1000 * 20);
			}

//			sampleClient.disconnect();
//			System.out.println("Disconnected");
//			System.exit(0);
		} catch(MqttException me) {
			System.out.println("reason "+me.getReasonCode());
			System.out.println("msg "+me.getMessage());
			System.out.println("loc "+me.getLocalizedMessage());
			System.out.println("cause "+me.getCause());
			System.out.println("excep "+me);
			me.printStackTrace();
		}
	}
}

