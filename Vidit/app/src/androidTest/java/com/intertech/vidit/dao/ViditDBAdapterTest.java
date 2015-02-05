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

public class ViditDBAdapterTest extends AndroidTestCase {
    /**
     * Test happy path of creating new FavoriteItem.  Deleted to keep database empty of test data
     */
    public void testCreateAndGetFavorites() {
        ViditDBAdapter adapter = null;
        try {
            adapter = new ViditDBAdapter(getContext());
            adapter.open();
            long favorite_id = adapter.createFavorite(TEST_ARTIST[0], TEST_TITLE[0], TEST_URI[0], TEST_DESCRIPTION[0], TEST_VIDEO_TITLE[0], TEST_ID[0], TEST_THUMBNAIL[0]);
            Log.d(TEST_LOG_TAG, "----->" + favorite_id);
            assertTrue(favorite_id > 0);
            List<FavoriteItem> items = adapter.getFavorites();
            for (FavoriteItem item : items) {  // should only be one item found
                if (item.getId() == favorite_id) {
                    assertEquals(TEST_ARTIST[0], item.getArtist());
                    assertEquals(TEST_TITLE[0], item.getTitle());
                    assertEquals(TEST_URI[0], item.getUri());
                    assertEquals(TEST_DESCRIPTION[0], item.getDescription());
                    assertEquals(TEST_ID[0], item.getVideoId());
                    assertEquals(TEST_THUMBNAIL[0], item.getThumbnail());
                    assertTrue(adapter.deleteFavorite((int) favorite_id));
                    adapter.close();
                    return;
                }
            }
            fail("Adapter was unable to find favorite item after saving");
        } finally {
            if (adapter != null) {
                adapter.close();
            }
        }
    }

    /**
     * Test attempt to create favorite with required column data not provided.
     */
    public void testNullFieldFailure() {
        ViditDBAdapter adapter = null;
        try {
            adapter = new ViditDBAdapter(getContext());
            adapter.open();
            adapter.createFavorite(null, null, null, TEST_DESCRIPTION[0], TEST_VIDEO_TITLE[0], TEST_ID[0], TEST_THUMBNAIL[0]);
            fail("Create should have failed because of null data in required columns");
        } catch (IllegalArgumentException exception) {
            // successful test
        } finally {
            if (adapter != null) {
                adapter.close();
            }
        }
    }

    /**
     * Test check of duplicate entry into database.
     */
    public void testIsDuplicateEntry() {
        ViditDBAdapter adapter = new ViditDBAdapter(getContext());
        adapter.open();
        long favorite_id = adapter.createFavorite(TEST_ARTIST[0], TEST_TITLE[0], TEST_URI[0], TEST_DESCRIPTION[0], TEST_VIDEO_TITLE[0], TEST_ID[0], TEST_THUMBNAIL[0]);
        Log.d(TEST_LOG_TAG, "----->" + favorite_id);
        assertTrue(favorite_id > 0);
        assertTrue(adapter.isDuplicateFavorite(TEST_ARTIST[0], TEST_TITLE[0], TEST_ID[0]));
        assertTrue(adapter.deleteFavorite((int) favorite_id));
        adapter.close();
    }

    /**
     * Test attempt to create favorite with duplicate data.  Should be rejected.
     */
    public void testDuplicateCreateRejection() {
        ViditDBAdapter adapter = new ViditDBAdapter(getContext());
        adapter.open();
        long favorite_id = adapter.createFavorite(TEST_ARTIST[0], TEST_TITLE[0], TEST_URI[0], TEST_DESCRIPTION[0], TEST_VIDEO_TITLE[0], TEST_ID[0], TEST_THUMBNAIL[0]);
        Log.d(TEST_LOG_TAG, "----->" + favorite_id);
        assertTrue(favorite_id > 0);
        long failed_id = adapter.createFavorite(TEST_ARTIST[0], TEST_TITLE[0], TEST_URI[0], TEST_DESCRIPTION[0], TEST_VIDEO_TITLE[0], TEST_ID[0], TEST_THUMBNAIL[0]);
        assertEquals(-1, failed_id);
        assertTrue(adapter.deleteFavorite((int) favorite_id));
        adapter.close();
    }

}
