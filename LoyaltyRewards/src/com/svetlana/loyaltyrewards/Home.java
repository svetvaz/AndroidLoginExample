package com.svetlana.loyaltyrewards;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("NewApi")
public class Home extends Activity{
	Timer timer;
	TimerTask myTimerTask;
	private TextView welcomeText;
	 
	ImageView myImage;
    int counter=0;
	 
   
	
 @Override
 public void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.home);
//  if(timer != null){
//	     timer.cancel();
//	    }
//  
//  timer = new Timer();
//  class RemindTask extends TimerTask {
//	   
//      int[] arrImageResources = {R.drawable.shop,R.drawable.coins,R.drawable.shop};
//
//      public void run() {
//       	  myImage.setImageResource(arrImageResources[counter]);
//             if(counter==2){
//           	  counter=0;
//             }else{
//             counter++;
//             }
//              
//          
//       }
//   }
//  timer.schedule(new RemindTask(),0,3*1000);
  myImage = (ImageView) findViewById(R.id.imageViewMain);
    
    myImage.setImageResource(R.drawable.shop);
    welcomeText = (TextView) findViewById(R.id.welcomeText);
  //retrieve welcome msg from shared preferences
  SharedPreferences sharedPrefs = PreferenceManager
          .getDefaultSharedPreferences(this);
  String welcomeMsg = sharedPrefs.getString("welcome_message", "Welcome to Loyalty Rewards");
  String displayusername = sharedPrefs.getString("display_user", "customer");
  welcomeText.setText(welcomeMsg+' '+displayusername);
  
 }
 @Override
	protected void onResume() {
		super.onResume();
		
	}
	
	@Override
	protected void onPause() {
		  if(timer != null){
			     timer.cancel();
			    }
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		  if(timer != null){
			     timer.cancel();
			    }
		super.onDestroy();
	
	}
 
}