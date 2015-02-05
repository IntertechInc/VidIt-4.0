package com.intertech.vidit.activity;


import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

@LargeTest
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();
    }

    public void testOpenWithNoFavorites() {
        // should show toast
//        onView(withText("Vidit")).check(matches(isDisplayed()));
//        onView(ViewMatchers.withText(R.string.toast_no_favorites)).check(matches(isDisplayed()));
    }

//    public void testOpenWithFavorites(){
//        // should show favorites in recycler view
//        onView(withText("Vidit")).check(matches(isDisplayed()));
//    }

    // TODO - tests to complete
    // test play video
    // test play video with no player
    // test play video with no selected
    // test play of video with bad url - should get toast
    // test play video without network - in separate test class


}
