package MQTT;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
public class MainProject {
		
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				// TODO Auto-generated method stub
				try {
					GraphicsWindows windows = new GraphicsWindows();
                    windows.windows1.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		});
		
		try {
			MqttClient client = new MqttClient("tcp://127.0.0.1:1883", "Miron");
	        client.connect();
	        MqttMessage message = new MqttMessage();
	        message.setPayload("get hecked".getBytes());
	        client.publish("foo", message);
	        client.disconnect();
	    } catch (MqttException e) {
	        e.printStackTrace();
	    }
		
		try {
			//creearea unei instante sincrone IMqttClient
			String publisherId = UUID.randomUUID().toString();
			IMqttClient publisher = new MqttClient("tcp://127.0.0.1:1883", publisherId);
		} catch (MqttException e) {
	        e.printStackTrace();
	    }
		MqttConnectOptions options = new MqttConnectOptions();
		options.setAutomaticReconnect(true);
		options.setCleanSession(true);
		options.setConnectionTimeout(10);
		//publisher.connect(options);
	}

}