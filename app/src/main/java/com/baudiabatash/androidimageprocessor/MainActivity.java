package com.baudiabatash.androidimageprocessor;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.baudiabatash.imageprocessor.ImageProcessor;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.baudiabatash.androidimageprocessor.R.id.processImage;

public class MainActivity extends AppCompatActivity {

    private ImageView ivProcess;

    private Observable<Bitmap> bitmapObservable;

    private ProgressDialog dialog;
    private ImageProcessor imageProcessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivProcess = (ImageView) findViewById(processImage);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait ...");
        dialog.setTitle("Inverting Image");


       /* Bitmap bitmap = ImageProcessor.invertBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.cup));

        ivProcess.setImageBitmap(bitmap);*/
    }

    public void invertImage(View view) {
        Log.d("UUUU","CAllF");
        Bitmap bitmap = ((BitmapDrawable)ivProcess.getDrawable()).getBitmap();

        doIt(bitmap);

        /*Bitmap bitmap = ImageProcessor.invertBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.cup));
        ivProcess.setImageBitmap(bitmap);*/
    }

    private void doIt(final Bitmap bitmap){
        dialog.show();
        bitmapObservable = Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                try {
                    Log.d("UUUU","CAll");
                    Bitmap data = ImageProcessor.applyReflection(bitmap);
                    subscriber.onNext(data); // Emit the contents of the URL
                    subscriber.onCompleted(); // Nothing more to emit
                } catch (Exception e) {
                    subscriber.onError(e);
                    Log.d("UUUU","CATCH");// In case there are network errors
                }

            }
        });

        bitmapObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        dialog.dismiss();
                        ivProcess.setImageBitmap(bitmap);
                    }
                });

    }


}
