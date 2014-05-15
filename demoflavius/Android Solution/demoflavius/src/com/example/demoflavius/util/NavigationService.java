package com.example.demoflavius.util;

import java.util.Map;

import android.app.Activity;
import android.content.Intent;

public class NavigationService {
	public static void navigate(Activity source, Class<?> destination, Map<String, String> extras)
	{
		try
		{
			Intent intent = new Intent(source, destination);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			if(extras != null)
			{
				for (Map.Entry<String, String> entry : extras.entrySet()) 
				{
					intent.putExtra(entry.getKey(),entry.getValue());
				}
			}
			source.startActivity(intent);

		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			NotificationService.showDebugCrouton("failed at Navigate!");
		}
	}
}
