package MQTT;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import java.util.ArrayList;
import java.util.List;

public class Subscriber {
	//store all mesages received by the subscriber
	private List <MqttMessage> subscriberMessages = new ArrayList <MqttMessage>();
	
	public List <MqttMessage> getSubscriberMessages() {
		return subscriberMessages;
	}
	public void setSubscriberMessages(List <MqttMessage> subscriberMessages) {
		this.subscriberMessages = subscriberMessages;
	}
	
	//add subscriber for a topic
	public void addSubscriber(String topic) {
		
	}
	public void unSubscriber(String topic) {
		
	}
	public void printMessagesTopic() {
		for (MqttMessage message : subscriberMessages) {
			//System.out.println("Messages Topic " +  message.getTopic() + ":" + message.getPayload());
		}
	}
}
