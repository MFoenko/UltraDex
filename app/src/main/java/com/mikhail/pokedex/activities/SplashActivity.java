package com.mikhail.pokedex.activities;


import android.app.*;
import android.content.*;
import android.content.res.*;
import android.os.*;
import android.util.*;
import android.widget.*;
import com.mikhail.pokedex.*;
import com.mikhail.pokedex.data.*;
import java.io.*;
import java.util.*;
import java.util.zip.*;


public class SplashActivity extends Activity{

	public static final int MEDIA_VERSION = 30;
    public static final String ICONS_LOC = "Icons/";
    public static final String MODELS_LOC = "Models/";

	public static final int WAIT_TIME = 300;
	
	public boolean mediaDone;
	public boolean dataDone;
	public boolean mediaZipNotExists= false;
	
	public File version;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);


		File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

		//System.out.println(root.getAbsolutePath());

		version = new File(getExternalFilesDir(null), "version");
		try{
			Scanner reader = new Scanner(version);
			if (reader.nextInt() != MEDIA_VERSION){
				new MediaSetup(this).execute();
			}else{
				mediaDone = true;
			}
		}catch (FileNotFoundException e){
			new MediaSetup(this).execute();
		}catch(NoSuchElementException e){
			new MediaSetup(this).execute();
		}

		new PokedexSetup(this).execute(this);

		moveOn();
	}

	public void moveOn(){
		if (dataDone && mediaDone && !mediaZipNotExists){

			new Handler().postDelayed(new Runnable(){

					public void run(){


						Intent i = new Intent(SplashActivity.this, MainActivity.class);
						startActivity(i);

						finish();

					}
				}, WAIT_TIME);
		}
	}

	private class MediaSetup extends AsyncTask<Context, Integer, Void>{


		public SplashActivity act;

		public MediaSetup(SplashActivity act){
			this.act = act;
		}

		@Override
		protected void onPreExecute(){
			ProgressBar bar = (ProgressBar)findViewById(R.id.splashProgressBar);
			bar.setVisibility(ProgressBar.VISIBLE);
			bar.setProgress(0);
			super.onPreExecute();
		};



		@Override
		protected Void doInBackground(Context[] p1){

			extract();
			return null;

		}

		public void extract(){

            String packageName = getApplicationContext().getPackageName();

            File root = Environment.getExternalStorageDirectory();
            File expPath = new File(root.toString() + "/Android/obb/" + packageName);

            if (expPath.exists()){
                String strMainPath = null;
                try{
                    strMainPath = expPath + File.separator + "main."
                        + MEDIA_VERSION + "."
						+ packageName + ".obb";


					Log.e("Extract File path", "===>" + strMainPath);

					File f=new File(strMainPath);
					if (f.exists()){
						Log.e("Extract From File path", "===> not exist");
					}else{
						Log.e("Extract From File path", "===> exist");
					}

					boolean flag = extractZip(strMainPath, getExternalFilesDir(null).getAbsolutePath());

					Log.e("After Extract Zip", "===>" + flag);
                }catch (Exception e){
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }else{
				mediaZipNotExists = true;
			}

		}

		private boolean extractZip(String pathOfZip, String pathToExtract){


			int BUFFER_SIZE = 1024;
			int size;
			byte[] buffer = new byte[BUFFER_SIZE];


			try{
				File f = new File(pathToExtract);
				if (!f.isDirectory()){
					f.mkdirs();
				}
				ZipInputStream zin = new ZipInputStream(new BufferedInputStream(new FileInputStream(pathOfZip), BUFFER_SIZE));
				final long fileSize = new File(pathOfZip).length();
				long sizeUnzipped = 0;
				try{
					ZipEntry ze = null;
					while ((ze = zin.getNextEntry()) != null){
						String path = pathToExtract  + "/" + ze.getName();

						if (ze.isDirectory()){
							File unzipFile = new File(path);
							if (!unzipFile.isDirectory()){
								unzipFile.mkdirs();
							}
						}else{
							FileOutputStream out = new FileOutputStream(path, false);
							BufferedOutputStream fout = new BufferedOutputStream(out, BUFFER_SIZE);
							try{
								while ((size = zin.read(buffer, 0, BUFFER_SIZE)) != -1){
									fout.write(buffer, 0, size);
									sizeUnzipped+=size;
									publishProgress((int)(((double)sizeUnzipped / fileSize) * 100));
									
								}
								
								zin.closeEntry();
							}catch (Exception e){
								Log.e("Exception", "Unzip exception 1:" + e.toString());
							}finally{
								fout.flush();
								fout.close();
							}
						}
						}
				}catch (Exception e){
					Log.e("Exception", "Unzip exception2 :" + e.toString());
				}finally{
					zin.close();
				}
				return true;
			}catch (Exception e){
				Log.e("Exception", "Unzip exception :" + e.toString());
			}
			return false;

		}



		protected void publishProgress(Integer values){
			// TODO: Implement this method
			ProgressBar bar = (ProgressBar)findViewById(R.id.splashProgressBar);
			bar.setProgress(values);
		}



		@Override
		protected void onPostExecute(Void result){

			super.onPostExecute(result);

			try{
				version.createNewFile();
				FileWriter writer = new FileWriter(version, false);
				writer.write(String.valueOf(MEDIA_VERSION));
				writer.flush();
				writer.close();
			}catch (IOException e){
				e.printStackTrace();
			}

			act.mediaDone = true;
				act.moveOn();

		}


	}


	private class PokedexSetup extends AsyncTask<Context, Void, Void>{


		public SplashActivity act;

		public PokedexSetup(SplashActivity act){
			this.act = act;
		}


		@Override
		protected void onPreExecute(){
			// TODO: Implement this method
			super.onPreExecute();
		}



		@Override
		protected Void doInBackground(Context[] p1){

			PokedexDatabase.getInstance(act);

			return null;
		}

		@Override
		protected void onPostExecute(Void result){
			// TODO: Implement this method
			super.onPostExecute(result);

			act.dataDone = true;
				act.moveOn();


		}








	}

}
