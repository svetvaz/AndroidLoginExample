package com.svetlana.loyaltyrewards;



import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.svetlana.library.DatabaseHandler;
import com.svetlana.library.SQLController;
import com.svetlana.library.UserFunctions;
import com.svetlana.loyaltyrewards.R;



public class Reward extends Activity {
	TableLayout table_layout;

	SQLController sqlcon;

	ProgressDialog PD;
    private static String KEY_SUCCESS = "success";
    private static String KEY_UID = "uid";
    private static String BUSINESS_NAME = "businessname";
    private static String POINTS = "pointvalues";
    private static String UPDATED_AT = "updated_at";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rewardlist);

		sqlcon = new SQLController(this);

		table_layout = (TableLayout) findViewById(R.id.tableLayout1);
		NetAsync();
		
		
		 Button backtoMenu = (Button) findViewById(R.id.bktomenu);
		 backtoMenu.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View view) {
	                Intent myIntent = new Intent(view.getContext(), Main.class);
	                startActivityForResult(myIntent, 0);
	                finish();
	            }

	        });


	}

	private void BuildTable() {
		sqlcon.open();
		Cursor c = sqlcon.readPoints();

		int rows = c.getCount();
		int cols = c.getColumnCount();

		c.moveToFirst();

		// outer for loop
		for (int i = 0; i < rows; i++) {

			TableRow row = new TableRow(this);
			row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));

			// inner for loop
			for (int j = 0; j < cols; j++) {

				TextView tv = new TextView(this);
				tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT));
				tv.setGravity(Gravity.CENTER);
				tv.setTextSize(18);
				tv.setPadding(0, 5, 0, 5);

				tv.setText(c.getString(j));

				row.addView(tv);

			}

			c.moveToNext();

			table_layout.addView(row);

		}
		
		registerForContextMenu(table_layout);
		sqlcon.close();
	}
	
	@Override 
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
    {
            super.onCreateContextMenu(menu, v, menuInfo);
            menu.setHeaderTitle("Select The Action");  
            menu.add(0, v.getId(), 0, "Go Back to Main Menu");  

    } 
	
	
	@Override  
    public boolean onContextItemSelected(MenuItem item)
    {  

                        if(item.getTitle()=="Go Back to Main Menu")
                        {
                        	Intent myIntent = new Intent(getApplicationContext(), Main.class);
        	                startActivityForResult(myIntent, 0);
        	                finish();
                        }                  
                        else 
                        {
                            return false;
                        }  
                        return true;  
                           
      }  
	
	
	 private class NetCheck extends AsyncTask<String,String,Boolean>
	    {
	        private ProgressDialog nDialog;

	        @Override
	        protected void onPreExecute(){
	            super.onPreExecute();
	            nDialog = new ProgressDialog(Reward.this);
	            nDialog.setMessage("Syncing..");
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
	               
	            }
	        }
	    }

	    private class ProcessRegister extends AsyncTask<String, String, JSONObject> {


	        private ProgressDialog pDialog;

	        String uid;
	        @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            DatabaseHandler db = new DatabaseHandler(getApplicationContext());

	            HashMap<String,String> user = new HashMap<String, String>();
	            user = db.getUserDetails();

	            uid = user.get("uid");
	            

	            pDialog = new ProgressDialog(Reward.this);
	            pDialog.setTitle("LoyaltyPoints");
	            pDialog.setMessage("Getting your rewards ...");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(true);
	            pDialog.show();
	        }

	        @Override
	        protected JSONObject doInBackground(String... args) {
	            UserFunctions userFunction = new UserFunctions();
	            JSONObject json = userFunction.syncdb(uid);
	            Log.d("Button", "Syncing DB");
	            return json;


	        }


	        @Override
	        protected void onPostExecute(JSONObject json) {
	        	
	            try {
	      
	                if (json.getString(KEY_SUCCESS) != null) {
	       
	                    String res = json.getString(KEY_SUCCESS);
	                 


	                    if (Integer.parseInt(res) == 1) {
	                    	 JSONArray points = json.getJSONArray("points");
	                    	 DatabaseHandler db = new DatabaseHandler(getApplicationContext());
	                         //first reset the points table
	                    	 db.resetPoints();
	                         for(int i=0;i<points.length();i++){
	                      	   JSONObject obj = (JSONObject)points.get(i);
	                      	   db.setPoints(obj.getString(KEY_UID),obj.getString(BUSINESS_NAME),obj.getString(POINTS),obj.getString(UPDATED_AT));
	                         }
	                         Toast.makeText(getApplicationContext(), "Sync DB complete", Toast.LENGTH_SHORT).show();
	                         BuildTable();
	                        pDialog.dismiss();
	               


	                    } else {
	                    	Toast.makeText(getApplicationContext(), "Problem while syncing. Go back to menu and re open your rewards page!", Toast.LENGTH_LONG).show();
	                        pDialog.dismiss();
	                      
	                    }


	                }
	            } catch (JSONException e) {
	                e.printStackTrace();


	            }

	        }}
	    public void NetAsync(){
	        new NetCheck().execute();
	    }



}
