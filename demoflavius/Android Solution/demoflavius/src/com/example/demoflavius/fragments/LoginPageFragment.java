package com.example.demoflavius.fragments;

import android.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.example.demoflavius.util.NotificationService;

public class LoginPageFragment extends SherlockFragment{
	private static ListView customerslist;
	private View rootView;
	public static boolean firstCallMade = false; 
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		rootView = null;
		try
		{
			rootView = inflater.inflate(R.layout.activity_list_item, container, false);
			
			initializeUIElements();	

		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return rootView;
	}

	private void initializeUIElements() {
		try
		{
		}
		catch(Exception ex)
		{
			NotificationService.showDebugCrouton("failed at initializeUIElements FragmentCustomers");
			ex.printStackTrace();
		}
	}
}
