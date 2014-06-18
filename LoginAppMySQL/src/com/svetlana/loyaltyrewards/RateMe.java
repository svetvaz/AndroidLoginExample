package com.svetlana.loyaltyrewards;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.Toast;

import com.svetlana.library.DatabaseHandler;
import com.svetlana.library.UserFunctions;
 
public class RateMe extends Activity {
 
  private RatingBar ratingBar;
  private EditText txtFeedback;
  private Button btnSubmit;
  private Button btnbktomenu;
  boolean ratingChanged;
  private static String KEY_SUCCESS = "success";
  DatabaseHandler db;
  @Override
  public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.rateme);
 
	addListenerOnRatingBar();
	txtFeedback = (EditText) findViewById(R.id.feedback);
	txtFeedback.addTextChangedListener(new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        	ratingChanged = true;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           
        }

        @Override
        public void afterTextChanged(Editable s) {

           
        }

		
    });
	addListenerOnButton();
	//get user_id from saved preferences
	String uid = getPreferences("USER_ID");
	db = new DatabaseHandler(getApplicationContext());
	HashMap<String,String> ratingDetails = db.getRating(uid);
	if(!ratingDetails.isEmpty() && ratingDetails.get("rating")!=null){
	ratingBar.setRating(Float.parseFloat(ratingDetails.get("rating")));
	if(ratingDetails.get("feedback")!=null){
	txtFeedback.setText(ratingDetails.get("feedback"));
	}
	else{
		txtFeedback.setText("");
	}
	}
	else{
		ratingBar.setRating(4);
		txtFeedback.setText("");
	}
 
  }
  
  public String getPreferences(String key)
  {
      SharedPreferences prefs = getApplicationContext().getSharedPreferences(getApplicationContext().getPackageName(), 0);
      return prefs.getString(key, "");
  }

 
  public void addListenerOnRatingBar() {
 
	ratingBar = (RatingBar) findViewById(R.id.ratingBar);
	
 
	
	ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
		public void onRatingChanged(RatingBar ratingBar, float rating,
			boolean fromUser) {
 
			ratingChanged = true;
			
 
		}
	});

  }
 
  public void addListenerOnButton() {
 
	ratingBar = (RatingBar) findViewById(R.id.ratingBar);
	btnSubmit = (Button) findViewById(R.id.btnSubmit);
	btnbktomenu = (Button) findViewById(R.id.btnbktomenu);

	btnSubmit.setOnClickListener(new OnClickListener() {
 
		@Override
		public void onClick(View v) {
 
			//check if rating and/or feedback was changed
			if(ratingChanged){
				//save the new rating to the mysql db
				NetAsync();
			}else{
				 Toast.makeText(RateMe.this, 
           			  "Please change either the rating or the feedback and then click on Submit!", Toast.LENGTH_SHORT).show();
			}
 
		}
 
	});
	
	btnbktomenu.setOnClickListener(new OnClickListener() {
		 
		@Override
		public void onClick(View v) {
			Intent myIntent = new Intent(getApplicationContext(), Main.class);
             startActivityForResult(myIntent, 0);
             finish();
		}
 
	});
 
  }
  
  
  
  private class NetCheck extends AsyncTask<String,String,Boolean>
	{
	 
	
	    @Override
	    protected void onPreExecute(){
	        super.onPreExecute();
	    }
	
	    @Override
	    protected Boolean doInBackground(String... args){


/**
* Gets current device state and checks for working internet connection by trying Google.
**/
          ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
          NetworkInfo netInfo = cm.getActiveNetworkInfo();
          if (netInfo != null && netInfo.isConnected()) {
              try {
                  URL url = new URL("http://www.google.com");
                  HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                  urlc.setConnectTimeout(3000);
                  urlc.connect();
                  if (urlc.getResponseCode() == 200) {
                      return true;
                  }
              } catch (MalformedURLException e1) {
                  e1.printStackTrace();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
          return false;

      }
	    @Override
	    protected void onPostExecute(Boolean th){
	
	        if(th == true){
	            new ProcessLogin().execute();
	        }
	        else{
             
	        	 Toast.makeText(getApplicationContext(), "Network not connected", Toast.LENGTH_SHORT).show();
          }
	    }
	}
	
	
  private class ProcessLogin extends AsyncTask<String, String, JSONObject> {

	  String rating;
	  String feedValue;
	  String uid;


      @Override
      protected void onPreExecute() {
          super.onPreExecute();
          rating = String.valueOf(ratingBar.getRating());
          feedValue = txtFeedback.getText().toString();
          uid = getPreferences("USER_ID");
          
         
      }

      @Override
      protected JSONObject doInBackground(String... args) {
      	UserFunctions userFunction = new UserFunctions();
      	JSONObject json = userFunction.updateRating(rating, uid, feedValue);
      	return json;
         
      }




      
      @Override
      protected void onPostExecute(JSONObject json) {


          try {
              if (json.getString(KEY_SUCCESS) != null) {
                  String res = json.getString(KEY_SUCCESS);
               


                  if (Integer.parseInt(res) != 1) {
                     //a problem occurred
                	  Toast.makeText(RateMe.this, 
                			  "A problem occurred while updating the rating. Please try again!", Toast.LENGTH_SHORT).show();
                  }
                  else{
                	  db.resetRating();
                	  db.setRating(uid, rating, feedValue);
                	  Toast.makeText(RateMe.this, 
                			  "Thank you! Your feedback is important to us!", Toast.LENGTH_SHORT).show();
                  }


              }
          } catch (JSONException e) {
              e.printStackTrace();


          }

      
    	  
    	  
      }
  }
  public void NetAsync(){
      new NetCheck().execute();
  } 
  

}