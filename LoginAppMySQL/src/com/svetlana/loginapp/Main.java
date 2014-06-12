package com.svetlana.loginapp;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.svetlana.library.UserFunctions;

public class Main extends Activity {
    Button btnLogout;
    Button changepas;
    Button viewrewards;
    Button generateqrcode;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        changepas = (Button) findViewById(R.id.btchangepass);
        btnLogout = (Button) findViewById(R.id.logout);
        viewrewards = (Button) findViewById(R.id.btviewrewards);
        generateqrcode = (Button) findViewById(R.id.generateqrcode);

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
        


    }}