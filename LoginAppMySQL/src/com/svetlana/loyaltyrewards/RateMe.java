package com.svetlana.loyaltyrewards;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
 
public class RateMe extends Activity {
 
  private RatingBar ratingBar;
  private TextView txtRatingValue;
  private Button btnSubmit;
 
  @Override
  public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.rateme);
 
	addListenerOnRatingBar();
	addListenerOnButton();
 
  }
 
  public void addListenerOnRatingBar() {
 
	ratingBar = (RatingBar) findViewById(R.id.ratingBar);
	txtRatingValue = (TextView) findViewById(R.id.txtRatingValue);
 
	
	ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
		public void onRatingChanged(RatingBar ratingBar, float rating,
			boolean fromUser) {
 
			txtRatingValue.setText(String.valueOf(rating));
 
		}
	});
  }
 
  public void addListenerOnButton() {
 
	ratingBar = (RatingBar) findViewById(R.id.ratingBar);
	btnSubmit = (Button) findViewById(R.id.btnSubmit);
 

	btnSubmit.setOnClickListener(new OnClickListener() {
 
		@Override
		public void onClick(View v) {
 
			Toast.makeText(RateMe.this,
				String.valueOf(ratingBar.getRating()),
					Toast.LENGTH_SHORT).show();
 
		}
 
	});
 
  }
}