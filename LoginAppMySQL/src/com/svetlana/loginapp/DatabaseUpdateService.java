package com.svetlana.loginapp;



import java.util.ArrayList;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class DatabaseUpdateService extends Service {
	
	  private final static String REGISTER_MSG_KEY = "com.tecblogon.loginexample.REGISTER_MSG";
	  private final static String TAG = "DatabaseUpdateService";
	  private final static int REGISTER_USER = 1;
	  public static final String RESULT = "result";
	  public static final String NOTIFICATION = "com.techblogon.loginexample";
	  private DatabaseUpdateService mySelf = this;
	  final Messenger mMessenger = new Messenger( new IncomingMessageHandler());
	  ArrayList<Messenger> mClients = new ArrayList<Messenger>();
	  int mValue = 0;
	  
	  final static int MSG_REGISTER_CLIENT = 2;
	  private final static int MSG_UNREGISTER_CLIENT = 3;
	  public final static int MSG_USER_REGISTERED =4;
	  
	  class IncomingMessageHandler extends Handler {
		  @Override
		  public void handleMessage(Message msg) {
			  switch(msg.what) {
			  	case REGISTER_USER:
				  Log.i(TAG,msg.getData().getString(REGISTER_MSG_KEY));
				  String username = msg.getData().getString("userName");
	    		  String password = msg.getData().getString("password");
	    		  Log.i(TAG,"Username= "+username+", Password: "+password);
	    		  for( int i=mClients.size()-1; i>=0; i-- ) {
	    			  try {
	    				  mClients.get(i).send(Message.obtain(null,MSG_USER_REGISTERED));
	    			  }
	    			  catch ( RemoteException e) {
	    				  mClients.remove(i);
	    			  }
	    		  }
	    		  break;
			  	case MSG_REGISTER_CLIENT:
			  		mClients.add(msg.replyTo);
			  		break;
			  	case MSG_UNREGISTER_CLIENT:
			  		mClients.remove(msg.replyTo);
			  		break;
			  	default:
					  super.handleMessage(msg);
			  }
		  }
	  }
	
	  
//	  private void publishResults(int result) {
//		    Intent intent = new Intent(NOTIFICATION);
//		    intent.putExtra(RESULT, result);
//		    sendBroadcast(intent);
//		  }
	  
	  
	  private final IBinder mBinder = new MyBinder();
	  
	  @Override
	  public IBinder onBind(Intent intent) {
		  mySelf = this;
		  return mMessenger.getBinder();
	  }
	  
	  @Override
	  public int onStartCommand(Intent intent, int flags, int startId) {
	      Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

	      if( intent.getAction().equalsIgnoreCase("svetlana.register_user") ) {
	    	  if( intent.hasExtra("userName") && intent.hasExtra("password") ) {
	    		  String username = intent.getStringExtra("userName");
	    		  String password = intent.getStringExtra("password");
	    		  Toast.makeText(this, "Registering user "+username+", passwd "+password, Toast.LENGTH_SHORT).show();
	    	  }
	    	  else {
	    		  Toast.makeText(this, "Cannot register user without username and password" , Toast.LENGTH_SHORT).show();
	    	  }
	      }
	      
	      mySelf = this;

	      // If we get killed, after returning from here, restart
	      return START_NOT_STICKY;
	  }

	

	  @Override
	  public void onDestroy() {
	    Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
	  }

	public class MyBinder extends Binder {
		DatabaseUpdateService getService() {
	      return DatabaseUpdateService.this;
	    }
	  }

	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		
	}

	}