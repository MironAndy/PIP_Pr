package MQTT;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class GraphicsWindows {
	public JFrame windows1 = new JFrame();
	public JFrame windows2 = new JFrame();
	
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
		nickname.setVisible(true); //aici trebuie true
		nickname.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				nickname.setText("");
				}
		});
		
		final JTextField password = new JTextField(" Password: "); 
		
		password.setEditable(true);
		password.setBounds(150,30,70,20);
		windows1.getContentPane().add(password);
		password.setVisible(true); //si aici
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
		
		System.out.println(" Istoric commit - Update ");
		
		windows2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		windows2.getContentPane().setBackground((new Color(100,200,225)));
		
		JTextField n = new JTextField(" Text Topic: "); 
		n.setEditable(true);
		n.setBounds(20,20,200,200);
		windows2.getContentPane().add(n);
		n.setVisible(true); //aici trebuie true
		
//		JTextField p = new JTextField(" Password: "); 
//		p.setEditable(true);
//		p.setBounds(150,30,70,20);
//		getContentPane().add(p);
//		p.setVisible(true); //si aici
		
		JButton logOut_button = new JButton("Log out"); 
		logOut_button.setBounds(250, 30, 80, 20);
		windows2.getContentPane().add(logOut_button);
		logOut_button.setVisible(true);
		logOut_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				windows2.setVisible(false);
				initializeWindows1();
			}
			
		});
		
		JButton newTopic_button = new JButton("Topic nou. "); 
		newTopic_button.setBounds(250, 70, 110, 20);
		windows2.getContentPane().add(newTopic_button);
		newTopic_button.setVisible(true);
		
		JButton subscribedTopic_button = new JButton("Topic abonat. "); 
		subscribedTopic_button.setBounds(250, 110, 130, 20);
		windows2.getContentPane().add(subscribedTopic_button);
		subscribedTopic_button.setVisible(true);
		
		JButton write_button = new JButton("Write. "); 
		write_button.setBounds(370, 70, 100, 20);
		windows2.getContentPane().add(write_button);
		write_button.setVisible(true);
		
		JButton read_button = new JButton("Read. "); 
		read_button.setBounds(480, 70, 100, 20);
		windows2.getContentPane().add(read_button);
		read_button.setVisible(true);
		
		JButton susbscribe_button = new JButton("Subscribe. "); 
		susbscribe_button.setBounds(590, 70, 100, 20);
		windows2.getContentPane().add(susbscribe_button);
		susbscribe_button.setVisible(true);
		
		
		JButton unsusbscribe_button = new JButton("Unsubscribe. "); 
		unsusbscribe_button.setBounds(390, 110, 130, 20);
		windows2.getContentPane().add(unsusbscribe_button);
		unsusbscribe_button.setVisible(true);
		
		JButton noName = new JButton("????"); 
		noName.setBounds(390, 110, 100, 20);
		windows2.getContentPane().add(noName);
		noName.setVisible(false);
		
	//	n.setVisible();
		windows2.setVisible(true);
	}
}
