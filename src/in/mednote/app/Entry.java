package in.mednote.app;

import org.json.JSONException;
import org.json.JSONObject;

public class Entry {
	String user_id;
	String visitName;
	String doctor;
	String hospital;
	String remarks;
	String entryDate;
	
	public JSONObject extractJSONOjbect() {
		JSONObject object = new JSONObject();
		
		try {
			object.put("id", user_id);
			object.put("name", visitName);
			object.put("doctor", doctor);
			object.put("hospital", hospital);
			object.put("remark", remarks);
			object.put("date", entryDate);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return object;
	}
}
