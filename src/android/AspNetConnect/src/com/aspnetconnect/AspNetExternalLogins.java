package com.aspnetconnect;

import java.util.ArrayList;

public class AspNetExternalLogins {
	private ArrayList<String> providers = new ArrayList<String>(AspNetProvider.values().length);
	public void setExternalLoginLink(AspNetProvider provider, String link){
		providers.set(provider.ordinal(), link);
	}
	public String getExternalLoginLonk(AspNetProvider provider){
		return providers.get(provider.ordinal());
	}
}
