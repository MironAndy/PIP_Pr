package MQTT;

//Importarea  Librariilor
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
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class GraphicsWindows implements MqttCallback {
	
	//creearea celor 2 ferestrea care compun interfata
	public JFrame windows1 = new JFrame("Log In");
	public JFrame windows2 = new JFrame("Main Application");
	
	ActionButton b = new ActionButton();
	String broker = "tcp://mqtt.eclipseprojects.io:1883";	//Adaugare brooker
    int qos = 0;
    
    //Declararea de variabile folosite in diverse functii
    //String clientId = "name1 Client";
    MemoryPersistence persistence = new MemoryPersistence();
    MqttClient mqttClient;
    String txtTopic;
    static int nrOfTopic = 0;
    int i = 70;
    static int nrOfRows = 0;
    boolean isConnectedMqtt = false;
    MqttMessage message;
    Preferences preferences = Preferences.userNodeForPackage(GraphicsWindows.class);		//Memoreaza topicurile folosind libraria preferences
    JTextField[] t = new JTextField[100];
    JTextField nickname;
    JPasswordField password2;
    JTextArea messagesBox;
    
	GraphicsWindows() {
		initializeWindows1();
	}
	
	void initNickname() {
		
		//Initierea textfieldului nickname din ferestra de logare
		nickname = new JTextField(" Nickname: "); 
		nickname.setEditable(true);
		nickname.setBounds(60,30,70,20);
		
		nickname.setVisible(true);
		//Actiunea la apasarea textfieldului
		nickname.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				nickname.setText(" ");
			}
		});
		windows1.getContentPane().add(nickname);
	}
	
	void initPasswordButton() {
		
		//inititalizarea campului password din fereastra de logare
		final JTextField password = new JTextField(" Password: "); 
		password.setEditable(true);
		password.setBounds(150,30,70,20);
		password.setVisible(true);
		//Adaugarea functiei de clik a fieldului password
		password.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				password.setVisible(false);
				password2 = new JPasswordField();
				password.setEditable(true);
				password2.setBounds(150,30,70,20);
				windows1.getContentPane().add(password2);	//convertirea continutului camului pasword din string  
				password2.setVisible(true);					// in buline pentru a pastra confidentialitatea clientului
			}
		});
		windows1.getContentPane().add(password);
	}
	
	void initLoginButton() {
		
		//Crearea butonului de Log In
		JButton logIn_button = new JButton("Log in"); 
		logIn_button.setBounds(250, 30, 80, 20);
		logIn_button.setVisible(true);
		//Adaugarea actiunii butonului
		logIn_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Butonul va verifica daca exista un cont introdus inainte de a efectua actiunea implementata
				if (!nickname.getText().isBlank() && password2.getPassword() != null 
						&&  password2.getPassword().length != 0) {
					System.out.print(password2.getPassword().toString());
					windows1.setVisible(false);
					initializeWindows2();		//Schimbarea ferestrelor aplicatiei
				}
			}
		});
		windows1.getContentPane().add(logIn_button);
	}
		
	void initializeWindows1() {
		
		windows1.setLayout(null);
		//Initialiazarea ferestrei de log In
		windows1.setBounds(800,300,400,300);
		windows1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		windows1.getContentPane().setBackground((new Color(100,200,225)));
		
		initNickname();			//apelul funtciei care creeaza campul nickname
		initPasswordButton();	//apelul funtciei care creeaza campul password
		initLoginButton();		//apelul funtciei care creeaza butonul de log in
		
		windows1.setVisible(true);
	}
	
	MqttConnectOptions initConnectionOptions() {
		
		//Initialiazarea conexiunii cu brooker-ul Mqtt
		MqttConnectOptions connOpts = new MqttConnectOptions();		//creearea variabilei connOpts care va fi utila mai tarziu
		connOpts.setCleanSession(true);
		connOpts.setUserName(nickname.getText());
		connOpts.setPassword(password2.getPassword());//toCharArray());
		
		connOpts.setAutomaticReconnect(true);	//Biblioteca va incerca automat sa se reconnecteze la server in cazul unei defectiuni in retea
		connOpts.setCleanSession(true);			//Acesta va elimina mesajele netrimise dintr-o rulare anterioara
		connOpts.setConnectionTimeout(100);		//Timpul de expirare a conexiunii este setat la 100 secunde
		
		return connOpts;			
	}
	
	void initConnectButton(final MqttConnectOptions connOpts) {
		
		//Crearea butonul Connect din interfata aplicatiei si functia de conectare
		JButton connect_button = new JButton("Connect");
		connect_button.setBounds(100, 30, 120, 20);
		connect_button.setVisible(true);
		//Conectarea propriu zisa la brooker prin intermediul connOpts la apasarea butonului
		connect_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//verifica conectivitatea pentru a evita o reconectare nedorita
				if (!isConnectedMqtt) {
					try {
						mqttClient.connect(connOpts);
						System.out.println("Connected");
						
					} catch (MqttException e1) {
						e1.printStackTrace();
					}
					isConnectedMqtt = true;
				}
			}
		});
		windows2.getContentPane().add(connect_button);
	}
	
	void initNewTopicButton() {
		
		//Crearea butonului care va dauga topicuri noi
		JButton newTopic_button = new JButton("Add new topic "); 
		newTopic_button.setBounds(100, 70, 150, 20);
		windows2.getContentPane().add(newTopic_button);
		newTopic_button.setVisible(true);
		//Definirea actiunilor butonului
		newTopic_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Testarea conexiunii la brooker
				if (mqttClient.isConnected()) {
					//mesaje pentru a ajuta proiectantul
					System.out.println("clientul este conectat la apasarea butonului 'add new topic'");
					System.out.println("la apasarea butonului 'add new topic' sunt " + nrOfTopic + " topicuri");
					txtTopic = JOptionPane.showInputDialog("Topic");
					//verificarea topicurilor existente
					if (txtTopic.length() > 0) {
						nrOfTopic++;
						preferences.put("pswTopic" + nrOfTopic, txtTopic);
						t[nrOfTopic] = new JTextField();
						t[nrOfTopic].setBounds(260, i, 150, 20);
						t[nrOfTopic].setText(preferences.get("pswTopic" + nrOfTopic, ""));
						t[nrOfTopic].setEditable(false);
						windows2.getContentPane().add(t[nrOfTopic]);
						i += 25;
						//mesaje pentru a ajuta proiectantul
						System.out.println("dupa adaugarea unui topic sunt: " + nrOfTopic + " topicuri");
						//nrOfTopic++;
						preferences.putInt("countTopic", nrOfTopic);
					}
				}
			}
		});
	}
	
	void initDeleteElements() {
		
		//Crearea sistemmului care sterge un topic care nu mai prezinta interes pentru client
		JLabel deleteTopic = new JLabel("Write what topic want to delete:");
		deleteTopic.setBounds(420, 70, 200, 20);
		windows2.getContentPane().add(deleteTopic);
		
		final JTextField fieldTopicDelete = new JTextField();
		fieldTopicDelete.setBounds(620, 70, 25, 20);
		windows2.getContentPane().add(fieldTopicDelete);
		
		JButton okDelete = new JButton("ok");
		okDelete.setBounds(650, 70, 50, 20);
		windows2.getContentPane().add(okDelete);
		//implementarea functionalitaii butonului ok
		okDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int indexTopicToDelete = Integer.parseInt(fieldTopicDelete.getText());
				//verificarea conectivitatii
				if (mqttClient.isConnected()) {
					//mesaje pentru a ajuta proiectantul
					System.out.println("dupa apasarea butonului ok sunt: " + nrOfTopic + " topicuri");
					//verificarea indexului topicului din lista de topicuri
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
						//mesaje pentru a ajuta proiectantul
						System.out.println("dupa apasarea butonului ok si elimare sunt: " + nrOfTopic + " topicuri");
					}
					else {
						//mesaje pentru a ajuta proiectantul
						System.out.println("Nu aveti topic-uri de eliminat, iar nr de topicuri este: " + nrOfTopic);
					}
				}
			}
		});
	}
	
	void initPublishButton(final JTextField fieldPublish,final JTextField fieldMessage,final JTextField textTopicAndMessages) {
		
		//Adaugarea butonului de publish si functionalitatea sa
		JButton publish_button = new JButton("Publish"); 
		publish_button.setBounds(500, 200, 100, 20);
		windows2.getContentPane().add(publish_button);
		publish_button.setVisible(true);
		publish_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//mesaje pentru a ajuta proiectantul
				MqttMessage message = new MqttMessage(fieldPublish.getText().getBytes());
		        message.setQos(qos);
		        message.setRetained(true); //sets retained message 
		        try {
		        	mqttClient.publish("topic", message);
		        	System.out.println("published" + message.getPayload());
				} catch (MqttPersistenceException e1) {
					e1.printStackTrace();
				} catch (MqttException e1) {
					e1.printStackTrace();
				}
			}
		});
	}
	
	void initDisconnectButton() {
		
		//Crearea butonului de Disconnect
			JButton disconnect_button = new JButton("Disconnect");	
			disconnect_button.setBounds(230, 30 ,120, 20);
			windows2.getContentPane().add(disconnect_button);
			disconnect_button.setVisible(true);
			//Implementarea functiei de deconectare de la brooker
			disconnect_button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//b.disconnectedBroker(mqttClient);
					//isConnectedMqtt = false;
					try {
						mqttClient.disconnect();			//deconectarea folosind functiile brooker-ului
						System.out.println("Disconnected");
					} catch (MqttException e1) {
						e1.printStackTrace();
					}
				}
			});	
		}
	
	void initLog_outButton() {
		
		//Creearea butonului de log out care va face trecerea intre interfete
		JButton logOut_button = new JButton("Log out"); 
		logOut_button.setBounds(100, 310, 100, 20);
		windows2.getContentPane().add(logOut_button);
		logOut_button.setVisible(true);
		//Implementarea tranzitiei
		logOut_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				windows2.setVisible(false);
				initializeWindows1();
			}
			
		});
		}
	
	void initSubscribeButton(final JTextField fieldPublish,final JTextField fielsubscribe) {
		
		//Crearea butonului de subscribe
		JButton susbscribe_button = new JButton("Subscribe "); 
		susbscribe_button.setBounds(270, 270, 100, 20);
		windows2.getContentPane().add(susbscribe_button);
		susbscribe_button.setVisible(true);
		//implementarea functionalitatii butonului ajuntandu-ne de functiile definite in clasa subscriber
		susbscribe_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] messSubscribe = new String[nrOfTopic];
				try {
					mqttClient.subscribe("topic");
				} catch (MqttException e1) {
					e1.printStackTrace();
				}
			}
		});
	}
	
	void initializeWindows2() {
		
		windows2.setLayout(null);
		//Creearea celei de-a 2 ferestre a interfetei si apelarea functiilor specifice acesteia
		try {
			mqttClient = new MqttClient(broker, nickname.getText(), persistence);
			
			final MqttConnectOptions connOpts = initConnectionOptions();
			
			System.out.println("Connecting to broker: " + broker);
			
			windows2.setBounds(800,300,900,400);
			windows2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			windows2.getContentPane().setBackground((new Color(100,200,225)));
		
			initConnectButton(connOpts);			//apelarea functiei de connectarea
			initNewTopicButton();					//apelarea functiei care creea butonul de adaugarae de topicuri
			initDeleteElements();					//apelarea functiei care sterge un element
			
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
		
		messagesBox = new JTextArea();
		messagesBox.setBounds(450, 250, 330, 80);
		messagesBox.setEditable(false);
		messagesBox.setVisible(true);
		windows2.getContentPane().add(messagesBox);
		
		final JTextField textTopicAndMessages = new JTextField();
        textTopicAndMessages.setBounds(620, 200, 180, 20);
        textTopicAndMessages.setEditable(false);
		windows2.getContentPane().add(textTopicAndMessages);
		
		initPublishButton(fieldPublish,fieldMessage,textTopicAndMessages);		//apelarea si creearea butonului de pusblish
		
		initDisconnectButton();					//Creearea butonului de disconnect
		
		initLog_outButton();					// Creearea butonului de log_out
				
		JLabel subscribeMessage = new JLabel("Write topic to subscribe: ");
		subscribeMessage .setBounds(100, 250, 220, 20);
		windows2.getContentPane().add(subscribeMessage);
		
		final JTextField fielsubscribe = new JTextField();
		fielsubscribe.setBounds(100, 270, 130, 20);
		windows2.getContentPane().add(fielsubscribe);
		
		initSubscribeButton(fieldPublish,fielsubscribe);		//creearea butonului de subscribe
		
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

	public void connectionLost(Throwable cause) {
		// TODO Auto-generated method stub
		
	}

	public void messageArrived(String topic, MqttMessage message) throws Exception {
		
		messagesBox.append(message.toString());
	}

	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
		
	}
}