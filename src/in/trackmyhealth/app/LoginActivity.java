package in.trackmyhealth.app;

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
    		// Check credentials
    		Log.i("MakeMachine", _username.getText().toString() + _pass.getText().toString());
    		
    		// If succeeded
    		Intent intent = new Intent(view.getContext(), AddEntryActivity.class);
            startActivity(intent);
         
    	}
    }

}
