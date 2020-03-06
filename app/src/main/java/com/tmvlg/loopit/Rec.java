package com.tmvlg.loopit;

import android.widget.Button;
import android.widget.ImageButton;

import pl.droidsonroids.gif.GifImageButton;

public class Rec {
    public GifImageButton btn;
    private String status;

    public Rec(GifImageButton button){
        this.btn = button;
        status = "inactive";
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
