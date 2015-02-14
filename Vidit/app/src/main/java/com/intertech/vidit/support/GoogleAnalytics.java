package com.intertech.vidit.support;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.intertech.vidit.R;

import java.util.HashMap;

/**
 * Provide support for google analytics initialization and calls
 *
 * Created by jwhite on 2/14/2015.
 */
public class GoogleAnalytics {

    static HashMap<String, String> screenNames;

    static {
        // Setup the friendly screen names
        screenNames = new HashMap<>();
        screenNames.put("FinderActivity", "Finder");
        screenNames.put("MainActivity", "Home");
        screenNames.put("AboutActivity", "About");
        screenNames.put("SettingsActivity", "Settings");
    }

    private static GoogleAnalytics instance = null;
    private Tracker tracker;
    ViditApplication app;

    private GoogleAnalytics() {
    }

    public static GoogleAnalytics getInstance() {
        if (instance == null) {
            instance = new GoogleAnalytics();
        }
        return instance;
    }

    /**
     * Initialize the tracker
     *
     * @param app The application instance
     */
    public void initTracker(ViditApplication app) {
        this.app = app;
        tracker = com.google.android.gms.analytics.GoogleAnalytics.getInstance(app).newTracker(R.xml.google_analytics);
    }

    /**
     * Send the screen view to GA
     *
     * @param className Class name for the current screen
     */
    public void sendScreenView(String className) {
        String screen = screenNames.get(className);
        if (screen != null) {
            // Add this screen to analytics
            tracker.setScreenName(screen);
            tracker.send(new HitBuilders.AppViewBuilder()
                    .build());

            // Also add an event that includes the appcode
            tracker.send(new HitBuilders.EventBuilder()
                    .setCategory(ViditApplication.APP_NAME)
                    .setAction("Screen View")
                    .setLabel(screen)
                    .build());
        }
    }
}
