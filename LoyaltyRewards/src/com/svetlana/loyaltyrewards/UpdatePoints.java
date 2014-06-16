package com.svetlana.loyaltyrewards;



import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.svetlana.library.DatabaseHandler;
import com.svetlana.library.UserFunctions;
import com.svetlana.loyaltyrewards.R;

public class UpdatePoints extends Activity {

    private static String KEY_SUCCESS = "success";
    EditText newpoints;
    Button updatepoints;
    Button bktomenu;
    TextView alert;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.updatepoints);
        newpoints = (EditText) findViewById(R.id.newpoints);
        alert = (TextView) findViewById(R.id.alertpass);
        updatepoints = (Button) findViewById(R.id.btupdatepoints);
        bktomenu= (Button) findViewById(R.id.btbktomenu);

    
    updatepoints.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            NetAsync(view);
        }
    });
    bktomenu.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent myIntent = new Intent(view.getContext(), Main.class);
            startActivityForResult(myIntent, 0);
            finish();
        
        }
    });
    }


    private class NetCheck extends AsyncTask<String,String,Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(UpdatePoints.this);
            nDialog.setMessage("Updating points..");
            nDialog.setTitle("Loyalty Points");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args){
            return true;
        }
        @Override
        protected void onPostExecute(Boolean th){

            if(th == true){
                nDialog.dismiss();
                new ProcessRegister().execute();
            }
            else{
                nDialog.dismiss();
                alert.setText("Error in Network Connection");
            }
        }
    }

    private class ProcessRegister extends AsyncTask<String, String, JSONObject> {


        private ProgressDialog pDialog;

        String newpoint,uid,email,businessname;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            DatabaseHandler db = new DatabaseHandler(getApplicationContext());

            HashMap<String,String> user = new HashMap<String, String>();
            user = db.getUserDetails();

            uid = user.get("uid");
            email = user.get("email");
            businessname = "Starbucks";
            newpoint = newpoints.getText().toString();
            

            pDialog = new ProgressDialog(UpdatePoints.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {


            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.updatePoints(newpoint, uid,businessname,email);
            Log.d("Button", "Changing points");
            return json;


        }


        @Override
        protected void onPostExecute(JSONObject json) {

            try {
                if (json.getString(KEY_SUCCESS) != null) {
                    alert.setText("");
                    String res = json.getString(KEY_SUCCESS);
                 


                    if (Integer.parseInt(res) == 1) {
                        /**
                         * Dismiss the process dialog
                         **/
                        pDialog.dismiss();
                        alert.setText("Your Points were successfully changed.");


                    } else {
                        pDialog.dismiss();
                        alert.setText("Error occured while updating points. Please try again");
                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();


            }

        }}
    public void NetAsync(View view){
        new NetCheck().execute();
    }
    


}
