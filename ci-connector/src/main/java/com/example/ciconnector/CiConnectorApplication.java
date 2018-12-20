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

	private MemoryPersistence persistence = new MemoryPersistence();

	public static void main(String[] args) {
		SpringApplication.run(CiConnectorApplication.class, args);
	}

	public MqttMessage buildMessage(BuildInfo buildInfo) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();

		String content = objectMapper.writeValueAsString(buildInfo);
		MqttMessage message = new MqttMessage(content.getBytes());
		int qos = 2;
		message.setQos(qos);
		return message;
	}

	@Override
	public void run(String... args) throws Exception {

		try {
			String broker = "tcp://ec2-34-243-3-198.eu-west-1.compute.amazonaws.com:1883";
			String clientId = "JavaSample";
			MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			System.out.println("Connecting to broker: " + broker);
			sampleClient.connect(connOpts);
			System.out.println("Connected");

			while (true) {
				Random random = new Random();

				int delay = 1 * 1000 * 10;

				MqttMessage message = buildMessage(new BuildInfo(BuildStatus.BUILDING));
				System.out.println("Publishing message: "+message.toString());
				sampleClient.publish("test/espruino", message);
				Thread.sleep(delay);

				message = buildMessage(new BuildInfo(BuildStatus.FAIL));
				System.out.println("Publishing message: "+message.toString());
				sampleClient.publish("test/espruino", message);
				Thread.sleep(delay);

				message = buildMessage(new BuildInfo(BuildStatus.BUILDING));
				System.out.println("Publishing message: "+message.toString());
				sampleClient.publish("test/espruino", message);
				Thread.sleep(delay);

				message = buildMessage(new BuildInfo(BuildStatus.SUCCESS));
				System.out.println("Publishing message: "+message.toString());
				sampleClient.publish("test/espruino", message);
				Thread.sleep(delay);
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

	 public <T> T randomValue(T[] values) {
		Random mRandom = new Random();
		return values[mRandom.nextInt(values.length)];
	}
}

