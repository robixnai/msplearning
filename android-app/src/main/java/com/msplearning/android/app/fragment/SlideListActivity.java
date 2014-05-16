/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.msplearning.android.app.fragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.msplearning.android.app.MainActivity;
import com.msplearning.android.app.R;

@EActivity(R.layout.activity_slide_list)
public class SlideListActivity extends ActionBarActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide fragments representing each object in a collection. We use a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter} derivative, which will destroy and re-create fragments as needed, saving and restoring their
	 * state in the process. This is important to conserve memory and is a best practice when allowing navigation between objects in a potentially large
	 * collection.
	 */
	SlidePagerAdapter mSlidePagerAdapter;

	/**
	 * The {@link android.support.v4.view.ViewPager} that will display the object collection.
	 */
	ViewPager mViewPager;

	@AfterViews
	public void afterViews() {
		// Create an adapter that when requested, will return a fragment representing an object in
		// the collection.
		//
		// ViewPager and its adapters use support library fragments, so we must use
		// getSupportFragmentManager.
		this.mSlidePagerAdapter = new SlidePagerAdapter(this.getSupportFragmentManager());

		// Set up action bar.
		final ActionBar actionBar = this.getSupportActionBar();

		// Specify that the Home button should show an "Up" caret, indicating that touching the
		// button will take the user one step up in the application's hierarchy.
		actionBar.setDisplayHomeAsUpEnabled(true);

		// Set up the ViewPager, attaching the adapter.
		this.mViewPager = (ViewPager) this.findViewById(R.id.pager);
		this.mViewPager.setAdapter(this.mSlidePagerAdapter);
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This is called when the Home (Up) button is pressed in the action bar.
			// Create a simple intent that starts the hierarchical parent activity and
			// use NavUtils in the Support Package to ensure proper handling of Up.
			Intent upIntent = new Intent(this, MainActivity.class);
			if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
				// This activity is not part of the application's task, so create a new task
				// with a synthesized back stack.
				TaskStackBuilder.from(this)
				// If there are ancestor activities, they should be added here.
				.addNextIntent(upIntent).startActivities();
				this.finish();
			} else {
				// This activity is part of the application's task, so simply
				// navigate up to the hierarchical parent activity.
				NavUtils.navigateUpTo(this, upIntent);
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A {@link android.support.v4.app.FragmentStatePagerAdapter} that returns a fragment representing an object in the collection.
	 */
	public static class SlidePagerAdapter extends FragmentStatePagerAdapter {

		public SlidePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			Fragment fragment = new SlideFragment();
			Bundle args = new Bundle();
			args.putInt(SlideFragment.ARG_OBJECT, i + 1); // Our object is just an integer :-P
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// For this contrived example, we have a 100-object collection.
			return 100;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return "Slide " + (position + 1);
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply displays dummy text.
	 */
	public static class SlideFragment extends Fragment {

		public static final String ARG_OBJECT = "slide";

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_list_item, container, false);
			Bundle args = this.getArguments();
			((TextView) rootView.findViewById(android.R.id.text1)).setText(Integer.toString(args.getInt(ARG_OBJECT)));
			return rootView;
		}
	}
}