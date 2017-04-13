package com.adgear.adgearconsolesample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.adgear.consolead.AGConsoleAdModel;
import com.adgear.eventmanager.AGECompletionHandler;
import com.adgear.eventmanager.AGEContext;
import com.adgear.spotview.AGConsoleAdFactory;
import com.adgear.spotview.AGSpotView;
import com.adgear.spotview.AGSpotViewAd;

public class MainActivity extends AppCompatActivity {

    private AGSpotView spotView;

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

        AGConsoleAdFactory.makeSpotViewAdWithSpotId("8810278", null, new AGECompletionHandler<AGSpotViewAd<AGConsoleAdModel>, Void>() {
            @Override
            public void completed(AGSpotViewAd<AGConsoleAdModel> spotViewAd, Void aVoid, Exception exception) {

                // Check for errors
                if (exception != null) {
                    showError(exception);
                    return;
                }

                // Set spot view size and load the ad.
                spotView.setSizeDip(336, 280);
                spotView.loadAd(spotViewAd);
            }
        });
    }

    private void showError(Exception exception) {
        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
    }
}
