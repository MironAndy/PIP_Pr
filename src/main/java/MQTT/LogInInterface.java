package MQTT;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

class logInInterface extends JFrame{
	

	private static final long serialVersionUID = 1L;
	
	logInInterface(){
		setTitle("Log In");
		setBounds(800,300,400,300);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setBackground((new Color(100,200,225)));
		
		final JTextField nickname = new JTextField(" Nickname: "); 
		nickname.setEditable(true);
		nickname.setBounds(60,30,70,20);
		getContentPane().add(nickname);
		nickname.setVisible(true); //aici trebuie true
		nickname.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				nickname.setText("");
				}
		});
		
		final JTextField password = new JTextField(" Password: "); 
		
		password.setEditable(true);
		password.setBounds(150,30,70,20);
		getContentPane().add(password);
		password.setVisible(true); //si aici
		password.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e)
			{
				//password.setText("");
				password.setVisible(false);
				JPasswordField password2 = new JPasswordField();
				password.setEditable(true);
				password2.setBounds(150,30,70,20);
				getContentPane().add(password2);
				password2.setVisible(true);
			}
		});
		
		JButton logIn_button = new JButton("Log in"); 
		logIn_button.setBounds(250, 30, 80, 20);
		getContentPane().add(logIn_button);
		logIn_button.setVisible(true);
		
		JTextField txt = new JTextField(" Text: "); 
		txt.setEditable(false);
		txt.setBounds(60,100,100,20);
		getContentPane().add(txt);
		txt.setVisible(false);
		
		JTextField txt2 = new JTextField(" Text: "); 
		txt.setEditable(true);
		txt.setBounds(60,30,250,20);
		getContentPane().add(txt);
		txt.setVisible(false);
		
	//	n.setVisible();
		setVisible(true);
	}
	
	

}
public class LogIn{
	
	public static void main(String[] args) {
		new logInInterface();

	}

}