package MQTT;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class GraphicsWindows {
	
	public JFrame windows1 = new JFrame("Log In");
	public JFrame windows2 = new JFrame("Main Application");
	ActionButton b = new ActionButton();
	String broker = "tcp://mqtt.eclipseprojects.io:1883";
    int qos = 0;
    //String clientId = "name1 Client";
    MemoryPersistence persistence = new MemoryPersistence();
    MqttClient mqttClient;
    String txtTopic;
    static int nrOfTopic = 0;
    int i = 70;
    static int nrOfRows = 0;
    boolean isConnectedMqtt = false;
    MqttMessage message;
    Preferences preferences = Preferences.userNodeForPackage(GraphicsWindows.class);
    JTextField[] t = new JTextField[100];
    JTextField nickname;
    JPasswordField password2;
	GraphicsWindows() {
		initializeWindows1();
	}
	void initializeWindows1() {
		windows1.setBounds(800,300,400,300);
		windows1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		windows1.getContentPane().setBackground((new Color(100,200,225)));
		
		nickname = new JTextField(" Nickname: "); 
		nickname.setEditable(true);
		nickname.setBounds(60,30,70,20);
		windows1.getContentPane().add(nickname);
		nickname.setVisible(true); 
		nickname.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				nickname.setText(" ");
			}
		});
		
		final JTextField password = new JTextField(" Password: "); 
		password.setEditable(true);
		password.setBounds(150,30,70,20);
		windows1.getContentPane().add(password);
		password.setVisible(true);
		password.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				password.setVisible(false);
				password2 = new JPasswordField();
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
		
		windows1.setVisible(true);
	}
	void initializeWindows2() {
		try {
			mqttClient = new MqttClient(broker, nickname.getText(), persistence);
			final MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			if (nickname.getText() != null && password2.getPassword() != null) {
				connOpts.setUserName(nickname.getText());
				connOpts.setPassword(password2.getPassword());//toCharArray());
			}
			connOpts.setAutomaticReconnect(true);//Biblioteca va incerca automat sa se reconnecteze la server în cazul unei defectiuni în retea
			connOpts.setCleanSession(true);//Acesta va elimina mesajele netrimise dintr-o rulare anterioara
			connOpts.setConnectionTimeout(100);//Timpul de expirare a conexiunii este setat la 100 secunde
			System.out.println("Connecting to broker: " + broker);
			
			windows2.setBounds(800,300,900,400);
			windows2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			windows2.getContentPane().setBackground((new Color(100,200,225)));
		
			JButton connect_button = new JButton("Connect");
			connect_button.setBounds(100, 30, 120, 20);
			windows2.getContentPane().add(connect_button);
			connect_button.setVisible(true);
			connect_button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//b.connectBroker(mqttClient, clientId, persistence, broker);
					if (!isConnectedMqtt) {
						try {
							mqttClient.connect(connOpts);
							System.out.println("mqttClient Connected");
						} catch (MqttSecurityException e1) {
							e1.printStackTrace();
						} catch (MqttException e1) {
							e1.printStackTrace();
						}
						isConnectedMqtt = true;
						// afisare aceasta doar cu un client deja existent
						nrOfTopic = preferences.getInt("countTopic", 0);
						System.out.println("la apasarea butonului connect sunt " + nrOfTopic + " topicuri din conectarea anterioara.");
						for (int k = 0; k <= nrOfTopic; k++) {
							t[k] = new JTextField("", 100);
							t[k].setBounds(260, i, 150, 20);
							t[k].setText(preferences.get("pswTopic" + k, ""));
							t[k].setVisible(true);
							t[k].setEditable(false);
							i += 25;
							windows2.getContentPane().add(t[k]);
						}
					}
				}
			});
			
			JButton newTopic_button = new JButton("Add new topic "); 
			newTopic_button.setBounds(100, 70, 150, 20);
			windows2.getContentPane().add(newTopic_button);
			newTopic_button.setVisible(true);
			newTopic_button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (mqttClient.isConnected()) {
						System.out.println("clientul este conectat la apasarea butonului 'add new topic'");
						System.out.println("la apasarea butonului 'add new topic' sunt " + nrOfTopic + " topicuri");
						txtTopic = JOptionPane.showInputDialog("Topic");
						if (txtTopic.length() > 0) {
							nrOfTopic++;
							preferences.put("pswTopic" + nrOfTopic, txtTopic);
							t[nrOfTopic] = new JTextField();
							t[nrOfTopic].setBounds(260, i, 150, 20);
							t[nrOfTopic].setText(preferences.get("pswTopic" + nrOfTopic, ""));
							t[nrOfTopic].setEditable(false);
							windows2.getContentPane().add(t[nrOfTopic]);
							i += 25;
							System.out.println("dupa adaugarea unui topic sunt: " + nrOfTopic + " topicuri");
							//nrOfTopic++;
							preferences.putInt("countTopic", nrOfTopic);
						}
					}
				}
			});
			JLabel deleteTopic = new JLabel("Write what topic want to delete:");
			deleteTopic.setBounds(420, 70, 200, 20);
			windows2.getContentPane().add(deleteTopic);
			
			final JTextField fieldTopicDelete = new JTextField();
			fieldTopicDelete.setBounds(620, 70, 25, 20);
			windows2.getContentPane().add(fieldTopicDelete);
			
			JButton okDelete = new JButton("ok");
			okDelete.setBounds(650, 70, 50, 20);
			windows2.getContentPane().add(okDelete);
			okDelete.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int indexTopicToDelete = Integer.parseInt(fieldTopicDelete.getText());
					if (mqttClient.isConnected()) {
						System.out.println("dupa apasarea butonului ok sunt: " + nrOfTopic + " topicuri");
						if (nrOfTopic >= 0 && indexTopicToDelete > 0 && indexTopicToDelete-1 <= nrOfTopic && t[indexTopicToDelete-1].isVisible()) {
							t[indexTopicToDelete-1].setVisible(false);
							for(int l = indexTopicToDelete-1; l < nrOfTopic - 1; l++) {
								preferences.put("pswTopic" + l, t[l+1].getText());
							}
							nrOfTopic--;
							if(nrOfTopic < 0) {
								nrOfTopic = 0;
							}
							preferences.putInt("countTopic", nrOfTopic);
							System.out.println("dupa apasarea butonului ok si elimare sunt: " + nrOfTopic + " topicuri");
						}
						else {
							System.out.println("Nu aveti topic-uri de eliminat, iar nr de topicuri este: " + nrOfTopic);
						}
					}
				}
			});
		JLabel publishTopic = new JLabel("If you want to publish choose a topic:");
		publishTopic.setBounds(100, 180, 220, 20);
		windows2.getContentPane().add(publishTopic);
		
		final JTextField fieldPublish = new JTextField();
		fieldPublish.setBounds(100, 200, 130, 20);
		windows2.getContentPane().add(fieldPublish);
		
		JLabel publishMessage = new JLabel("Write message: ");
		publishMessage.setBounds(350, 180, 150, 20);
		windows2.getContentPane().add(publishMessage);
		
		final JTextField fieldMessage = new JTextField();
		fieldMessage.setBounds(350, 200, 130, 20);
		windows2.getContentPane().add(fieldMessage);
		
		final JTextField textTopicAndMessages = new JTextField();
        textTopicAndMessages.setBounds(620, 200, 180, 20);
        textTopicAndMessages.setEditable(false);
		windows2.getContentPane().add(textTopicAndMessages);
		
		JButton publish_button = new JButton("Publish"); 
		publish_button.setBounds(500, 200, 100, 20);
		windows2.getContentPane().add(publish_button);
		publish_button.setVisible(true);
		publish_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("nr topic la apasarea btn publish sunt : " + nrOfTopic + " topicuri ");
				String[] mesAndTop = new String[nrOfTopic];
				//if (mqttClient.isConnected()) {
					if (nrOfTopic > 0) {
						for (int t = 0; t < nrOfTopic; t++) { // parcurgeti topicurile
							mesAndTop[t] = preferences.get("pswTopic" + t, "");
							System.out.println(mesAndTop[t]);
							System.out.println("topic nou: " + fieldPublish.getText() + " si topic dinainte: " +  mesAndTop[t]);
							if (fieldPublish.getText().equals(mesAndTop[t])) {
								message = new MqttMessage(fieldMessage.getText().getBytes());
								message.setQos(qos);
								message.setRetained(true); //sets retained message 
								System.out.println("messages is " + message);
								try {
									//message.setPayload("fieldMessage.getText()".getBytes());
									mqttClient.publish(fieldMessage.getText(), message);
									System.out.println("Message published");
								} catch (MqttPersistenceException e1) {
									e1.printStackTrace();
								} catch (MqttException e1) {
									e1.printStackTrace();
								} 
								textTopicAndMessages.setText("topic: '" + fieldPublish.getText() + " ' ," + " message: '" + message + " '");
							//introducerea tuturor topicurilor intr un string
							/*String[] mesAndTop = new String[nrOfTopic];
							for (int c = 0; c < nrOfTopic; c++) {
								mesAndTop[c] = preferences.get("pswTopic" + c, "");
								System.out.println(mesAndTop[c]);
								//textTopicAndMessages.insert("topic: '" + txtTopic + " ' ," + " message: '" + message + "'", nrOfRows);
								//nrOfRows++;
							}*/
					}
						}
					//}
				}
			}
		});
		
		JButton disconnect_button = new JButton("Disconnect");	
		disconnect_button.setBounds(230, 30 ,120, 20);
		windows2.getContentPane().add(disconnect_button);
		disconnect_button.setVisible(true);
		disconnect_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//b.disconnectedBroker(mqttClient);
				//isConnectedMqtt = false;
				try {
					mqttClient.disconnect();
					System.out.println("Disconnected");
				} catch (MqttException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		JButton logOut_button = new JButton("Log out"); 
		logOut_button.setBounds(100, 310, 100, 20);
		windows2.getContentPane().add(logOut_button);
		logOut_button.setVisible(true);
		logOut_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				windows2.setVisible(false);
				initializeWindows1();
			}
			
		});
		
		JLabel subscribeMessage = new JLabel("Write topic to subscribe: ");
		subscribeMessage .setBounds(100, 250, 220, 20);
		windows2.getContentPane().add(subscribeMessage);
		
		final JTextField fielsubscribe = new JTextField();
		fielsubscribe.setBounds(100, 270, 130, 20);
		windows2.getContentPane().add(fielsubscribe);
		
		JButton susbscribe_button = new JButton("Subscribe "); 
		susbscribe_button.setBounds(270, 270, 100, 20);
		windows2.getContentPane().add(susbscribe_button);
		susbscribe_button.setVisible(true);
		susbscribe_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] messSubscribe = new String[nrOfTopic];
				try {
					for (int t = 0; t < nrOfTopic; t++) { // parcurgeti topicurile
						messSubscribe[t] = preferences.get("pswTopic" + t, "");
						System.out.println(messSubscribe[t]);
						System.out.println("topic nou: " + fieldPublish.getText() + " si topic dinainte: " +  messSubscribe[t]);
						if (fielsubscribe.getText().equals(messSubscribe[t])) {
							//mqttClient.setCallback(this);
							mqttClient.subscribe(fieldPublish.getText());
							mqttClient.unsubscribe(fieldPublish.getText());
							System.out.println("subscribe topic");
						}
					}
				} catch (MqttException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		JButton unsusbscribe_button = new JButton("Unsubscribe "); 
		unsusbscribe_button.setBounds(220, 250, 120, 20);
		//windows2.getContentPane().add(unsusbscribe_button);
		unsusbscribe_button.setVisible(true);
		
		JButton noName = new JButton("????"); 
		noName.setBounds(390, 110, 100, 20);
		windows2.getContentPane().add(noName);
		noName.setVisible(false);
		
		windows2.setVisible(true);
		} catch (MqttException e1) {
			e1.printStackTrace();  
		}
	}
}