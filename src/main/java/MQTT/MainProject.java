package MQTT;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
public class MainProject {
		
	public static void main(String[] args) throws Exception {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GraphicsWindows windows = new GraphicsWindows();
                    windows.windows1.setVisible(true);           
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		});
		
		/*
		try {
			//creearea unei instante sincrone IMqttClient
			String publisherId = UUID.randomUUID().toString();
			IMqttClient publisher = new MqttClient("tcp://127.0.0.1:1883", publisherId);
			//pt a stabili conexiunea la server 
			MqttConnectOptions options = new MqttConnectOptions();
			options.setAutomaticReconnect(true);
			options.setCleanSession(true);//Acesta va elimina mesajele netrimise dintr-o rulare anterioarã
			options.setConnectionTimeout(10);//Timpul de expirare a conexiunii este setat la 10 sec
			//conectare interfata MqttClient la server
			publisher.connect(options);
			
			String broker = "tcp://localhost:1883"; 
			String topicName = "test/topic";
			int qos = 1;

			MqttClient mqttClient = new MqttClient(broker,String.valueOf(System.nanoTime()));
			 //Mqtt ConnectOptions is used to set the additional features to mqtt message

			MqttConnectOptions connOpts = new MqttConnectOptions();

			connOpts.setCleanSession(true); //no persistent session 
			connOpts.setKeepAliveInterval(1000);


			MqttMessage message = new MqttMessage("ceva".getBytes());
			 
			message.setQos(qos);     //sets qos level 1
			message.setRetained(true); //sets retained message 

			MqttTopic topic2 = mqttClient.getTopic(topicName);

		    mqttClient.connect(connOpts); //connects the broker with connect options
			topic2.publish(message);    // publishes the message to the topic(test/topic)
			  //We're using eclipse paho library  so we've to go with MqttCallback 
			MqttClient client = new MqttClient("tcp://localhost:1883","clientid");
			//client.setCallback(this);
			MqttConnectOptions mqOptions=new MqttConnectOptions();
			mqOptions.setCleanSession(true);
			client.connect(mqOptions);      //connecting to broker 
			client.subscribe("test/topic"); //subscribing to the topic name  test/topic

			//Override methods from MqttCallback interface
			/*   //@Override
		    public void messageArrived(String topic, MqttMessage message) throws Exception {
			     System.out.println("message is : "+message);
		    }
		} catch (MqttException e) {
	        e.printStackTrace();
	    }*/ 
	}

}