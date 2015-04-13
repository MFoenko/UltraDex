package com.mikhail.pokedex.activities;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.vending.billing.IInAppBillingService;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.mikhail.pokedex.R;
import com.mikhail.pokedex.data.PokedexDatabase;
import com.mikhail.pokedex.fragments.AdmobBannerAd;
import com.mikhail.pokedex.misc.CrashDialog;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class SplashActivity extends Activity
{

	public static final int MEDIA_VERSION = 30;
    public static final String ICONS_LOC = "Icons/";
    public static final String MODELS_LOC = "Models/";

	public static final int WAIT_TIME = 300;

	public boolean mediaDone;
	public boolean mediaFail;
	public boolean dataDone;
	public boolean mediaZipNotExists= false;

	public File version;

	private TextView loadingTV;

	public static final String[] TIP_MESSAGES = new String[]{


	};

	IInAppBillingService mService;

	ServiceConnection mServiceConn = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name)
		{
			mService = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, 
									   IBinder service)
		{
			mService = IInAppBillingService.Stub.asInterface(service);
			if (!AdmobBannerAd.showAds)
			{
				Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
				serviceIntent.setPackage("com.android.vending");
				bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);

				ArrayList<String> skuList = new ArrayList<String>();
				skuList.add("remove_ads");
				final Bundle querySkus = new Bundle();
				querySkus.putStringArrayList("ITEM_ID_LIST", skuList);

				new Thread(new Runnable(){

						@Override
						public void run()
						{
							try
							{
								Bundle ownedItems = mService.getPurchases(3, getPackageName(), "inapp", null);
								int response = ownedItems.getInt("RESPONSE_CODE");
								if (response == 0)
								{
									ArrayList<String> ownedSkus =
										ownedItems.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
									Log.e("AAA", ownedSkus.toString());
									// do something with this purchase information
									// e.g. display the updated list of products owned by user


									// if continuationToken != null, call getPurchases again 
									// and pass in the token to retrieve more items
								}

							}
							catch (RemoteException e)
							{}
						}


					}).start();
			}
		}
	};

	public SplashActivity()
	{
		Thread.setDefaultUncaughtExceptionHandler(new CrashDialog(this));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);

		checkPurchase();

		GoogleAnalytics ana = GoogleAnalytics.getInstance(this);
		Tracker t = ana.newTracker("UA-58176286-1");
		setContentView(R.layout.splash);
		loadingTV = (TextView)findViewById(R.id.loading_message);

		File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

		//System.out.println(root.getAbsolutePath());

		version = new File(getExternalFilesDir(null), "version");
		try
		{
			Scanner reader = new Scanner(version);
			if (reader.nextInt() != MEDIA_VERSION)
			{
				new MediaSetup(this).execute();
			}
			else
			{
				mediaDone = true;
			}
		}
		catch (FileNotFoundException e)
		{
			new MediaSetup(this).execute();
		}
		catch (NoSuchElementException e)
		{
			new MediaSetup(this).execute();
		}

		new PokedexSetup(this).execute(getApplicationContext());

		moveOn();
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		if (mService != null)
		{
			unbindService(mServiceConn);
		}	
	}

	public void moveOn()
	{
		if (dataDone && mediaDone && !mediaZipNotExists)
		{

			new Handler().postDelayed(new Runnable(){

					public void run()
					{


						Intent i = new Intent(SplashActivity.this, MainActivity.class);
						startActivity(i);

						finish();

					}
				}, WAIT_TIME);
		}
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		if (mediaZipNotExists)
		{
            showMissingFilesDiallog();
        }
	}

	public void checkPurchase()
	{

		AdmobBannerAd.showAds = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(AdmobBannerAd.PREF_SHOW_ADS, true);

	}

	public void showMissingFilesDiallog()
	{
		new AlertDialog.Builder(this).setTitle("File Install Failed").setPositiveButton("Ok", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					mediaDone = true;
					mediaZipNotExists = false;
					moveOn();
				}


			}).create().show();
	}


	private class MediaSetup extends AsyncTask<Context, Integer, Boolean>
	{


		public SplashActivity act;
		public static final String INIT_LOADING_MESSAGE = "Upgrading Ultradex";

		public final String[] LOADING_MESSAGES = new String[]{
			"Fishing for Feebas",
			"Battling Whitney",
			"Hatching Eggs",
			"Saving Lots of Data",
			"Waiting for Mirage Island",
			"Making Pokeblocks",
			"Cooking Poffins",
			"Watering Berries",
			"Connecting to Nintendo WFC"

		};
		public static final String DONE_LOADING_MESSAGE = "Launching Ultradex";
		private ArrayList<String> loadingMessages;

		public static final int BASE_MESSAGE_REFRESH_PERCENT = 6;


		public MediaSetup(SplashActivity act)
		{
			this.act = act;
		}

		@Override
		protected void onPreExecute()
		{
			ProgressBar bar = (ProgressBar)findViewById(R.id.splashProgressBar);
			bar.setVisibility(ProgressBar.VISIBLE);
			bar.setProgress(0);
			loadingTV.setText(INIT_LOADING_MESSAGE);
			prepareLoadingMessagesAL();
			super.onPreExecute();
		};


		private void prepareLoadingMessagesAL()
		{
			loadingMessages = new ArrayList<String>(Arrays.asList(LOADING_MESSAGES));
		}

		@Override
		protected Boolean doInBackground(Context[] p1)
		{


			return extract();

		}

		public boolean extract()
		{

			boolean result = false;
            String packageName = getApplicationContext().getPackageName();

            File root = Environment.getExternalStorageDirectory();
            File expPath = new File(root.toString() + "/Android/obb/" + packageName);

            if (expPath.exists())
			{
                String strMainPath = null;
                try
				{
                    strMainPath = expPath + File.separator + "main."
                        + MEDIA_VERSION + "."
						+ packageName + ".obb";


					Log.e("Extract File path", "===>" + strMainPath);

					File f=new File(strMainPath);
					if (f.exists())
					{
						Log.e("Extract From File path", "===> not exist");
					}
					else
					{
						Log.e("Extract From File path", "===> exist");
					}

					result = extractZip(strMainPath, getExternalFilesDir(null).getAbsolutePath());

					Log.e("After Extract Zip", "===>" + result);
                }
				catch (Exception e)
				{
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
			else
			{
				mediaZipNotExists = true;

			}
			return result;
		}

		private boolean extractZip(String pathOfZip, String pathToExtract)
		{


			int BUFFER_SIZE = 1024;
			int size;
			byte[] buffer = new byte[BUFFER_SIZE];


			try
			{
				File f = new File(pathToExtract);
				if (!f.isDirectory())
				{
					f.mkdirs();
				}
				ZipInputStream zin = new ZipInputStream(new BufferedInputStream(new FileInputStream(pathOfZip), BUFFER_SIZE));
				final long fileSize = new File(pathOfZip).length();
				long sizeUnzipped = 0;
				try
				{
					ZipEntry ze = null;
					while ((ze = zin.getNextEntry()) != null)
					{
						String path = pathToExtract  + "/" + ze.getName();

						if (ze.isDirectory())
						{
							File unzipFile = new File(path);
							if (!unzipFile.isDirectory())
							{
								unzipFile.mkdirs();
							}
						}
						else
						{
							FileOutputStream out = new FileOutputStream(path, false);
							BufferedOutputStream fout = new BufferedOutputStream(out, BUFFER_SIZE);
							try
							{
								while ((size = zin.read(buffer, 0, BUFFER_SIZE)) != -1)
								{
									fout.write(buffer, 0, size);
									sizeUnzipped += size;
									publishProgress((int)(((double)sizeUnzipped / fileSize) * 100));

								}

								zin.closeEntry();
							}
							catch (Exception e)
							{
								Log.e("Exception", "Unzip exception 1:" + e.toString());
							}
							finally
							{
								fout.flush();
								fout.close();
							}
						}
					}
				}
				catch (Exception e)
				{
					Log.e("Exception", "Unzip exception2 :" + e.toString());
				}
				finally
				{
					zin.close();
				}
				return true;
			}
			catch (Exception e)
			{
				Log.e("Exception", "Unzip exception :" + e.toString());
			}
			return false;

		}



		protected void publishProgress(final Integer values)
		{
			runOnUiThread(new Runnable(){

					@Override
					public void run()
					{

						ProgressBar bar = (ProgressBar)findViewById(R.id.splashProgressBar);
						bar.setProgress(values);
						if (values != 0 && values % BASE_MESSAGE_REFRESH_PERCENT == 0)
						{
							if (loadingMessages.size() > 0)
							{
								loadingTV.setText(loadingMessages.remove((int)(Math.random() * loadingMessages.size())));
							}
							else
							{
								prepareLoadingMessagesAL();
							}
						}
						if (values == 100)
						{
							loadingTV.setText(DONE_LOADING_MESSAGE);
						}
					}
				});
		}



		@Override
		protected void onPostExecute(Boolean result)
		{

			super.onPostExecute(result);

			if (result)
			{
				try
				{
					version.createNewFile();
					FileWriter writer = new FileWriter(version, false);
					writer.write(String.valueOf(MEDIA_VERSION));
					writer.flush();
					writer.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}


				act.mediaDone = true;
				act.moveOn();
			}
			else
			{
				mediaFail = true;
				showMissingFilesDiallog();
			}
		}


	}


	private class PokedexSetup extends AsyncTask<Context, Void, Void>
	{


		public SplashActivity act;

		public PokedexSetup(SplashActivity act)
		{
			this.act = act;
		}


		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
		}



		@Override
		protected Void doInBackground(Context[] p1)
		{

			PokedexDatabase dex = PokedexDatabase.getInstance(p1[0]);
			try
			{
				dex.getAllPokemon();
				dex.getAllMoves();
                dex.getAllAbilities();
                dex.getAllItems();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			// TODO: Implement this method
			super.onPostExecute(result);

			act.dataDone = true;
			act.moveOn();


		}








	}

}
