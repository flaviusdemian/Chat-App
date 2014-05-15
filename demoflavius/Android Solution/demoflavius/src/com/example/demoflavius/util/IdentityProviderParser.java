package com.example.demoflavius.util;

public class IdentityProviderParser {
	  public static String GetShortProvider(String longProviderId)
      {
          try
          {
              String[] result = longProviderId.split(":");
              if (result != null && result.length == 2)
              {
                  return result[1];
              }
          }
          catch (Exception ex)
          {
        	  NotificationService.showDebugCrouton("failed at GetShortProvider");
              ex.printStackTrace();
          }
          return null;
      }
}
