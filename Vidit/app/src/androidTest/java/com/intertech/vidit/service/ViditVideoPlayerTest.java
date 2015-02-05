package com.intertech.vidit.service;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import com.intertech.vidit.activity.MainActivity;
import com.intertech.vidit.domain.FavoriteItem;

import static com.intertech.vidit.ViditTestDataSupport.TEST_ARTIST;
import static com.intertech.vidit.ViditTestDataSupport.TEST_DESCRIPTION;
import static com.intertech.vidit.ViditTestDataSupport.TEST_ID;
import static com.intertech.vidit.ViditTestDataSupport.TEST_THUMBNAIL;
import static com.intertech.vidit.ViditTestDataSupport.TEST_TITLE;
import static com.intertech.vidit.ViditTestDataSupport.TEST_URI;
import static com.intertech.vidit.ViditTestDataSupport.TEST_VIDEO_TITLE;

@LargeTest
public class ViditVideoPlayerTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public ViditVideoPlayerTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    public void testPlayVideoWithNetwork() {
        ViditVideoPlayer player = new ViditVideoPlayer(getActivity());
        FavoriteItem favorite = new FavoriteItem(1, TEST_ARTIST[0], TEST_TITLE[0], TEST_URI[0], TEST_DESCRIPTION[0], TEST_VIDEO_TITLE[0], TEST_ID[0], TEST_THUMBNAIL[0]);
        assertTrue(player.playVideo(favorite));
    }
}
