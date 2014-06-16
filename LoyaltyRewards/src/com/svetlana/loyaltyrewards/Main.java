package com.svetlana.loyaltyrewards;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;


import com.svetlana.library.UserFunctions;



public class Main extends TabActivity {

    private static final int RESULT_SETTINGS = 1;

	private TextView welcomeText;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        TabHost tabHost = getTabHost();
        
        // Tab for Viewing Home Page
        TabSpec homespec = tabHost.newTabSpec("");
        homespec.setIndicator("", getResources().getDrawable(R.drawable.icon_home_tab));
        Intent homeIntent = new Intent(this, Home.class);
        homespec.setContent(homeIntent);
        
        // Tab for Viewing Rewards
        TabSpec rewardspec = tabHost.newTabSpec("");
        rewardspec.setIndicator("", getResources().getDrawable(R.drawable.icon_rewards_tab));
        Intent rewardsIntent = new Intent(this, Reward.class);
        rewardspec.setContent(rewardsIntent);
        
        
        // Tab to Generate QRCode
        TabSpec qrcodespec = tabHost.newTabSpec("");
        qrcodespec.setIndicator("", getResources().getDrawable(R.drawable.icon_qrcode_tab));
        Intent qrcodeIntent = new Intent(this, GenerateQRCode.class);
        qrcodespec.setContent(qrcodeIntent);
        
     // Adding all TabSpec to TabHost
        tabHost.addTab(homespec); // Adding photos tab
        tabHost.addTab(rewardspec); // Adding songs tab
        tabHost.addTab(qrcodespec); // Adding videos tab
        
//        welcomeText = (TextView) findViewById(R.id.welcomeText);
        //retrieve welcome msg from shared preferences
//        SharedPreferences sharedPrefs = PreferenceManager
//                .getDefaultSharedPreferences(this);
//        String welcomeMsg = sharedPrefs.getString("welcome_message", "Welcome to Loyalty Rewards");
//        String displayusername = sharedPrefs.getString("display_user", "customer");
//        welcomeText.setText(welcomeMsg+' '+displayusername);
      

//        changepas = (Button) findViewById(R.id.btchangepass);
//        updatepoints = (Button) findViewById(R.id.btupdatepoints);
////        btnLogout = (Button) findViewById(R.id.logout);
//        viewrewards = (Button) findViewById(R.id.btviewrewards);
//        generateqrcode = (Button) findViewById(R.id.generateqrcode);
//


//        changepas.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View arg0){
//
//                Intent chgpass = new Intent(getApplicationContext(), ChangePassword.class);
//
//                startActivity(chgpass);
//            }
//
//        });
        
        
//
//        updatepoints.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View arg0){
//
//                Intent updatePoint = new Intent(getApplicationContext(), UpdatePoints.class);
//
//                startActivity(updatePoint);
//            }
//
//        });
        
//        
//        viewrewards.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View arg0){
//
//                Intent viewrew = new Intent(getApplicationContext(), Reward.class);
//
//                startActivity(viewrew);
//            }
//
//        });
        
        
        
//        generateqrcode.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View arg0){
//
//                Intent generateQrCode = new Intent(getApplicationContext(), GenerateQRCode.class);
//
//                startActivity(generateQrCode);
//            }
//
//        });
//        
   


//        btnLogout.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View arg0) {
//                UserFunctions logout = new UserFunctions();
//                logout.logoutUser(getApplicationContext());
//                Intent login = new Intent(getApplicationContext(), Login.class);
//                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(login);
//                finish();
//            }
//        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
      }
    
    
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.about:
        	 startActivity(new Intent(this, About.class));
             return true;
        case R.id.logout:
        	 UserFunctions logout = new UserFunctions();
             logout.logoutUser(getApplicationContext());
             Intent login = new Intent(getApplicationContext(), Login.class);
             login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
             startActivity(login);
             finish();
             return true;
        case R.id.settings:
        	Intent usersetting = new Intent(this, UserSettings.class);
            startActivityForResult(usersetting, RESULT_SETTINGS);
        	return true;
        case R.id.chngPswd:
            Intent chgpass = new Intent(getApplicationContext(), ChangePassword.class);
            startActivity(chgpass);
            return true;
        case R.id.rateus:
            Intent rateus = new Intent(getApplicationContext(), RateMe.class);
            startActivity(rateus);
            return true; 
            
        default:
              return super.onOptionsItemSelected(item);
        }
        
    }

    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
 
        switch (requestCode) {
        case RESULT_SETTINGS:
        	SharedPreferences sharedPrefs = PreferenceManager
            .getDefaultSharedPreferences(this);
        	String welcome = sharedPrefs.getString("welcome_message", "Welcome to Potful Loyalty Rewards");
        	String displayusername = sharedPrefs.getString("display_user", "customer");
        	Editor edit=sharedPrefs.edit();
        	edit.putString("welcome_message", welcome);
        	edit.putString("display_user", displayusername);
        	edit.commit();
        	Intent main = new Intent(getApplicationContext(), Main.class);
            startActivity(main);
            break;
//            welcomeText.setText(welcome+' '+displayusername);
//            break;
 
        }
 
    }

}