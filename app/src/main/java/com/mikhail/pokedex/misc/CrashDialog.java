package com.mikhail.pokedex.misc;

import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.os.*;
import android.util.*;
import com.google.android.gms.internal.*;
import java.io.*;
import java.util.*;
import java.lang.Thread.UncaughtExceptionHandler;


public class CrashDialog implements UncaughtExceptionHandler {

	public static final String DEV_EMAIL = "ultradexapp@gmail.com";
	
	
    private Context context;
    private static Context context1;

    public CrashDialog(Context ctx) {
        context = ctx;
        context1 = ctx;
    }

    private StatFs getStatFs() {
        File path = Environment.getDataDirectory();
        return new StatFs(path.getPath());
    }

    private long getAvailableInternalMemorySize(StatFs stat) {
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    private long getTotalInternalMemorySize(StatFs stat) {
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    private void addInformation(StringBuilder message) {
        message.append("Locale: ").append(Locale.getDefault()).append('\n');
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi;
            pi = pm.getPackageInfo(context.getPackageName(), 0);
            message.append("Version: ").append(pi.versionName).append('\n');
            message.append("Package: ").append(pi.packageName).append('\n');
        } catch (Exception e) {
            Log.e("CustomExceptionHandler", "Error", e);
            message.append("Could not get Version information for ").append(
				context.getPackageName());
        }
        message.append("Phone Model: ").append(android.os.Build.MODEL)
			.append('\n');
        message.append("Android Version: ")
			.append(android.os.Build.VERSION.RELEASE).append('\n');
        message.append("Board: ").append(android.os.Build.BOARD).append('\n');
        message.append("Brand: ").append(android.os.Build.BRAND).append('\n');
        message.append("Device: ").append(android.os.Build.DEVICE).append('\n');
        message.append("Host: ").append(android.os.Build.HOST).append('\n');
        message.append("ID: ").append(android.os.Build.ID).append('\n');
        message.append("Model: ").append(android.os.Build.MODEL).append('\n');
        message.append("Product: ").append(android.os.Build.PRODUCT)
			.append('\n');
        message.append("Type: ").append(android.os.Build.TYPE).append('\n');
        StatFs stat = getStatFs();
        message.append("Total Internal memory: ")
			.append(getTotalInternalMemorySize(stat)).append('\n');
        message.append("Available Internal memory: ")
			.append(getAvailableInternalMemorySize(stat)).append('\n');
    }

    public void uncaughtException(Thread t, Throwable e) {
        try {
            StringBuilder report = new StringBuilder();
            Date curDate = new Date();
            report.append("Error Report collected on : ")
				.append(curDate.toString()).append('\n').append('\n');
            report.append("Informations :").append('\n');
            addInformation(report);
            report.append('\n').append('\n');
            report.append("Stack:\n");
            final Writer result = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(result);
            e.printStackTrace(printWriter);
            report.append(result.toString());
            printWriter.close();
            report.append('\n');
            report.append("**** End of current Report ***");
            Log.e(CrashDialog.class.getName(),
				  "Error while sendErrorMail" + report);
            sendErrorMail(report);
        } catch (Throwable ignore) {
            Log.e(CrashDialog.class.getName(),
				  "Error while sending error e-mail", ignore);
        }
    }

    /**
     * This method for call alert dialog when application crashed!
     */
    public void sendErrorMail(final StringBuilder errorContent) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                builder.setTitle("Ultradex Crashed!");
                builder.create();
                builder.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
											int which) {
							System.exit(0);
						}
					});
                builder.setPositiveButton("Report",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
											int which) {
							Intent sendIntent = new Intent(
								Intent.ACTION_SEND);
							String subject = "Ultradex 3.0 Beta Crash Report";
							StringBuilder body = new StringBuilder();
							body.append(errorContent).append('\n')
								.append('\n');
							// sendIntent.setType("text/plain");
							sendIntent.setType("message/rfc822");
							sendIntent.putExtra(Intent.EXTRA_EMAIL,
												new String[] {DEV_EMAIL});
							sendIntent.putExtra(Intent.EXTRA_TEXT,
												body.toString());
							sendIntent.putExtra(Intent.EXTRA_SUBJECT,
												subject);
							sendIntent.setType("message/rfc822");
							context1.startActivity(sendIntent);
							System.exit(0);
						}
					});
                builder.setMessage("If this is your first time experiencing this issue, please send a crash report.");
                builder.show();
                Looper.loop();
            }
        }.start();
    }
}
