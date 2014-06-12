package com.svetlana.loginapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.svetlana.library.QRCodeEncoder;

@SuppressLint("NewApi")
public class GenerateQRCodeActivity extends Activity{


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

   } catch (WriterException e) {
    e.printStackTrace();
   }

 }
}