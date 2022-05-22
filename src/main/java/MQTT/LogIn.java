package MQTT;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class LogIn implements MqttCallback {
	
	MqttClient client;

	public LogIn() {
	}

	public static void main(String[] args) {
	    new LogIn().doDemo("Andrei");
	}

	public void doDemo(String name) {
	    try {
	        client = new MqttClient("tcp://127.0.0.1:1883", name);
	        client.connect();
	        client.setCallback(this);
	        client.subscribe("foo");
	        MqttMessage message = new MqttMessage();
	        message.setPayload("A single message from my computer fff".getBytes());
	        client.publish("foo", message);
	    } catch (MqttException e) {
	        e.printStackTrace();
	    }
	}

	public void connectionLost(Throwable cause) {
	    // TODO Auto-generated method stub

	}

	public void messageArrived(String topic, MqttMessage message)
	        throws Exception {
	 System.out.println("DDSGDSGSDGSD " + message);
	 //adauga mesaj in fisierul care contine mesajele cu topicul respectiv
	 
	 //daca topic mesaj venit == topic mesaj selectat
	 //adauga in textbox
	}

	public void deliveryComplete(IMqttDeliveryToken token) {
	    // TODO Auto-generated method stub

	}

}