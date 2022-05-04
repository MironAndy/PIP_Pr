package MQTT;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

class Main extends JFrame{
	

	private static final long serialVersionUID = 1L;
	
	Main(){
		setTitle("Main Application");
		setBounds(800,300,900,400);
		
		System.out.println(" Istoric commit - Update ");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setBackground((new Color(100,200,225)));
		
		JTextField n = new JTextField(" Text Topic: "); 
		n.setEditable(true);
		n.setBounds(20,20,200,200);
		getContentPane().add(n);
		n.setVisible(true); //aici trebuie true
		
//		JTextField p = new JTextField(" Password: "); 
//		p.setEditable(true);
//		p.setBounds(150,30,70,20);
//		getContentPane().add(p);
//		p.setVisible(true); //si aici
		
		JButton logOut_button = new JButton("Log out"); 
		logOut_button.setBounds(250, 30, 80, 20);
		getContentPane().add(logOut_button);
		logOut_button.setVisible(true);
		
		JButton newTopic_button = new JButton("Topic nou. "); 
		newTopic_button.setBounds(250, 70, 110, 20);
		getContentPane().add(newTopic_button);
		newTopic_button.setVisible(true);
		
		JButton subscribedTopic_button = new JButton("Topic abonat. "); 
		subscribedTopic_button.setBounds(250, 110, 130, 20);
		getContentPane().add(subscribedTopic_button);
		subscribedTopic_button.setVisible(true);
		
		JButton write_button = new JButton("Write. "); 
		write_button.setBounds(370, 70, 100, 20);
		getContentPane().add(write_button);
		write_button.setVisible(true);
		
		JButton read_button = new JButton("Read. "); 
		read_button.setBounds(480, 70, 100, 20);
		getContentPane().add(read_button);
		read_button.setVisible(true);
		
		JButton susbscribe_button = new JButton("Subscribe. "); 
		susbscribe_button.setBounds(590, 70, 100, 20);
		getContentPane().add(susbscribe_button);
		susbscribe_button.setVisible(true);
		
		
		JButton unsusbscribe_button = new JButton("Unsubscribe. "); 
		unsusbscribe_button.setBounds(390, 110, 130, 20);
		getContentPane().add(unsusbscribe_button);
		unsusbscribe_button.setVisible(true);
		
		JButton noName = new JButton("????"); 
		noName.setBounds(390, 110, 100, 20);
		getContentPane().add(noName);
		noName.setVisible(false);
		
	//	n.setVisible();
		setVisible(true);
	}
	
	

}
	
public class MainProject {
		
	
	public static void main(String[] args) {
//		new Main();
		try {
			MqttClient client = new MqttClient("tcp://127.0.0.1:8081", "Miron");
	        client.connect();
	        MqttMessage message = new MqttMessage();
	        message.setPayload("get hecked".getBytes());
	        client.publish("foo", message);
	        client.disconnect();
	    } catch (MqttException e) {
	        e.printStackTrace();
	    }
	}

}