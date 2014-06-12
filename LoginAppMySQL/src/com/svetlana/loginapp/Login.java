package com.svetlana.loginapp;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;





public class Login extends Activity {

    Button btnLogin;
    Button Btnregister;
    Button passreset;
    EditText inputEmail;
    EditText inputPassword;
    private TextView loginErrorMsg;
	Activity mlogin = null;
	DatabaseSyncService dss = null;
    /**
     * Called when the activity is first created.
     */
//    private static String KEY_SUCCESS = "success";
//    private static String KEY_UID = "uid";
//    private static String KEY_USERNAME = "uname";
//    private static String KEY_FIRSTNAME = "fname";
//    private static String KEY_LASTNAME = "lname";
//    private static String KEY_EMAIL = "email";
//    private static String KEY_CREATED_AT = "created_at";
//    private static String BUSINESS_NAME = "businessname";
//    private static String POINTS = "pointvalues";
//    private static String UPDATED_AT = "updated_at";
	private final static int SYNC_DB = 1;
    private final static int MSG_REGISTER_CLIENT = 2;
	private final static String SYNC_MSG_KEY = "com.svetlana.loginapp.SYNC_MSG";
	private final static String TAG = "Login";
//	private final static String POINTS_DATA="pointsData";
    
	private final static Intent mDatabaseSyncServiceIntent = new Intent("com.svetlana.loginapp.DatabaseSyncService");
	private Messenger mMessengerToDatabaseSyncservice=null;
	private boolean mIsBound;
	final Messenger mMessenger = new Messenger(new IncomingHandler());
	
	
    private ServiceConnection mConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mMessengerToDatabaseSyncservice = new Messenger(service);
			try {
				Message msg = Message.obtain(null, Login.MSG_REGISTER_CLIENT);
				msg.replyTo = mMessenger;
				if(mMessengerToDatabaseSyncservice != null) {
					mMessengerToDatabaseSyncservice.send(msg);
				}
			}
			catch(RemoteException e) {}
			mIsBound = true;			
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mMessengerToDatabaseSyncservice = null;
			mIsBound = false;			
		}
	};

	@SuppressLint("HandlerLeak")
	class IncomingHandler extends Handler {
		 @Override
		 public void handleMessage(Message msg) {
			 switch(msg.what) {
			 case DatabaseSyncService.MSG_DB_SYNCED:
				 final ProgressDialog pDialog =  new ProgressDialog(Login.this);
	             Bundle data = msg.getData();
	             if((int)data.get("result")==RESULT_OK){
	             pDialog.setMessage("Loading User Space");
	             pDialog.setTitle("Getting Data");
				 Toast.makeText(getApplicationContext(),
			              "Sync Complete",Toast.LENGTH_SHORT).show();
				 Intent upanel = new Intent(getApplicationContext(), Main.class);
                 upanel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                 pDialog.dismiss();
                 startActivity(upanel);
                 /**
                  * Close Login Screen
                  **/
                 finish();
	             }
	             else{
	            	 pDialog.dismiss();
	               	 Toast.makeText(Login.this, "Login failed",
				     Toast.LENGTH_LONG).show();
	                    loginErrorMsg.setText("Incorrect username/password");
	             }
				 break;
			 default:
				 super.handleMessage(msg);
			 }
		 }
	 }
	
	 
	
	  
	  @Override
		protected void onResume() {
			super.onResume();
			bindService(mDatabaseSyncServiceIntent, mConnection, Context.BIND_AUTO_CREATE);
			
		}
		
		@Override
		protected void onPause() {
			if(mIsBound)
				unbindService(mConnection);
			
			super.onPause();
		}
		
		@Override
		protected void onDestroy() {
			super.onDestroy();
		
		}
		
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        Intent intent = mDatabaseSyncServiceIntent;
	    getApplicationContext().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	    
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.pword);
        Btnregister = (Button) findViewById(R.id.registerbtn);
        btnLogin = (Button) findViewById(R.id.login);
        passreset = (Button)findViewById(R.id.passres);
        loginErrorMsg = (TextView) findViewById(R.id.loginErrorMsg);
        mlogin = this;
        passreset.setOnClickListener(new View.OnClickListener() {
        public void onClick(View view) {
        Intent myIntent = new Intent(view.getContext(), PasswordReset.class);
        startActivityForResult(myIntent, 0);
        finish();
        
        }});


        Btnregister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Register.class);
                startActivityForResult(myIntent, 0);
                finish();
             }});


        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                if (  ( !inputEmail.getText().toString().equals("")) && ( !inputPassword.getText().toString().equals("")) )
                {
                	 if( mIsBound ) {
     			    	// Create Message
     			    	Message msg = Message.obtain(null, SYNC_DB);
     			    	Bundle bundle = new Bundle();
     			    	bundle.putString(SYNC_MSG_KEY, "syncdatabase");
     			    	bundle.putString("email", inputEmail.getText().toString());
     			    	bundle.putString("password", inputPassword.getText().toString());
     			    	msg.setData(bundle);
     			    	try {
     			    		mMessengerToDatabaseSyncservice.send(msg);
     			    	}
     			    	catch(RemoteException re) {
     			    		Log.e(TAG,re.toString());
     			    	}
     					Toast.makeText(getApplicationContext(), "Logging in ", Toast.LENGTH_LONG).show();
     			    }
                 
                }
                else if ( ( !inputEmail.getText().toString().equals("")) )
                {
                    Toast.makeText(getApplicationContext(),
                            "Password field empty", Toast.LENGTH_SHORT).show();
                }
                else if ( ( !inputPassword.getText().toString().equals("")) )
                {
                    Toast.makeText(getApplicationContext(),
                            "Email field empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "Email and Password field are empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



   
}