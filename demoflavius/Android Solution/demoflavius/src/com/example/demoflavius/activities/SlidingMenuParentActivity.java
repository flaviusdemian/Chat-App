package com.example.demoflavius.activities;

import java.util.ArrayList;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.example.demoflavius.R;
import com.example.demoflavius.adapters.ChatAdapterForSlidingMenu;
import com.example.demoflavius.adapters.SettingsAdapter;
import com.example.demoflavius.application.MyApplication;
import com.example.demoflavius.data.Friend;
import com.example.demoflavius.data.Section;
import com.example.demoflavius.data.UserEntity;
import com.example.demoflavius.fragments.ChatPageFragment;
import com.example.demoflavius.util.ImageLoaderManager;
import com.example.demoflavius.util.MenuHelper;
import com.example.demoflavius.util.NavigationService;
import com.example.demoflavius.util.NotificationService;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public abstract class SlidingMenuParentActivity extends SherlockFragmentActivity implements ExpandableListView.OnChildClickListener {

	protected DrawerLayout mDrawerLayout;
	protected ListView mDrawerListRight;
	protected ExpandableListView mDrawerListLeft;
	protected ActionBarDrawerToggle mDrawerToggle;

	protected CharSequence mDrawerTitle;
	protected CharSequence mTitle;

	protected ProgressBar progressBar;
	private ImageLoader imageLoader;
	private boolean sliderInitialized ; 

	protected ArrayList<Section> settings;
	protected SettingsAdapter settingsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		try
		{
			sliderInitialized = false;
			super.onCreate(savedInstanceState);

			setContentView(R.layout.activity_slidingmenu);
			imageLoader = ImageLoaderManager.getImageLoader();

			initializeUIElements();

			if (savedInstanceState == null) {
				selectItem();
			}
		}
		catch(Exception ex)
		{
			NotificationService.showDebugCrouton("failed at onCreate at SlidingMenuParentActivity!");
			ex.printStackTrace();
		}
	}

	protected void initializeUIElements() 
	{
		if( sliderInitialized == false)
		{
			try
			{
				mTitle = mDrawerTitle = getTitle();
				mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
				mDrawerListLeft = (ExpandableListView) findViewById(R.id.left_drawer);
				// no need to actually populate the right menu as obviously it would have different items and functionality than the left
				mDrawerListRight = (ListView) findViewById(R.id.right_drawer); 

				// set a custom shadow that overlays the main content when the drawer opens
				mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
				mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.END);
				// set up the drawer's list view with items and click listener

				mDrawerListRight.setAdapter(MyApplication.getChatAdapterForSlidingMenu());
				mDrawerListRight.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						try
						{
							ChatAdapterForSlidingMenu chatAdapterForSlidingMenu = MyApplication.getChatAdapterForSlidingMenu();
							if( chatAdapterForSlidingMenu != null)
							{
								Friend selectedItem = chatAdapterForSlidingMenu.getDataSource().get(position);
								if( selectedItem != null)
								{
									if(selectedItem.getId().equals(ChatPageFragment.currentCustomerIdITalkTo) == false)
									{
										ChatPageFragment.updateChatConversations(selectedItem.getId(), selectedItem.getName(), selectedItem.getPicture());
										collapseRightSideAfterSelection();
									}
								}
								else
								{
									NotificationService.showDebugCrouton("failed at startChatApp, selectedItem is null");
								}
							}
						}
						catch(Exception ex)
						{
							NotificationService.showDebugCrouton("failed at startChatApp");
							ex.printStackTrace();	
						}
					}
				});

				settings = MenuHelper.createMenu();
				settingsAdapter = new SettingsAdapter(getApplicationContext(), R.layout.activity_settings , settings);

			}
			catch(Exception ex)
			{
				NotificationService.showDebugCrouton("fucking crapat here!");
				ex.printStackTrace();
			}

			try
			{
				View expandableHeaderReference = getLayoutInflater().inflate(R.layout.activity_expandable_listview_header, null);			
				if( expandableHeaderReference != null)
				{
					mDrawerListLeft.addHeaderView(expandableHeaderReference);

					TextView tv_userName = (TextView) expandableHeaderReference.findViewById(R.id.current_customer_name);
					UserEntity currentUser = MyApplication.getCurrentUser();
					if( currentUser != null)
					{
						if( tv_userName != null)
						{
							tv_userName.setText(currentUser.getName());
						}

						TextView tv_location = (TextView) expandableHeaderReference.findViewById(R.id.current_customer_location);
						if( tv_location != null)
						{
							tv_location.setText(currentUser.getLocation());
						}

						ImageView iv_profilePicture = (ImageView) expandableHeaderReference.findViewById(R.id.current_customer_image);
						if( iv_profilePicture != null)
						{
							imageLoader.displayImage(currentUser.getPicture(), iv_profilePicture, ImageLoaderManager.getImageLoaderOptions(),new ImageLoadingListener() {
								@Override
								public void onLoadingCancelled(String arg0, View arg1) {
								}

								@Override
								public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
								}

								@Override
								public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
								}

								@Override
								public void onLoadingStarted(String arg0, View arg1) {
								}
							});
						}

						expandableHeaderReference.setOnClickListener(new OnClickListener() 
						{	
							@Override
							public void onClick(View v) 
							{	
							}
						});
					}
					else
					{
						NotificationService.showDebugCrouton("there is a problem here!, user is null!");
					}
				}
			}
			catch(Exception ex)
			{
				NotificationService.showDebugCrouton("Crapat at header....!");
				ex.printStackTrace();
			}

			try
			{
				mDrawerListLeft.setAdapter(settingsAdapter);
				mDrawerListLeft.setOnItemClickListener(new DrawerItemClickListener());

				mDrawerListLeft.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
					@Override
					public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
						return true;
					}
				});

				mDrawerListLeft.setOnChildClickListener(this);

				int count = settingsAdapter.getGroupCount();
				for (int position = 0; position < count; position++) 
				{
					this.mDrawerListLeft.expandGroup(position);
				}

				getActionBar().setDisplayShowTitleEnabled(false);
				getActionBar().setDisplayHomeAsUpEnabled(true);
				getActionBar().setHomeButtonEnabled(true);
				getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.blue_bar));

				// ActionBarDrawerToggle ties together the the proper interactions
				// between the sliding drawer and the action bar app icon
				mDrawerToggle = new ActionBarDrawerToggle(
						this,                  /* host Activity */
						mDrawerLayout,         /* DrawerLayout object */
						R.drawable.ic_launcher,  /* nav drawer image to replace 'Up' caret */
						R.string.drawer_open,  /* "open drawer" description for accessibility */
						R.string.drawer_close  /* "close drawer" description for accessibility */
						) {
					public void onDrawerClosed(View view) {
						getActionBar().setTitle(mTitle);
						invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
					}

					public void onDrawerOpened(View drawerView) {
						getActionBar().setTitle(mDrawerTitle);
						invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
					}
				};
				// This would disable the home button from being the menu toggle and revert it to normal "back" behavior
				// mDrawerToggle.setDrawerIndicatorEnabled(false); 
				mDrawerLayout.setDrawerListener(mDrawerToggle);
				sliderInitialized = true;
			}
			catch(Exception ex)
			{
				NotificationService.showDebugCrouton("Crapat at mDrawerToggle....!");
				ex.printStackTrace();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		try
		{
			MenuInflater menuInflater = getSupportMenuInflater();
			menuInflater.inflate(R.menu.slidingmenu, menu);
			return super.onCreateOptionsMenu(menu);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			NotificationService.showDebugCrouton("Crapat at onCreateOptionsMenu....!");
		}
		return false;
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) 
	{
		try
		{
			// If the nav drawer is open, hide action items related to the content view
			boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerListLeft);
			menu.findItem(R.id.action_chat).setVisible(!drawerOpen);
			//			menu.findItem(R.id.t).setVisible(!drawerOpen);
			return super.onPrepareOptionsMenu(menu);
		}
		catch(Exception ex)
		{
			NotificationService.showDebugCrouton("exception at onPrepareOptionsMenu SlidingMenuParentActivity");
			ex.printStackTrace();
		}	
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		try
		{
			int itemId = item.getItemId();
			if (itemId == R.id.action_chat) 
			{
				mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
				// You can also use "GravityCompat.END" instead of mDrawerListRight
				if (mDrawerLayout.isDrawerOpen(mDrawerListRight)) 
				{
					mDrawerLayout.closeDrawer(mDrawerListRight);
				} 
				else 
				{
					mDrawerLayout.openDrawer(mDrawerListRight);
				}
				return true;
			}

			return super.onOptionsItemSelected(item);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			NotificationService.showDebugCrouton("exception at onOptionsItemSelected");
		}
		return false;
	}
	
	public void collapseRightSideAfterSelection()
	{
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		// You can also use "GravityCompat.END" instead of mDrawerListRight
		if (mDrawerLayout.isDrawerOpen(mDrawerListRight)) 
		{
			mDrawerLayout.closeDrawer(mDrawerListRight);
		} 
	}

	/* The click listener for ListView in the navigation drawer */
	private class DrawerItemClickListener implements ListView.OnItemClickListener
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
		{
			try
			{
				selectItem();
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}

	protected abstract void selectItem();

	@Override
	public void setTitle(CharSequence title) 
	{
		try
		{
			mTitle = title;
			getActionBar().setTitle(mTitle);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) 
	{
		try
		{
			super.onPostCreate(savedInstanceState);
			// Sync the toggle state after onRestoreInstanceState has occurred.
			if( mDrawerToggle != null)
			{
				mDrawerToggle.syncState();
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.imageview_anim));
		switch ((int)id) {

		case 304:
			MyApplication.getMobileServiceClient().logout();
			NavigationService.navigate(MyApplication.getCurrentActivity(), LoginActivity.class, null);
			break;
		}
		return false;
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) 
	{
		try
		{
			super.onConfigurationChanged(newConfig);
			// Pass any configuration change to the drawer toggls
			mDrawerToggle.onConfigurationChanged(newConfig);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
