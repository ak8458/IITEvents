package iitevent.project.iit.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import iitevent.project.iit.helpers.Contents;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import iitevent.project.iit.helpers.QRCodeEncoder;

/**
 * Created by Akshay Patil on 20-11-2015.
 * @author Akshay Patil
 * This activity is loaded when the user creates and event, this activity generetes a QR code
 * with the event details embedded in it which can be snapshot and shared
 */
public class EventCreatedActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_reg_success);

        Intent eventRegSuccess=getIntent();
        String qrInput=eventRegSuccess.getStringExtra("qrInput");

        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3/4;

        //Encode with a QR Code image
        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(qrInput,
                null,
                Contents.Type.TEXT,
                BarcodeFormat.QR_CODE.toString(),
                smallerDimension);
        try {
            Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
            ImageView myImage = (ImageView) findViewById(R.id.eventQR);
            myImage.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }

    }
}
