package MQTT;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class GraphicsWindows {
	public JFrame windows1 = new JFrame();
	public JFrame windows2 = new JFrame();
	ActionButton b = new ActionButton();
	String broker = "tcp://mqtt.eclipseprojects.io:1883";
	String topic = "MQTT Examples";
    String content = "continutul mesajului:)))";
    int qos = 2;
    String clientId = "name1 Client";
    MemoryPersistence persistence = new MemoryPersistence();
    MqttClient mqttClient;
    String txtTopic;
    static int nrOfTopic = 0;
    int i = 70;
    Preferences preferences = Preferences.userNodeForPackage(GraphicsWindows.class);
	GraphicsWindows() {
		initializeWindows1();
	}
	void initializeWindows1() {
		windows1.setTitle("Log In");
		windows1.setBounds(800,300,400,300);
		
		windows1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		windows1.getContentPane().setBackground((new Color(100,200,225)));
		
		final JTextField nickname = new JTextField(" Nickname: "); 
		nickname.setEditable(true);
		nickname.setBounds(60,30,70,20);
		windows1.getContentPane().add(nickname);
		nickname.setVisible(true); 
		nickname.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				nickname.setText("");
			}
		});
		
		final JTextField password = new JTextField(" Password: "); 
		
		password.setEditable(true);
		password.setBounds(150,30,70,20);
		windows1.getContentPane().add(password);
		password.setVisible(true);
		password.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e)
			{
				//password.setText("");
				password.setVisible(false);
				JPasswordField password2 = new JPasswordField();
				password.setEditable(true);
				password2.setBounds(150,30,70,20);
				windows1.getContentPane().add(password2);
				password2.setVisible(true);
			}
		});
		
		JButton logIn_button = new JButton("Log in"); 
		logIn_button.setBounds(250, 30, 80, 20);
		windows1.getContentPane().add(logIn_button);
		logIn_button.setVisible(true);
		logIn_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				windows1.setVisible(false);
				initializeWindows2();
			}
			
		});
		
		JTextField txt = new JTextField(" Text: "); 
		txt.setEditable(false);
		txt.setBounds(60,100,100,20);
		windows1.getContentPane().add(txt);
		txt.setVisible(false);
		
		JTextField txt2 = new JTextField(" Text: "); 
		txt.setEditable(true);
		txt.setBounds(60,30,250,20);
		windows1.getContentPane().add(txt);
		txt.setVisible(false);
		
	//	n.setVisible();
		windows1.setVisible(true);
	}
	void initializeWindows2() {
		windows2.setTitle("Main Application");
		windows2.setBounds(800,300,900,400);
		
		windows2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		windows2.getContentPane().setBackground((new Color(100,200,225)));
		
		JTextField n = new JTextField(); 
		n.setEditable(true);
		n.setBounds(20,20,200,200);
		//windows2.getContentPane().add(n);
		//n.setVisible(true); 
		//get add topic
		
		JButton connect_button = new JButton("Connect");
		connect_button.setBounds(100, 30, 120, 20);
		windows2.getContentPane().add(connect_button);
		connect_button.setVisible(true);
		connect_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//b.connectBroker(mqttClient, clientId, persistence, broker);
				try {
					mqttClient = new MqttClient(broker, clientId, persistence);
					MqttConnectOptions connOpts = new MqttConnectOptions();
					connOpts.setCleanSession(true);
					//connOpts.setUserName(username);
					//connOpts.setPassword(password.toCharArray());
					connOpts.setAutomaticReconnect(true);
					connOpts.setCleanSession(true);//Acesta va elimina mesajele netrimise dintr-o rulare anterioarã
					connOpts.setConnectionTimeout(10);//Timpul de expirare a conexiunii este setat la 10 sec
					System.out.println("Connecting to broker: " + broker);
					
					mqttClient.connect(connOpts);
					System.out.println("Connected");
					// afisare aceasta doar cu un client deja existent
					//preferences salveaza datele utilizatorului pentru a aminti ultimele valori din camp
					nrOfTopic = preferences.getInt("countTopic", 0);
					for (int k = 0; k <= nrOfTopic; k++) {
						JTextField t = new JTextField("", 100);
						t.setBounds(260, i, 150, 20);
						t.setText(preferences.get("pswTopic" + k, txtTopic));
						t.setVisible(true);
						windows2.getContentPane().add(t);
						i += 25;
					}
					
				} catch (MqttException e1) {
					e1.printStackTrace();  
				}
			}
		});
		JButton newTopic_button = new JButton("Add new topic "); 
		newTopic_button.setBounds(100, 70, 150, 20);
		windows2.getContentPane().add(newTopic_button);
		newTopic_button.setVisible(true);
		newTopic_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtTopic = JOptionPane.showInputDialog("Topic");//scriu mesajul
				if (txtTopic.length() > 0) {
					nrOfTopic++;
					preferences.put("pswTopic" + nrOfTopic, txtTopic);//il pun intr un cod + nrOfTopic
					JTextField t = new JTextField("", 100);
					t.setBounds(260, i, 150, 20);
					t.setText(preferences.get("pswTopic" + nrOfTopic, ""));
					t.setVisible(true);
					windows2.getContentPane().add(t);
					i += 25;
					System.out.println(nrOfTopic);
				}
				preferences.putInt("countTopic", nrOfTopic);
			}
			
		});
		JButton disconnect_button = new JButton("Disconnected");	
		disconnect_button.setBounds(230, 30 ,120, 20);
		windows2.getContentPane().add(disconnect_button);
		disconnect_button.setVisible(true);
		disconnect_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//b.disconnectedBroker(mqttClient);
				try {
					mqttClient.disconnect();
					System.out.println("Disconnected");
				} catch (MqttException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		JButton logOut_button = new JButton("Log out"); 
		logOut_button.setBounds(100, 230, 100, 20);
		windows2.getContentPane().add(logOut_button);
		logOut_button.setVisible(true);
		logOut_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				windows2.setVisible(false);
				initializeWindows1();
			}
			
		});
		
		
		JButton subscribedTopic_button = new JButton("Topic abonat. "); 
		subscribedTopic_button.setBounds(250, 110, 130, 20);
		//windows2.getContentPane().add(subscribedTopic_button);
		subscribedTopic_button.setVisible(true);
		
		
		JButton write_button = new JButton("Publish"); 
		write_button.setBounds(100, 200, 100, 20);
		windows2.getContentPane().add(write_button);
		write_button.setVisible(true);
		write_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Publishing message: " + content);
		        if (!mqttClient.isConnected()) {
		        	System.out.println("nu este conectat brokerul");
		        }
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
			
		});
		
		JButton read_button = new JButton("Read. "); 
		read_button.setBounds(480, 70, 100, 20);
		//windows2.getContentPane().add(read_button);
		read_button.setVisible(true);
		read_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
				
		
		JButton susbscribe_button = new JButton("Subscribe "); 
		susbscribe_button.setBounds(590, 70, 100, 20);
		//windows2.getContentPane().add(susbscribe_button);
		susbscribe_button.setVisible(true);
		susbscribe_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					mqttClient.subscribe("topic");
					System.out.println("subscribe topic");
				} catch (MqttException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		
		JButton unsusbscribe_button = new JButton("Unsubscribe. "); 
		unsusbscribe_button.setBounds(390, 110, 130, 20);
		//windows2.getContentPane().add(unsusbscribe_button);
		unsusbscribe_button.setVisible(true);
		
		JButton noName = new JButton("????"); 
		noName.setBounds(390, 110, 100, 20);
		windows2.getContentPane().add(noName);
		noName.setVisible(false);
		
		windows2.setVisible(true);
	}
}
