package com.tmvlg.loopit;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

public class MetronomeAsyncTask extends AsyncTask<Void, Integer, Void> {
    private static final String TAG = MetronomeAsyncTask.class.getSimpleName();
    private WeakReference<TextView> wTextView;
    private float delay;
    private int min;
    private int sec;

    public MetronomeAsyncTask(TextView textView, float delay) {
        this.wTextView = new WeakReference<>(textView);
        this.delay = delay * 1000;
        min = 0;
        sec = 0;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        updateText("Запуск");
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (values[1] < 10)
            updateText("" + values[0] + ":0" + values[1]);
        else
            updateText("" + values[0] + ":" + values[1]);
    }

    @Override
    protected Void doInBackground(Void... params) {
        while (!super.isCancelled()) {
            try {
                getTime();
                if (sec < 60) {
                    sec++;
                } else {
                    sec = 0;
                    min++;
                }
                publishProgress(min, sec);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        updateText("Выполнено");
    }

    private void getTime() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep((long) delay);
    }

    private void updateText(String str){
        final TextView textView = wTextView.get();
        if (textView != null) {
            textView.setText(str);
        } else {
            Log.d(TAG, "Что-то пошло не так!");
        }
    }

}
