package com.tmvlg.loopit;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

public class MetronomeAsyncTask extends AsyncTask<Void, Integer, Void> {
    private static final String TAG = MetronomeAsyncTask.class.getSimpleName();
    private ImageView[] dots;
    private Context context;
    private float delay;


    public MetronomeAsyncTask(ImageView[] dots, float delay, Context context) {
        this.dots = dots;
        this.delay = delay * 1000;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("tagn1", "be sure, " + dots.length);

    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.scale_center_anim);
        dots[values[0]].startAnimation(anim);
        Log.d("tagn1", "dot #" + values[0]);
    }

    @Override
    protected Void doInBackground(Void... params) {
        Log.d("tagn1", "GONNA CANCELED");
        while (!super.isCancelled()) {
            Log.d("tagn1", "GO!");
            for (int numberOfDot = 0; numberOfDot < 4; numberOfDot++) {
                publishProgress(numberOfDot);
                try {
                    sleepFor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        Log.d("tagn1", "Crashed?");

    }

    @Override
    protected void onCancelled(Void aVoid) {
        super.onCancelled(aVoid);
        Log.d("tagn1", "Y FUCKIN CANCELED!?!??!");
    }

    private void sleepFor() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep((long) delay);
    }


}
