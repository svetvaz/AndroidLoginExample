package com.svetlana.loginapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.svetlana.library.UserFunctions;

public class Main extends Activity {
    Button btnLogout;
    Button changepas;
    Button updatepoints;
    Button viewrewards;
    Button generateqrcode;
    

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        changepas = (Button) findViewById(R.id.btchangepass);
        updatepoints = (Button) findViewById(R.id.btupdatepoints);
        btnLogout = (Button) findViewById(R.id.logout);
        viewrewards = (Button) findViewById(R.id.btviewrewards);
        generateqrcode = (Button) findViewById(R.id.generateqrcode);
//        addPreferencesFromResource(R.layout.preferences);  
//        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        /**
         * Hashmap to load data from the Sqlite database
         **/
 
//         us db.getUserDetails();


        changepas.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){

                Intent chgpass = new Intent(getApplicationContext(), ChangePassword.class);

                startActivity(chgpass);
            }

        });
        
        

        updatepoints.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){

                Intent updatePoint = new Intent(getApplicationContext(), UpdatePoints.class);

                startActivity(updatePoint);
            }

        });
        
        
        viewrewards.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){

                Intent viewrew = new Intent(getApplicationContext(), Reward.class);

                startActivity(viewrew);
            }

        });
        
        
        
        generateqrcode.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){

                Intent generateQrCode = new Intent(getApplicationContext(), GenerateQRCodeActivity.class);

                startActivity(generateQrCode);
            }

        });

       /**
        *Logout from the User Panel which clears the data in Sqlite database
        **/
        btnLogout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                UserFunctions logout = new UserFunctions();
                logout.logoutUser(getApplicationContext());
                Intent login = new Intent(getApplicationContext(), Login.class);
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(login);
                finish();
            }
        });
/**
 * Sets user first name and last name in text view.
 **/
        


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
              Toast.makeText(this, "You have chosen the " + getResources().getString(R.string.About) + " menu option",
                          Toast.LENGTH_SHORT).show();
              return true;
        default:
              return super.onOptionsItemSelected(item);
        }
        
    }

    
//    public static String Read(Context context, final String key) {
//        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
//        return pref.getString(key, "");
//    }
// 
//    public static void Write(Context context, final String key, final String value) {
//          SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
//          SharedPreferences.Editor editor = settings.edit();
//          editor.putString(key, value);
//          editor.commit();        
//    }
//     
//    // Boolean  
//    public static boolean ReadBoolean(Context context, final String key, final boolean defaultValue) {
//        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
//        return settings.getBoolean(key, defaultValue);
//    }
// 
//    public static void WriteBoolean(Context context, final String key, final boolean value) {
//          SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
//          SharedPreferences.Editor editor = settings.edit();
//          editor.putBoolean(key, value);
//          editor.commit();        
//    }


}