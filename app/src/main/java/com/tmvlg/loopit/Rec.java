package com.tmvlg.loopit;

import android.widget.Button;
import android.widget.ImageButton;

import com.airbnb.lottie.LottieAnimationView;

import pl.droidsonroids.gif.GifImageButton;

public class Rec {
    public LottieAnimationView btn;
    private String status;

    public Rec(LottieAnimationView button){
        this.btn = button;
        status = "start";
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
