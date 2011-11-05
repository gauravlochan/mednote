package in.trackmyhealth.app;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class AddEntryActivity extends Activity 
{
	protected Button _takePhotoButton;
	protected Button _uploadButton;
	protected String _path;
	protected boolean _taken;
	
	protected static final String PHOTO_TAKEN	= "photo_taken";
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
		
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.addentry);
       
        _takePhotoButton = ( Button ) findViewById( R.id.TakePhoto );
        _takePhotoButton.setOnClickListener( new TakePhotoClickHandler() );
        
        _uploadButton = ( Button ) findViewById( R.id.buttonUpload );
        _uploadButton.setOnClickListener( new UploadButtonClickHandler() );
        
        _path = Environment.getExternalStorageDirectory() + "/medical_record.jpg";
    }
  
    
    public class TakePhotoClickHandler implements View.OnClickListener 
    {
    	public void onClick( View view ){
    		startCameraActivity();
    	}
    }
    
    public class UploadButtonClickHandler implements View.OnClickListener 
    {
    	public void onClick( View view ){
    		uploadRecord();
    	}
    }
    
    protected void startCameraActivity()
    {
    	Log.i("MakeMachine", "startCameraActivity()" );
    	File file = new File( _path );
    	Uri outputFileUri = Uri.fromFile( file );
    	
    	Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE );
    	intent.putExtra( MediaStore.EXTRA_OUTPUT, outputFileUri );
    	
    	startActivityForResult( intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {	
    	Log.i( "MakeMachine", "resultCode: " + resultCode );
    	switch( resultCode )
    	{
    		case 0:
    			Log.i( "MakeMachine", "User cancelled" );
    			break;
    			
    		case -1:
    			onPhotoTaken();
    			break;
    	}
    }
    
    /**
     * Uses the photo and displays it on the screen above the button
     */
    protected void onPhotoTaken()
    {
    	Log.i( "MakeMachine", "onPhotoTaken" );
    	_taken = true;

    	_takePhotoButton.setEnabled(false);
    	_takePhotoButton.setText("Clicked");
    }
    
    
    protected void uploadRecord() {
    	RESTHelper.uploadJson(_path);
    	//RESTHelper.uploadMultipart(_path);
    }
    
    
    @Override 
    protected void onRestoreInstanceState( Bundle savedInstanceState){
    	Log.i( "MakeMachine", "onRestoreInstanceState()");
    	if( savedInstanceState.getBoolean( AddEntryActivity.PHOTO_TAKEN ) ) {
    		onPhotoTaken();
    	}
    }
    
    @Override
    protected void onSaveInstanceState( Bundle outState ) {
    	outState.putBoolean( AddEntryActivity.PHOTO_TAKEN, _taken );
    }
}