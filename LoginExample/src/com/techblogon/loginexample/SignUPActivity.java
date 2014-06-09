package com.techblogon.loginexample;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

public class SignUPActivity extends Activity
{
	EditText editTextUserName,editTextPassword,editTextConfirmPassword;
	Button btnCreateAccount;
	Activity mSignupActivity = null;
	DatabaseUpdateService dus = null;
	private final static int REGISTER_USER = 1;
    private final static int MSG_REGISTER_CLIENT = 2;
    private final static int MSG_UNREGISTER_CLIENT = 3;
    private final static int MSG_USER_REGISTERED =4;
	private final static String REGISTER_MSG_KEY = "com.tecblogon.loginexample.REGISTER_MSG";
	private final static String TAG = "SignUPActivity";
	
	private final static Intent mDatabaseUpdateServiceIntent = new Intent("com.techblogon.loginexample.DatabaseUpdateService");
	private Messenger mMessengerToDatabaseUpdateservice=null;
	private boolean mIsBound;
	final Messenger mMessenger = new Messenger(new IncomingHandler());
	private ServiceConnection mConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mMessengerToDatabaseUpdateservice = new Messenger(service);
			try {
				Message msg = Message.obtain(null, SignUPActivity.MSG_REGISTER_CLIENT);
				msg.replyTo = mMessenger;
				if(mMessengerToDatabaseUpdateservice != null) {
					mMessengerToDatabaseUpdateservice.send(msg);
				}
			}
			catch(RemoteException e) {}
			mIsBound = true;			
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mMessengerToDatabaseUpdateservice = null;
			mIsBound = false;			
		}
	};
	 private TextView textView;
	 
	 class IncomingHandler extends Handler {
		 @Override
		 public void handleMessage(Message msg) {
			 switch(msg.what) {
			 case DatabaseUpdateService.MSG_USER_REGISTERED:
				 //textView.setText("Received from service: "+msg.arg1);
				 Toast.makeText(getApplicationContext(),
			              "Message received",Toast.LENGTH_SHORT).show();
				 break;
			 default:
				 super.handleMessage(msg);
			 }
		 }
	 }
	 
	 
	private BroadcastReceiver receiver = new BroadcastReceiver() {

	    @Override
	    public void onReceive(Context context, Intent intent) {
	      Bundle bundle = intent.getExtras();
	      if (bundle != null) {
	        int resultCode = bundle.getInt(DatabaseUpdateService.RESULT);
	        if (resultCode == RESULT_OK) {
//	          Toast.makeText(SignUPActivity.this,
//	              "Download complete. Download URI: " + 
//	              Toast.LENGTH_LONG).show();
	          textView.setText("Registration done");
	        } else {
	          Toast.makeText(SignUPActivity.this, "Registration failed",
	              Toast.LENGTH_LONG).show();
	          textView.setText("Registration failed");
	        }
	      }
	    }
	  };
	
	LoginDataBaseAdapter loginDataBaseAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);
		
		Intent intent = mDatabaseUpdateServiceIntent;
	    getApplicationContext().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		mSignupActivity = this;
		if(dus == null) {
			dus = new DatabaseUpdateService();
		}
		
		// get Instance  of Database Adapter
		loginDataBaseAdapter=new LoginDataBaseAdapter(this);
		loginDataBaseAdapter=loginDataBaseAdapter.open();
		
		// Get Refferences of Views
		editTextUserName=(EditText)findViewById(R.id.editTextUserName);
		editTextPassword=(EditText)findViewById(R.id.editTextPassword);
		editTextConfirmPassword=(EditText)findViewById(R.id.editTextConfirmPassword);
		
		btnCreateAccount=(Button)findViewById(R.id.buttonCreateAccount);
		btnCreateAccount.setOnClickListener(new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			String userName=editTextUserName.getText().toString();
			String password=editTextPassword.getText().toString();
			String confirmPassword=editTextConfirmPassword.getText().toString();
			
			// check if any of the fields are vaccant
			if(userName.equals("")||password.equals("")||confirmPassword.equals(""))
			{
					Toast.makeText(getApplicationContext(), "Field Vaccant", Toast.LENGTH_LONG).show();
					return;
			}
			// check if both password matches
			if(!password.equals(confirmPassword))
			{
				Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_LONG).show();
				return;
			}
			else
			{
			    if( mIsBound ) {
			    	// Create Message
			    	Message msg = Message.obtain(null, REGISTER_USER);
			    	Bundle bundle = new Bundle();
			    	bundle.putString(REGISTER_MSG_KEY, "RegisterUser");
			    	bundle.putString("userName", userName);
			    	bundle.putString("password", password);
			    	msg.setData(bundle);
			    	try {
			    		mMessengerToDatabaseUpdateservice.send(msg);
			    	}
			    	catch(RemoteException re) {
			    		Log.e(TAG,re.toString());
			    	}
					Toast.makeText(getApplicationContext(), "Account Successfully Created ", Toast.LENGTH_LONG).show();
			    }
			}
		}
	});
		
	
}
	@Override
	protected void onResume() {
		super.onResume();
		bindService(mDatabaseUpdateServiceIntent, mConnection, Context.BIND_AUTO_CREATE);
		registerReceiver(receiver, new IntentFilter(DatabaseUpdateService.NOTIFICATION));
	}
	
	@Override
	protected void onPause() {
		if(mIsBound)
			unbindService(mConnection);
		
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		loginDataBaseAdapter.close();
	}
	
	
}
