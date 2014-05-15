package com.example.demoflavius.application;

import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;

public class LifecycleHandler  implements ActivityLifecycleCallbacks 
{
	// increment/decrement it instead of using two and incrementing both.
	private int resumed;
	private int stopped;
	@Override
	public void onActivityCreated(Activity activity, Bundle savedInstanceState) 
	{
	}

	@Override
	public void onActivityDestroyed(Activity activity) 
	{
	}

	@Override
	public void onActivityResumed(Activity activity) 
	{
		++resumed;
		if(MyApplication.applicationIsActive == false)
		{
			MyApplication.applicationIsActive = true;
		}
	}

	@Override
	public void onActivityPaused(Activity activity) 
	{
	}

	@Override
	public void onActivitySaveInstanceState(Activity activity, Bundle outState) 
	{
	}

	@Override
	public void onActivityStarted(Activity activity) 
	{
	}

	@Override
	public void onActivityStopped(Activity activity) 
	{
		++stopped;
		if(resumed == stopped)
		{
			MyApplication.applicationIsActive = false;
		}
	}
}
