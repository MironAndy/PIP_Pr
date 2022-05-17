package MQTT;
import java.awt.EventQueue;
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
	}

}