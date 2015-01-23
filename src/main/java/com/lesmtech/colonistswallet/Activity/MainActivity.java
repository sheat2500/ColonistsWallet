package com.lesmtech.colonistswallet.Activity;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lesmtech.colonistswallet.R;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity {


    private final int NUM_PAGES = 3;

    ActionBar mActionBar;

    SharedPreferences sharedPreferences;
    boolean firstOpen;

    ViewPager mViewPager;
    PagerAdapter mPagerAdapter;

    // Main Content Container
    FrameLayout mFrameLayout;

    DashBoardFragment mDashBoardFragment;
    RecordFragment mRecordFragment;


    private final int DASHBOARDFRAGMENT = 0;
    private final int RECORDFRAGMENT = 1;
    private final int NUM_FRAGMENT = 2;

    Fragment[] Fragments;

    FragmentManager fragmentManager;

    // ListView in SlidingMenu
    ListView listView_menu;
    ArrayAdapter<String> mArrayAdapter;
    String[] mString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initial view
        initNormalView();

        // Check whether it's the first time open the app
        checkSharedPreferences();

        if (firstOpen) {
            initWelcomeView();
            mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
            mViewPager.setAdapter(mPagerAdapter);
        } else {
            initViewStatu();
        }
        Toast.makeText(getApplicationContext(), String.valueOf(firstOpen), Toast.LENGTH_SHORT).show();
    }

    private void initNormalView() {
        mActionBar = getActionBar();
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mFrameLayout = (FrameLayout) findViewById(R.id.main_content);
        fragmentManager = getSupportFragmentManager();
        mDashBoardFragment = (DashBoardFragment) fragmentManager.findFragmentById(R.id.dashboardfragment);
        mRecordFragment = (RecordFragment) fragmentManager.findFragmentById(R.id.recordfragment);
        Fragments = new Fragment[]{mDashBoardFragment, mRecordFragment};
        showThisFragment(DASHBOARDFRAGMENT);
    }

    private void showThisFragment(int fragment) {

        for (int i = 0; i < NUM_FRAGMENT; i++) {
            fragmentManager.beginTransaction().hide(Fragments[i]).commit();
        }
        fragmentManager.beginTransaction().show(Fragments[fragment]).commit();

    }

    private void initWelcomeView() {
        // Hide actionBar
        mActionBar.hide();
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
    }

    private void initViewStatu() {
        mViewPager.setVisibility(View.INVISIBLE);
        ((LinearLayout) mViewPager.getParent()).removeView(mViewPager);
        mFrameLayout.setVisibility(View.VISIBLE);

        SlidingMenu menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.slidingmenu_shadow_width);
        //menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        menu.setMenu(R.layout.slidingmenu);

        listView_menu = (ListView) findViewById(R.id.list_item);
        mString = getResources().getStringArray(R.array.slidingmenu_items);
        mArrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_2, mString);

        listView_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), position, Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void checkSharedPreferences() {
        sharedPreferences = getSharedPreferences("ColonistPreference", MODE_WORLD_WRITEABLE);

        // firstOpen?
        firstOpen = sharedPreferences.getBoolean("FirstOpen", true);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("FirstOpen", false);
        editor.commit();

        // May get other colonists' preferences

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class WelcomeAlphaFragment extends Fragment {

        public WelcomeAlphaFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_alpha, container, false);
            return rootView;
        }
    }

    public static class WelcomeBetaFragment extends Fragment {

        public WelcomeBetaFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_beta, container, false);
            return rootView;
        }
    }

    public static class WelcomeGammaFragment extends Fragment {

        public WelcomeGammaFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_gamma, container, false);
            return rootView;
        }
    }

    public static class DashBoardFragment extends Fragment {
        public DashBoardFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_dashboard, container, false);
            return v;
        }
    }

    public static class RecordFragment extends Fragment {
        public RecordFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_record, container, false);
            return v;
        }
    }

    // ZoomOutPageTransformer Animation for welcomePage

    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {

        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

    // Customize ScreenSlidePager Adapter

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            if (position == 0) {
                return new WelcomeAlphaFragment();
            } else if (position == 1) {
                return new WelcomeBetaFragment();
            } else
                return new WelcomeGammaFragment();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

}


