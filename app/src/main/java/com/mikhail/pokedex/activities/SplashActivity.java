package com.mikhail.pokedex.activities;


import android.content.*;
import android.os.*;
import java.io.*;

import android.app.Activity;
import android.content.res.AssetManager;
import android.database.SQLException;
import android.util.Log;
import android.widget.ProgressBar;
import com.mikhail.pokedex.R;
import com.mikhail.pokedex.data.PokedexDatabase;


public class SplashActivity extends Activity
{

    public static final String ICONS_LOC = "Icons/";
    public static final String MODELS_LOC = "Models/";

	public boolean spritesDone;
	public boolean iconsDone;
	public boolean dataDone;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		PokedexDatabase db = PokedexDatabase.getInstance(this);

		File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

		//System.out.println(root.getAbsolutePath());

		File sprites = new File("/sdcard/PokeTools/Models");
		File icons = new File("/sdcard/PokeTools/Icons");
		
		if (!sprites.exists())
		{
			sprites.mkdirs();
			new SpritesSetup(this).execute(this);

		}else{
			spritesDone = true;
		}

		if (!icons.exists())
		{
			icons.mkdirs();
			new IconsSetup(this).execute(this);

		}else{
			iconsDone = true;
		}
		
		new PokedexSetup(this).execute(this);
		
		if(spritesDone && dataDone && iconsDone){
			moveOn();
		}
	}
	
	public void moveOn(){
		new Handler().postDelayed(new Runnable(){

				public void run()
				{


					Intent i = new Intent(SplashActivity.this, MainActivity.class);
					startActivity(i);

					finish();

				}
			}, 1000);
	}

	private class SpritesSetup extends AsyncTask<Context, Integer, Void>
	{
	
		
		public SplashActivity act;

		public SpritesSetup(SplashActivity act)
		{
			this.act = act;
		}

		@Override
		protected void onPreExecute()
		{
			ProgressBar bar = (ProgressBar)findViewById(R.id.splashProgressBar);
			bar.setVisibility(ProgressBar.VISIBLE);
			bar.setProgress(0);
			super.onPreExecute();
		};

		
		
		@Override
		protected Void doInBackground(Context[] p1)
		{

			AssetManager assetManager = getAssets();
			String[] files = null;
			try
			{
				files = assetManager.list("Models");
			}
			catch (IOException e)
			{
				Log.e("tag", "Failed to get asset file list.", e);
			}
			for (int i=0;i<files.length;i++)
			{
				publishProgress(i/files.length*100);
				
				String filename = files[i];
				
				//System.out.println(filename);
				InputStream in = null;
				OutputStream fout = null;
				try
				{
					in = assetManager.open("Models/"+filename);
 
					String out= getExternalFilesDir(null).getAbsolutePath()+ICONS_LOC;

					File outFile = new File(out, filename);


					fout = new FileOutputStream(outFile);
					copyFile(in, fout);
					in.close();
					in = null;
					fout.flush();
					fout.close();
					out = null;
				}
				catch (IOException e)
				{
					Log.e("tag", "Failed to copy asset file: " + filename, e);
				}       
				
			}
			return null;
		}


		protected void publishProgress(Integer values)
		{
			// TODO: Implement this method
			ProgressBar bar = (ProgressBar)findViewById(R.id.splashProgressBar);
			bar.setProgress(values);
		}
		
		
		
		private void copyFile(InputStream in, OutputStream out) throws IOException
		{
			byte[] buffer = new byte[1024];
			int read;
			while ((read = in.read(buffer)) != -1)
			{
				out.write(buffer, 0, read);
			}

		}

		@Override
		protected void onPostExecute(Void result)
		{
			
			super.onPostExecute(result);
			
			act.spritesDone = true;
			if(act.spritesDone && act.dataDone && act.iconsDone)
				act.moveOn();
			
		}
		
		
	}

	private class IconsSetup extends AsyncTask<Context, Integer, Void>
	{


		public SplashActivity act;

		public IconsSetup(SplashActivity act)
		{
			this.act = act;
		}

		@Override
		protected void onPreExecute()
		{
			ProgressBar bar = (ProgressBar)findViewById(R.id.splashProgressBar);
			bar.setVisibility(ProgressBar.VISIBLE);
			bar.setProgress(0);
			super.onPreExecute();
		};



		@Override
		protected Void doInBackground(Context[] p1)
		{

			AssetManager assetManager = getAssets();
			String[] files = null;
			try
			{
				files = assetManager.list("Icons");
			}
			catch (IOException e)
			{
				Log.e("tag", "Failed to get asset file list.", e);
			}
			for (int i=0;i<files.length;i++)
			{
				publishProgress(i/files.length*100);

				String filename = files[i];

				//System.out.println(filename);
				InputStream in = null;
				OutputStream fout = null;
				try
				{
					in = assetManager.open("Icons/"+filename);

					String out= getExternalFilesDir(null).getAbsolutePath()+MODELS_LOC;

					File outFile = new File(out, filename);


					fout = new FileOutputStream(outFile);
					copyFile(in, fout);
					in.close();
					in = null;
					fout.flush();
					fout.close();
					out = null;
				}
				catch (IOException e)
				{
					Log.e("tag", "Failed to copy asset file: " + filename, e);
				}       

			}
			return null;
		}



		protected void publishProgress(Integer values)
		{
			// TODO: Implement this method
			ProgressBar bar = (ProgressBar)findViewById(R.id.splashProgressBar);
			bar.setProgress(values);
		}



		private void copyFile(InputStream in, OutputStream out) throws IOException
		{
			byte[] buffer = new byte[1024];
			int read;
			while ((read = in.read(buffer)) != -1)
			{
				out.write(buffer, 0, read);
			}

		}

		@Override
		protected void onPostExecute(Void result)
		{

			super.onPostExecute(result);

			act.iconsDone = true;
			if(act.spritesDone && act.dataDone && act.iconsDone)
				act.moveOn();

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
			// TODO: Implement this method
			super.onPreExecute();
		}



		@Override
		protected Void doInBackground(Context[] p1)
		{


			PokedexDatabase myDbHelper;
			myDbHelper = new PokedexDatabase(p1[0]);

			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			// TODO: Implement this method
			super.onPostExecute(result);

			act.dataDone = true;
			if(act.dataDone && act.spritesDone && act.iconsDone)
				act.moveOn();


		}








	}

}
