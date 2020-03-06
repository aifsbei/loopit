package com.tmvlg.loopit;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

public class TimerAsyncTask extends AsyncTask<Void, Integer, Void> {
    private static final String TAG = MetronomeAsyncTask.class.getSimpleName();
    private WeakReference<TextView> wTextView;
    private int min;
    private int sec;

    public TimerAsyncTask(TextView textView) {
        this.wTextView = new WeakReference<>(textView);
        min = 0;
        sec = 0;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        updateText("Запуск");
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
//        updateText("Выполнено");
    }

    @Override
    protected void onCancelled(Void aVoid) {
        super.onCancelled(aVoid);
        updateText("0:00");
    }

    private void getTime() throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
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
