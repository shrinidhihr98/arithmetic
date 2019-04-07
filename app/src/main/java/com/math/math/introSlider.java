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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

public class introSlider extends AppCompatActivity {
    private final String TAG = "IntroSlider.java";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_slider);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        ViewPager mViewPager =  findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                ImageView zero = findViewById(R.id.intro_indicator_0);
                ImageView one =  findViewById(R.id.intro_indicator_1);
                ImageView two =  findViewById(R.id.intro_indicator_2);
                Log.i(TAG, "onPageSelected: Getting int i:"+i);
                switch (i){
                    case 0: zero.setBackgroundResource(R.drawable.indicator_selected);
                        one.setBackgroundResource(R.drawable.indicator_not_selected);
                        two.setBackgroundResource(R.drawable.indicator_not_selected);
                        break;
                    case 1: one.setBackgroundResource(R.drawable.indicator_selected);
                        zero.setBackgroundResource(R.drawable.indicator_not_selected);
                        two.setBackgroundResource(R.drawable.indicator_not_selected);
                        break;
                    case 2: two.setBackgroundResource(R.drawable.indicator_selected);
                        one.setBackgroundResource(R.drawable.indicator_not_selected);
                        zero.setBackgroundResource(R.drawable.indicator_not_selected);
                        break;
                    default:
                            break;
                }


            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


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
                    finishButton.setVisibility(View.GONE);
                    break;
                case 2: textView.setText(getString(R.string.intro_slide_text_2));
                    finishButton.setVisibility(View.GONE);
                    break;
                case 3: textView.setText(getString(R.string.intro_slide_text_3));
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
