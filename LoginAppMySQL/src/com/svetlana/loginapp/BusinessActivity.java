package com.svetlana.loginapp;



import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import com.svetlana.library.SQLController;
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.WriterException;


public class BusinessActivity extends Activity {
	TableLayout table_layout;
	EditText businessname_et;
	Button addbusiness_btn;
	TextView businessAddErrorMsg;

	SQLController sqlcon;

	ProgressDialog PD;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.business);

		sqlcon = new SQLController(this);

		businessname_et = (EditText) findViewById(R.id.business_name_id);
		addbusiness_btn = (Button) findViewById(R.id.addbusiness_btn_id);
		businessAddErrorMsg = (TextView) findViewById(R.id.business_add_error);
//		table_layout = (TableLayout) findViewById(R.id.tableLayout1);

//		BuildTable();

		addbusiness_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new MyAsync().execute();

			}
		});

	}

//	private void BuildTable() {
//		sqlcon.open();
//		Cursor c = sqlcon.readBusinessEntry();
//
//		int rows = c.getCount();
//		int cols = c.getColumnCount();
//
//		c.moveToFirst();
//
//		// outer for loop
//		for (int i = 0; i < rows; i++) {
//
//			TableRow row = new TableRow(this);
//			row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
//					LayoutParams.WRAP_CONTENT));
//
//			// inner for loop
//			for (int j = 0; j < cols; j++) {
//
//				TextView tv = new TextView(this);
//				tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
//						LayoutParams.WRAP_CONTENT));
//				tv.setGravity(Gravity.CENTER);
//				tv.setTextSize(18);
//				tv.setPadding(0, 5, 0, 5);
//
//				tv.setText(c.getString(j));
//
//				row.addView(tv);
//
//			}
//
//			c.moveToNext();
//
//			table_layout.addView(row);
//
//		}
//		sqlcon.close();
//	}

	private class MyAsync extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			
			// TODO Auto-generated method stub
			super.onPreExecute();
			businessAddErrorMsg.setText("");
//			table_layout.removeAllViews();

			PD = new ProgressDialog(BusinessActivity.this);
			PD.setTitle("Please Wait..");
			PD.setMessage("Loading...");
			PD.setCancelable(false);
			PD.show();
		}

		@Override
		protected Void doInBackground(Void... params) {

			String businessname = businessname_et.getText().toString();
			sqlcon.open();
			long inserted = sqlcon.insertBusiness(businessname);
			if(inserted==-1){
				businessAddErrorMsg.setText("Business already exists!");
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
//			BuildTable();
			businessAddErrorMsg.setText("Successfully added business");
			PD.dismiss();
		}
	}

}
