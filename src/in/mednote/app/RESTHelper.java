package in.mednote.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class RESTHelper {
	//private static String _server = "http://192.168.2.139:3000/";
	//private static String _server = "http://10.0.0.19:3000/";
	private static String _server = "http://cottonslate.com/";
	
	public static String addEntry(Entry entry, String filePath) {
		JSONObject object = entry.extractJSONOjbect(); 
		String serverUrl = 	_server.concat("receipt/create_api");
		String result = null;
		
		try {
			// object.put("next_appointment", "2011-11-20");
			
			try {
				String imgString = null;
				imgString = Base64.encodeFromFile(filePath);
				object.put("image", imgString);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			result = postJSONObject(serverUrl, object);	
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public static String Authenticate(String username, String password) {
		JSONObject auth = new JSONObject();
		String result = null;
		String serverUrl = _server.concat("auth_verify");
		
		try {
			auth.put("username", username);
			auth.put("password", password);
			result = postJSONObject(serverUrl, auth);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Log.i(Global.Company, result);
		return result;
	}
	
	/**
     * A simple method to make a REST call to the specified server.  Good for 
     * testing
     * 
     * @param serverUrl
     */
    public static String postJSONObject(String serverUrl, JSONObject payload) { 
        Log.d(Global.Company, "Attempting call to REST");

        HttpPost httpPost = new HttpPost(serverUrl);
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("Accept", "application/json");
        
        HttpEntity requestEntity = null;
        try {
        	String json = payload.toString();
        	Log.d(Global.Company, json);
            requestEntity = new StringEntity(json);
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        httpPost.setEntity(requestEntity);
        return handleResponse(httpPost);
    }
    
    
    private static String handleResponse(HttpPost httpPost) {
        HttpClient httpClient = new DefaultHttpClient();
        String result = null;

        try {
            HttpResponse response = null;
    		Log.i(Global.Company, "Calling REST api...");
            response = httpClient.execute(httpPost);
            
            if (response != null) {
                Log.d(Global.Company, "Competed call to REST");
                Log.d(Global.Company, response.getStatusLine().toString());
                
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream instream = entity.getContent();
                    result = convertStreamToString(instream);
                    Log.i(Global.Company, "Result of converstion: [" + result + "]");
                    instream.close();
                } else {
                    Log.d(Global.Company, "Empty Http response");
                }
            } else {
                Log.d(Global.Company, "Could not call REST");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return result;
    }
    
    
    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


	
//	SendMultiPart() {
//		DefaultHttpClient httpclient = new DefaultHttpClient();     
//		HttpPost httpPost = new HttpPost(urlString);
//		MultipartEntity entity = new MultipartEntity();
//
//		File imageFile = new File("/sdcard/dir/image.jpg");
//		entity.addPart("image", new FileBody(imageFile));
//		httpPost.setEntity(entity);
//		response = httpclient.execute(httpPost);

	/**
	 * Upload Multipart
	 * 
	 * @param filePath
	 */
	public static void uploadMultipart(String filePath) {
		Log.i(Global.Company, "Ready to upload file...");
		HttpClient client = new DefaultHttpClient();
		client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
				HttpVersion.HTTP_1_1);

		Log.i(Global.Company, "Set remote URL...");
		HttpPost httpPost = new HttpPost(_server);
	
		MultipartEntity entity = new MultipartEntity();

		Log.i(Global.Company, "Adding file(s)...");
		
		try {
			entity.addPart("name", new StringBody("Hysteria"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		File image = new File(filePath);
		FileBody fileBody = new FileBody(image, "application/octet-stream");
		entity.addPart("image", fileBody);

		Log.i(Global.Company, "Set entity...");
		httpPost.setEntity(entity);
		
		handleResponse(httpPost);
	}

}
