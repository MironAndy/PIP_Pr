package MQTT;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class ActionButton {
    
	void connectBroker(MqttClient mqttClient, String clientId,  MemoryPersistence persistence, String broker) {
		try {
			mqttClient = new MqttClient(broker, clientId, persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			//connOpts.setUserName(username);
			//connOpts.setPassword(password.toCharArray());*/
			connOpts.setAutomaticReconnect(true);
			connOpts.setCleanSession(true);//Acesta va elimina mesajele netrimise dintr-o rulare anterioarã
			connOpts.setConnectionTimeout(10);//Timpul de expirare a conexiunii este setat la 10 sec
			System.out.println("Connecting to broker: " + broker);
			
			mqttClient.connect(connOpts);
			System.out.println("Connected");
			
		} catch (MqttException e1) {
			e1.printStackTrace();
		}
	}
	void disconnectedBroker(MqttClient mqttClient) {
		try {
			mqttClient.disconnect();
			System.out.println("Disconnected");
		} catch (MqttException e1) {
			e1.printStackTrace();
		}
	}
	void publishMessage(MqttClient mqttClient, String topic, int qos, String content) {
		System.out.println("Publishing message: " + content);
        MqttMessage message = new MqttMessage(content.getBytes());
        message.setQos(qos);
        message.setRetained(true); //sets retained message 
        try {
        	mqttClient.publish(topic, message);
		} catch (MqttPersistenceException e1) {
			e1.printStackTrace();
		} catch (MqttException e1) {
			e1.printStackTrace();
		}
           System.out.println("Message published");
	}
}
