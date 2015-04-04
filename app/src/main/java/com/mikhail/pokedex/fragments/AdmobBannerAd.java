package com.mikhail.pokedex.fragments;

import android.os.*;
import android.support.v4.app.*;
import android.view.*;
import com.google.android.gms.ads.*;
import com.mikhail.pokedex.*;

public class AdmobBannerAd extends Fragment {

	static AdView ad;
	public static final String AD_ID = "ca-app-pub-2920914560343441/1040711216";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		ad = new AdView(inflater.getContext());
		ad.setAdSize(AdSize.SMART_BANNER);
		ad.setAdUnitId(AD_ID);
		ad.setAdListener(new AdListener(){
				public void onAdLoaded() {

					ad.setVisibility(View.VISIBLE);

				}
			});
		ad.setVisibility(View.GONE);
		return ad;

	}

	@Override
	public void onStart() {
		super.onStart();
		AdRequest req = new AdRequest.Builder().addTestDevice("717713C885779182B8D2859A791219FE").build();

		ad.loadAd(req);

	}



}
