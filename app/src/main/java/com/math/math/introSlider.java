package com.math.math;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

public class introSlider extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_slider);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_intro_slider, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_intro_slider, container, false);
            TextView textView = rootView.findViewById(R.id.section_label);

            ImageView zero = rootView.findViewById(R.id.intro_indicator_0);
            ImageView one = rootView.findViewById(R.id.intro_indicator_1);
            ImageView two = rootView.findViewById(R.id.intro_indicator_2);

            ImageButton finishButton = rootView.findViewById(R.id.finish_button);
            finishButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor = sharedPref.edit();

                    editor.putBoolean("is_user_first_time",false);
                    editor.apply();

                    Intent intent = new Intent(getContext(),MainActivity.class);
                    startActivity(intent);
                }
            });

            switch(Objects.requireNonNull(getArguments()).getInt(ARG_SECTION_NUMBER)){
                case 1: textView.setText(getString(R.string.intro_slide_text_1));
                zero.setBackgroundResource(R.drawable.indicator_selected);
                one.setBackgroundResource(R.drawable.indicator_not_selected);
                two.setBackgroundResource(R.drawable.indicator_not_selected);
                    finishButton.setVisibility(View.GONE);
                    break;
                case 2: textView.setText(getString(R.string.intro_slide_text_2));
                    one.setBackgroundResource(R.drawable.indicator_selected);
                    zero.setBackgroundResource(R.drawable.indicator_not_selected);
                    two.setBackgroundResource(R.drawable.indicator_not_selected);
                    finishButton.setVisibility(View.GONE);
                    break;
                case 3: textView.setText(getString(R.string.intro_slide_text_3));
                    two.setBackgroundResource(R.drawable.indicator_selected);
                    zero.setBackgroundResource(R.drawable.indicator_not_selected);
                    one.setBackgroundResource(R.drawable.indicator_not_selected);
                    finishButton.setVisibility(View.VISIBLE);
                break;
                default:
                    break;
            }
            return rootView;
        }


    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
