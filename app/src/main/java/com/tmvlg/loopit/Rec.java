package com.tmvlg.loopit;

import android.widget.Button;
import android.widget.ImageButton;

public class Rec {
    public ImageButton btn;
    private String status;

    public Rec(ImageButton button){
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
