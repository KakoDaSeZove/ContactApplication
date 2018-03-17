package com.example.tijana.contactapplication.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.tijana.contactapplication.activities.HappyBirthdayActivity;

import static java.lang.Thread.sleep;

public class ContactReceiver extends BroadcastReceiver {
    public static final String BIRTHDAY = "birthday";
    public static final String BIRTHDAY_NAME = "birthday name";
    public static final String HAPPY_BIRTHDAY = "com.example.tijana.contactapplication.NEW_BIRTHDAY";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        if (intent.getAction() == HAPPY_BIRTHDAY) {
            String birthday = intent.getExtras().getString(BIRTHDAY);
            String birthdayName = intent.getExtras().getString(BIRTHDAY_NAME);

            SimpleSyncTask sst = new SimpleSyncTask(context);
            //! Execute function doesn't run in parallel

            //! Use executeOnExecutor for parallel jobs
            sst.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, birthday, birthdayName);
        }
    }

    private class SimpleSyncTask extends AsyncTask<String, String, String> {

        private Context context;

        public SimpleSyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {

            int x = Integer.parseInt(strings[0]);


            Log.i("TAG", "Start " + strings[0] + " " + strings[1]);

            try {
                sleep(x * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Intent i = new Intent(context, HappyBirthdayActivity.class);
            i.putExtra(BIRTHDAY_NAME, strings[1]);
            context.startActivity(i);
            return null;
        }
    }
}
