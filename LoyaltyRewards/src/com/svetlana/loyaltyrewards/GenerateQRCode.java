package com.svetlana.loyaltyrewards;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.svetlana.library.QRCodeEncoder;
import com.svetlana.loyaltyrewards.R;

@SuppressLint("NewApi")
public class GenerateQRCode extends Activity{


 @Override
 public void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.qrcode);


   //Find screen size
  WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
  Display display = manager.getDefaultDisplay();
   Point point = new Point();
   display.getSize(point);
   int width = point.x;
   int height = point.y;
   int smallerDimension = width < height ? width : height;
   smallerDimension = smallerDimension * 3/4;
   SharedPreferences prefs = getSharedPreferences(getPackageName(), 0);
   String userId = prefs.getString("USER_ID", "");
   //Encode with a QR Code image
   QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(userId, 
             null, 
             null,  
             BarcodeFormat.QR_CODE.toString(), 
             smallerDimension);
   try {
    Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
    ImageView myImage = (ImageView) findViewById(R.id.imageView1);
    myImage.setImageBitmap(bitmap);
    registerForContextMenu(myImage);

   } catch (WriterException e) {
    e.printStackTrace();
   }

 }
 
 @Override 
 public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
 {
         super.onCreateContextMenu(menu, v, menuInfo);
         menu.setHeaderTitle("Select The Action");  
         menu.add(0, v.getId(), 0, "Back Home");  

 } 
	
	
	@Override  
 public boolean onContextItemSelected(MenuItem item)
 {  

                     if(item.getTitle()=="Back Home")
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
}