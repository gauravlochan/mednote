package in.trackmyhealth.app;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
	protected Button _loginButton;
	protected EditText _username;
	protected EditText _pass;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        _loginButton = ( Button ) findViewById( R.id.button1 );
        _loginButton.setOnClickListener( new ButtonClickHandler() );
        
        _username = ( EditText ) findViewById( R.id.editText1);
        _pass = ( EditText ) findViewById( R.id.editText2);
    }

    public class ButtonClickHandler implements View.OnClickListener 
    {
    	public void onClick( View view ) {
    		JSONObject json = null;

    		// Check credentials
    		String username = _username.getText().toString();
    		String password = _pass.getText().toString();
    		
    		String success = null;
    		
    		String result = RESTHelper.Authenticate(username, password);
    		try {
				json = new JSONObject(result);
				success = json.getString("status");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    		if (success.equals("false")) {
    	    	Log.i(Global.Company, "failed");
    	    	return;
    		} else {
        		// If succeeded
    			String id = "";
				try {
					id = json.getString("id");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			Log.i(Global.Company, "user id ="+id);
        		Intent intent = new Intent(view.getContext(), AddEntryActivity.class);
        		intent.putExtra("user_id", id);
                startActivity(intent);
    		}
         
    	}
    }

}
