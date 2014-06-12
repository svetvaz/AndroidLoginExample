package com.svetlana.loginapp;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.svetlana.library.SQLController;
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.WriterException;


public class Reward extends Activity {
	TableLayout table_layout;

	SQLController sqlcon;

	ProgressDialog PD;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rewardlist);

		sqlcon = new SQLController(this);

		table_layout = (TableLayout) findViewById(R.id.tableLayout1);

		BuildTable();
		
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
		sqlcon.close();
	}


}
