package com.svetlana.loginapp;



import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.svetlana.library.DatabaseHandler;
import com.svetlana.library.UserFunctions;

public class DatabaseSyncService extends Service {
	
	  private final static String SYNC_MSG_KEY = "com.svetlana.loginapp.SYNC_MSG";
	  private final static String TAG = "DatabaseSyncService";
	  private final static int SYNC_DB= 1;
	  public static final String RESULT = "result";
	  public static final String NOTIFICATION = "com.svetlana.loginapp";

	    private static String KEY_SUCCESS = "success";
	    private static String KEY_UID = "uid";
	    private static String KEY_USERNAME = "uname";
	    private static String KEY_FIRSTNAME = "fname";
	    private static String KEY_LASTNAME = "lname";
	    private static String KEY_EMAIL = "email";
	    private static String KEY_CREATED_AT = "created_at";
	    private static String BUSINESS_NAME = "businessname";
	    private static String POINTS = "pointvalues";
	    private static String UPDATED_AT = "updated_at";
	    String inputEmail;
	    String inputPassword;

	  final Messenger mMessenger = new Messenger( new IncomingMessageHandler());
	  //ACTIVITIES connecting to this service
	  ArrayList<Messenger> mClients = new ArrayList<Messenger>();
	  int mValue = 0;
	  
	  final static int MSG_REGISTER_CLIENT = 2;
	  private final static int MSG_UNREGISTER_CLIENT = 3;
	  public final static int MSG_DB_SYNCED =4;
	  
	  @SuppressLint("HandlerLeak")
	class IncomingMessageHandler extends Handler {
		  @Override
		  public void handleMessage(Message msg) {
			  switch(msg.what) {
			  	case SYNC_DB:
				  Log.i(TAG,msg.getData().getString(SYNC_MSG_KEY));
				  String email = msg.getData().getString("email");
	    		  String password = msg.getData().getString("password");
	    		  Log.i(TAG,"email= "+email+", Password: "+password);
	    		  inputEmail = email;
	    		  inputPassword = password;
	    		  NetAsync();
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
	

	  
	  @Override
	  public IBinder onBind(Intent intent) {
		  return mMessenger.getBinder();
	  }
	  
	  @Override
	  public int onStartCommand(Intent intent, int flags, int startId) {
//	      Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
//
//	      if( intent.getAction().equalsIgnoreCase("svetlana.sync_db") ) {
//	    	  if( intent.hasExtra("userName") && intent.hasExtra("password") ) {
//	    		  String username = intent.getStringExtra("userName");
//	    		  String password = intent.getStringExtra("password");
//	    		  Toast.makeText(this, "Registering user "+username+", passwd "+password, Toast.LENGTH_SHORT).show();
//	    	  }
//	    	  else {
//	    		  Toast.makeText(this, "Cannot register user without username and password" , Toast.LENGTH_SHORT).show();
//	    	  }
//	      }


	      // If we get killed, after returning from here, restart
	      return START_NOT_STICKY;
	  }

	

	  @Override
	  public void onDestroy() {
	    Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
	  }

	public class MyBinder extends Binder {
		DatabaseSyncService getService() {
	      return DatabaseSyncService.this;
	    }
	  }

	protected void onHandleIntent(Intent intent) {
		
		
	}
	
	protected int loginAndSyncDb(JSONObject json) {
        try {
           if (json.getString(KEY_SUCCESS) != null) {

                String res = json.getString(KEY_SUCCESS);

                if(Integer.parseInt(res) == 1){
                   
                    DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                    JSONObject json_user = json.getJSONObject("user");
                    /**
                     * Clear all previous data in SQlite database.
                     **/
                    UserFunctions logout = new UserFunctions();
                    logout.logoutUser(getApplicationContext());
                    db.addUser(json_user.getString(KEY_FIRSTNAME),json_user.getString(KEY_LASTNAME),json_user.getString(KEY_EMAIL),json_user.getString(KEY_USERNAME),json_user.getString(KEY_UID),json_user.getString(KEY_CREATED_AT));
                    savePreferences("USER_ID", json_user.getString(KEY_UID));
                    
                    // Now load the points table

                    JSONArray points = json_user.getJSONArray("points");
                    
                   
                   for(int i=0;i<points.length();i++){
                	   JSONObject obj = (JSONObject)points.get(i);
                	   db.setPoints(obj.getString(KEY_UID),obj.getString(BUSINESS_NAME),obj.getString(POINTS),obj.getString(UPDATED_AT));
                   }
                    /**
                    *If JSON array details are stored in SQlite it launches the User Panel.
                    **/
                   
                    return -1;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
   }


public void savePreferences(String key, String value)
{
    SharedPreferences prefs = getApplicationContext().getSharedPreferences(getApplicationContext().getPackageName(), 0);
    prefs.edit().putString(key, value).commit();
}

	private class NetCheck extends AsyncTask<String,String,Boolean>
	{
	 
	
	    @Override
	    protected void onPreExecute(){
	        super.onPreExecute();
	    }
	
	    @Override
	    protected Boolean doInBackground(String... args){
	       
	       return true;
	
	    }
	    @Override
	    protected void onPostExecute(Boolean th){
	
	        if(th == true){
//	            nDialog.dismiss();
	            new ProcessLogin().execute();
	        }
	
	    }
	}
	
	
    private class ProcessLogin extends AsyncTask<String, String, JSONObject> {

    	String email,password;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
    
            email = inputEmail;
            password = inputPassword;
           
        }

        @Override
        protected JSONObject doInBackground(String... args) {
        	UserFunctions userFunction = new UserFunctions();
        	JSONObject json = userFunction.loginUser(email, password);
        	return json;
           
        }




        
        @Override
        protected void onPostExecute(JSONObject json) {
        	for( int i=mClients.size()-1; i>=0; i-- ) {
  			  try {
  				  //do processing
  				  int resultCode = loginAndSyncDb(json);
  				  Bundle bundle = new Bundle();
  				  bundle.putInt("result", resultCode);
  				  Message msgToSend = Message.obtain(null,MSG_DB_SYNCED);
  				  msgToSend.setData(bundle);
  				  mClients.get(i).send(msgToSend);
  			  }
  			  catch ( RemoteException e) {
  				  mClients.remove(i);
  			  }
  		  }

      }
    }
    public void NetAsync(){
        new NetCheck().execute();
    }
	}