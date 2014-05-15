package com.example.demoflavius.util;

import com.example.demoflavius.data.Enums;
import com.microsoft.windowsazure.mobileservices.MobileServiceAuthenticationProvider;

public class IdentityProviderConverter {
	  public static Enums.IdentityProvider GetProvider(MobileServiceAuthenticationProvider providerName)
      {
          Enums.IdentityProvider returnedProvider = Enums.IdentityProvider.Facebook;
          switch (providerName)
          {
              case Facebook:
                  returnedProvider = Enums.IdentityProvider.Facebook;
                  break;
              case Google:
                  returnedProvider = Enums.IdentityProvider.Google;
                  break;
              case MicrosoftAccount:
                  returnedProvider = Enums.IdentityProvider.MicrosoftAccount;
                  break;
              case Twitter:
                  returnedProvider = Enums.IdentityProvider.Twitter;
                  break;
          }
          return returnedProvider;
      }
}
