package com.lesmtech.colonistswallet.Activity;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Outline;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lesmtech.colonistswallet.R;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.PieModel;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

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

    SlidingMenu mSlidingMenu;


    private final int DASHBOARDFRAGMENT = 0;
    private final int RECORDFRAGMENT = 1;
    private final int NUM_FRAGMENT = 2;

    Fragment[] Fragments;

    FragmentManager fragmentManager;

    // ListView in SlidingMenu
    ListView listView_menu;
    ArrayAdapter<String> mArrayAdapter;
    String[] mString;


    // RecordFragment View
    ListView recordfragment_listview;


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

        // Initial ActionBar with
        mActionBar = getActionBar();
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mSlidingMenu.showMenu();
    }

    private void initWelcomeView() {
        // Hide actionBar
        mActionBar.hide();
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
    }

    private void initViewStatu() {
        mViewPager.setVisibility(View.INVISIBLE);
        ((FrameLayout) mViewPager.getParent()).removeView(mViewPager);

        // clip the float button
        View addButton = findViewById(R.id.add_button);
        addButton.setClipToOutline(true);
        addButton.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                int diameter = getResources().getDimensionPixelSize(R.dimen.round_button_diameter);
                outline.setOval(0, 0, diameter, diameter);
            }
        });

        // Initial mSlidingMenu
        mSlidingMenu = new SlidingMenu(this);
        mSlidingMenu.setMode(SlidingMenu.LEFT);
        mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        mSlidingMenu.setShadowWidthRes(R.dimen.slidingmenu_shadow_width);
        //menu.setShadowDrawable(R.drawable.shadow);
        mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        mSlidingMenu.setFadeDegree(0.35f);
        mSlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        mSlidingMenu.setMenu(R.layout.slidingmenu);
        mSlidingMenu.showMenu();


//        listView_menu = (ListView) findViewById(R.id.list_item);
//        mString = getResources().getStringArray(R.array.slidingmenu_items);
//        mArrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_2, mString);

//        listView_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(), position, Toast.LENGTH_SHORT).show();
//            }
//        });


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            /**
             *  if(Login == true){
             *      SYNC Server Data
             *  }
             *  else
             *      Pop up LogIn Activity
             */
            case R.id.refresh:
                boolean login = ColonistsWalletApplication.getInstance().getLogIn();
                if (login == false) {
                    Intent account = new Intent(this, AccountActivity.class);
                    startActivity(account);
                } else {
                    // Refresh Data From Server


                }
                break;
            // Add any item you wanted, pop up a new want item activity
            case R.id.want:
                Intent want = new Intent(this, WantActivity.class);
                startActivity(want);
                break;
            // Add a bill, pop up a new purchase activity
            case R.id.add:
                Intent event = new Intent(this, EventActivity.class);
                startActivity(event);
                break;
            case android.R.id.home:
                mSlidingMenu.showMenu();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

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


    // DashBoardFragment!
    public static class DashBoardFragment extends Fragment {
        public DashBoardFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

            getActivity().getActionBar().setTitle("DashBoard");


            // LineChart
            ValueLineChart mCubicValueLineChart = (ValueLineChart) v.findViewById(R.id.cubiclinechart);

            ValueLineSeries series = new ValueLineSeries();
            series.setColor(0xFF56B7F1);

            series.addPoint(new ValueLinePoint("Jan", 2.4f));
            series.addPoint(new ValueLinePoint("Feb", 3.4f));
            series.addPoint(new ValueLinePoint("Mar", .4f));
            series.addPoint(new ValueLinePoint("Apr", 1.2f));
            series.addPoint(new ValueLinePoint("Mai", 2.6f));
            series.addPoint(new ValueLinePoint("Jun", 1.0f));
            series.addPoint(new ValueLinePoint("Jul", 3.5f));
            series.addPoint(new ValueLinePoint("Aug", 2.4f));
            series.addPoint(new ValueLinePoint("Sep", 2.4f));
            series.addPoint(new ValueLinePoint("Oct", 3.4f));
            series.addPoint(new ValueLinePoint("Nov", .4f));
            series.addPoint(new ValueLinePoint("Dec", 1.3f));

            mCubicValueLineChart.addSeries(series);
            mCubicValueLineChart.startAnimation();

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

            // PieChart
            PieChart mPieChart = (PieChart) v.findViewById(R.id.piechart);

            // Data render PieChart

            mPieChart.addPieSlice(new PieModel("Freetime", 15, Color.parseColor("#FE6DA8")));
            mPieChart.addPieSlice(new PieModel("Sleep", 25, Color.parseColor("#56B7F1")));
            mPieChart.addPieSlice(new PieModel("Work", 35, Color.parseColor("#CDA67F")));
            mPieChart.addPieSlice(new PieModel("Eating", 9, Color.parseColor("#FED70E")));
            mPieChart.startAnimation();

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

    // For Testing
    public void displayToast(String content) {
        Toast.makeText(getApplicationContext(), content, Toast.LENGTH_SHORT).show();
    }

}


