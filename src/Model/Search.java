package Model;

import java.sql.Types;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Search {


	@SuppressWarnings("unchecked")
	public static String searchByName(String name) {
		String callStatement = "{? = call Search_Restaurants_By_Name( ? ) }";
		JSONObject json = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONObject inputObject = new JSONObject();
		inputObject.put("type", Types.VARCHAR);
		inputObject.put("value", name);
		jsonArray.add(inputObject);
		json.put("call_statement", callStatement);
		json.put("out_type", Types.OTHER);
		json.put("input_array", jsonArray);
		return json.toString();
	}
	
	
	
}
