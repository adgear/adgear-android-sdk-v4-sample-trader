package com.adgear.adgearconsolenativeadsample;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adgear.consolead.AGConsoleAdModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

public class CustomNativeAd  extends RelativeLayout {

    private ImageView imageView;
    private TextView header;
    private TextView description;
    private Button button;

    private AGConsoleAdModel adModel;


    public CustomNativeAd(Context context) {
        super(context);
        init();
    }

    public CustomNativeAd(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomNativeAd(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setAdModel(final AGConsoleAdModel adModel) {
        this.adModel = adModel;
        if (this.adModel != null) {
            this.header.setText(this.adModel.getVariable("title"));
            this.description.setText(this.adModel.getVariable("description"));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL fileUrl = adModel.getFileUrl("logo");
                        InputStream inputStream = null;
                        if (fileUrl.getProtocol().startsWith("http")) {
                            inputStream = fileUrl.openStream();
                        }
                        else if (fileUrl.getProtocol().startsWith("file")) {
                            File file = new File(fileUrl.getPath());
                            inputStream = new FileInputStream(file);
                        }
                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageBitmap(bitmap);
                                imageView.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                    catch (Exception e) {
                        Log.e("!", "download image exception: " + e.getMessage());
                    }
                }
            }).start();
        }
    }

    private void init() {
        inflate(getContext(), R.layout.custom_native_ad, this);
        this.imageView = (ImageView)findViewById(R.id.imageView);
        this.imageView.setVisibility(View.GONE);
        this.header = (TextView)findViewById(R.id.ad_title);
        this.description = (TextView)findViewById(R.id.ad_description);
        this.button = (Button)findViewById(R.id.button);
        this.button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adModel != null) {
                    try {
                        String clickUrlString = adModel.getClickUrlString("site");
                        if (clickUrlString == null) {
                            throw new Exception("Could not get click tag url");
                        }
                        Uri uri = Uri.parse(clickUrlString);
                        if (uri == null) {
                            throw new Exception("Could not parse click tag url");
                        }
                        getContext().startActivity(new Intent(Intent.ACTION_VIEW, uri));
                    }
                    catch (Exception e) {
                        Log.e("!", "Ad button clicked ERROR: " + e.getMessage());
                    }
                }
            }
        });
    }

}