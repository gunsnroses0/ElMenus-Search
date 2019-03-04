package Commands;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;
import Model.Search;

public class RetrieveSearch extends ConcreteCommand {

	@Override
	protected void execute() {
		this.consume("r1");
        HashMap < String, Object > props = parameters;
        Channel channel = (Channel) props.get("channel");
        JSONParser parser = new JSONParser();

		JSONObject messageBody;
		try {
			messageBody = (JSONObject) parser.parse((String) props.get("body"));
			String uri = (messageBody).get("uri").toString();

			String name = "";
			if(uri.contains("?name=")) {
				name = ((JSONObject) (messageBody).get("parameters")).get("name").toString();
			}
			
	        AMQP.BasicProperties properties = (AMQP.BasicProperties) props.get("properties");
	        AMQP.BasicProperties replyProps = (AMQP.BasicProperties) props.get("replyProps");
	        Envelope envelope = (Envelope) props.get("envelope");
			
	        String results = Search.searchByName(name);	        
	        sendMessage("database", properties.getCorrelationId(), results);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

	@Override
	public void handleApi(HashMap<String, Object> service_parameters) {
		HashMap<String, Object> props = parameters;
		AMQP.BasicProperties properties = (AMQP.BasicProperties) props.get("properties");
		AMQP.BasicProperties replyProps = (AMQP.BasicProperties) props.get("replyProps");
		String serviceBody = service_parameters.get("body").toString();

		Envelope envelope = (Envelope) props.get("envelope");
		try {
			channel.basicPublish("", properties.getReplyTo(), replyProps, serviceBody.getBytes("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
