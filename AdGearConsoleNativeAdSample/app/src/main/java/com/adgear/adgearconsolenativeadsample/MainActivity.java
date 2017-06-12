package com.adgear.adgearconsolenativeadsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.adgear.consolead.AGConsoleAdModel;
import com.adgear.consolead.AGConsoleAdModelRequest;
import com.adgear.eventmanager.AGECompletionHandler;
import com.adgear.eventmanager.AGEContext;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private CustomNativeAd nativeAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize AGEventManager context.
        // Must be initialized before any interaction with AdGear SDK.
        AGEContext.init(this);

        // Get layout to add a spot view to.
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.activityMainLayout);
        this.nativeAdView = new CustomNativeAd(this);
        layout.addView(nativeAdView);

        String spotId = "11705816";
        final int width = 320;
        final int height = 50;
        AGConsoleAdModelRequest.requestAdModel(AGConsoleAdModelRequest.makeUrlForSpotId(spotId, null), new AGECompletionHandler<AGConsoleAdModel, Void>() {
            @Override
            public void completed(AGConsoleAdModel adModel, Void aVoid, Exception exception) {

                // Check for errors.
                if (exception != null) {
                    showError(exception);
                    return;
                }

                // Setting ad model into the native ad view.
                nativeAdView.setAdModel(adModel);

                // Adjusting layout parameters
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) nativeAdView.getLayoutParams();
                layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, getResources().getDisplayMetrics());
                layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, getResources().getDisplayMetrics());

                // Registering impression.
                adModel.registerImpressionUrl(new AGECompletionHandler<Boolean, URL>() {
                    @Override
                    public void completed(Boolean aBoolean, URL url, Exception exception) {

                        // Register impression complete.

                        // Check for errors
                        if (exception != null) {
                            showError(exception);
                        }
                    }
                });
            }
        });
    }

    private void showError(Exception exception) {
        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
    }
}
