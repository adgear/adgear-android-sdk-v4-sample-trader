package com.adgear.adgeartradersample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.adgear.eventmanager.AGECompletionHandler;
import com.adgear.eventmanager.AGEContext;
import com.adgear.spotview.AGSpotView;
import com.adgear.spotview.AGSpotViewAd;
import com.adgear.spotview.AGTraderAdFactory;
import com.adgear.traderad.AGTraderAdModel;

public class MainActivity extends AppCompatActivity {

    private AGSpotView spotView;

    private final static String APP_TAG_ID = "APP_TAG_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize AGEventManager context.
        // Must be initialized before any interaction with AdGear SDK.
        AGEContext.init(this);

        // Get layout to add a spot view to.
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.activityMainLayout);

        // Create spot view and add it to the layout
        this.spotView = new AGSpotView(this);
        layout.addView(this.spotView);
        RelativeLayout.LayoutParams layoutParamsSpot = (RelativeLayout.LayoutParams) this.spotView.getLayoutParams();
        layoutParamsSpot.addRule(RelativeLayout.CENTER_IN_PARENT);

        // Request and make a spot view ad asynchronously (with appTagId, with, and height)
        AGTraderAdFactory.makeSpotViewAdWithAppTagId(APP_TAG_ID, 300, 250, new AGECompletionHandler<AGSpotViewAd<AGTraderAdModel>, Void>() {
            @Override
            public void completed(AGSpotViewAd<AGTraderAdModel> ad, Void aVoid, Exception exception) {

                // Check for errors
                if (exception != null) {
                    showError(exception);
                    return;
                }

                // Set spot view size and load the ad.
                spotView.setSizeDip(ad.getModel().getWidth(), ad.getModel().getHeight());
                spotView.loadAd(ad);
            }
        });
    }

    private void showError(Exception exception) {
        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
    }
}
