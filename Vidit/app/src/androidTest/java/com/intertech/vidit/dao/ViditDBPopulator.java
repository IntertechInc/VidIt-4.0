package com.intertech.vidit.dao;

import android.test.AndroidTestCase;
import android.util.Log;

import com.intertech.vidit.domain.FavoriteItem;

import java.util.List;

import static com.intertech.vidit.ViditTestDataSupport.TEST_ARTIST;
import static com.intertech.vidit.ViditTestDataSupport.TEST_DESCRIPTION;
import static com.intertech.vidit.ViditTestDataSupport.TEST_ID;
import static com.intertech.vidit.ViditTestDataSupport.TEST_THUMBNAIL;
import static com.intertech.vidit.ViditTestDataSupport.TEST_TITLE;
import static com.intertech.vidit.ViditTestDataSupport.TEST_URI;
import static com.intertech.vidit.ViditTestDataSupport.TEST_VIDEO_TITLE;
import static com.intertech.vidit.support.ViditApplication.TEST_LOG_TAG;

public class ViditDBPopulator extends AndroidTestCase {

    private static final boolean createDuringTesting = true;
    private static final boolean destroyTestData = false;

//    public void testCreateAndGetFavorites() {
//        if (createDuringTesting) {
//            ViditDBAdapter adapter = new ViditDBAdapter(getContext());
//            adapter.open();
//            for (int i = 0; i < TEST_ARTIST.length; i++) {
//                long favorite_id = adapter.createFavorite(TEST_ARTIST[i], TEST_TITLE[i], TEST_URI[i], TEST_DESCRIPTION[i], TEST_VIDEO_TITLE[i], TEST_ID[i], TEST_THUMBNAIL[i]);
//                Log.d(TEST_LOG_TAG, "--->" + favorite_id);
//            }
//            adapter.close();
//        }
//        Log.i(TEST_LOG_TAG, "Test data created");
//    }

//    public void testCleanTestData() {
//        if (destroyTestData) {
//            ViditDBAdapter adapter = new ViditDBAdapter(getContext());
//            adapter.open();
//            List<FavoriteItem> favs = adapter.getFavorites();
//            Log.d(TEST_LOG_TAG, "Current # of Favorites:  " + favs.size());
//            for (FavoriteItem fav : favs) {
//                adapter.deleteFavorite(fav.getId());
//            }
//            assertEquals(0, adapter.getFavorites().size());
//            adapter.close();
//        }
//        Log.d(TEST_LOG_TAG, "Test data destroyed");
//    }
}
