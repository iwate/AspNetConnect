package com.aspnetconnect;

import java.util.ArrayList;

public class AspNetExternalLogins {
	private ArrayList<String> providers = new ArrayList<String>(AspNetExternalLoginProvider.values().length);
	public void setExternalLoginLink(AspNetExternalLoginProvider provider, String link){
		providers.set(provider.ordinal(), link);
	}
	public String getExternalLoginLonk(AspNetExternalLoginProvider provider){
		return providers.get(provider.ordinal());
	}
}
