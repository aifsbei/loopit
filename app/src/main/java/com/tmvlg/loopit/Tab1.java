package com.tmvlg.loopit;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toolbar;

import com.sdsmdg.harjot.crollerTest.Croller;


import static android.content.Intent.getIntent;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Tab1.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Tab1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Tab1 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    Croller croller;
    RelativeLayout rLayout;
    Toolbar tb;
    ViewPager vp;
    ImageButton recBtn1;
    ImageButton recBtn2;
    ImageButton recBtn3;
    ImageButton recBtn4;
    ImageButton recBtn5;
    ImageButton recBtn6;
    ImageButton[] recBtns;
    int DIALOG_CROLLER = 1;
    private static final int DEFAULT_TOOLBAR_HEIGHT = 56;
    private static int toolBarHeight = -1;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Tab1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tab1.
     */
    // TODO: Rename and change types and number of parameters
    public static Tab1 newInstance(String param1, String param2) {
        Tab1 fragment = new Tab1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab1, container, false);
        rLayout = view.findViewById(R.id.RelativeLayout);
        tb = view.findViewById(R.id.toolbar);
        vp = view.findViewById(R.id.pager);
//        croller = view.findViewById(R.id.croller);
        recBtn1 = view.findViewById(R.id.imageButton7);
        recBtn2 = view.findViewById(R.id.imageButton8);
        recBtn3 = view.findViewById(R.id.imageButton9);
        recBtn4 = view.findViewById(R.id.imageButton10);
        recBtn5 = view.findViewById(R.id.imageButton11);
        recBtn6 = view.findViewById(R.id.imageButton12);
        recBtn1.setOnLongClickListener(recBtnLCL);
        recBtn2.setOnLongClickListener(recBtnLCL);
        recBtn3.setOnLongClickListener(recBtnLCL);
        recBtn4.setOnLongClickListener(recBtnLCL);
        recBtn5.setOnLongClickListener(recBtnLCL);
        recBtn6.setOnLongClickListener(recBtnLCL);
        recBtns = new ImageButton[6];
        recBtns[0] = recBtn1;
        recBtns[1] = recBtn2;
        recBtns[2] = recBtn3;
        recBtns[3] = recBtn4;
        recBtns[4] = recBtn5;
        recBtns[5] = recBtn6;
        registerForContextMenu(recBtn1);
        registerForContextMenu(recBtn2);
        registerForContextMenu(recBtn3);
        registerForContextMenu(recBtn4);
        registerForContextMenu(recBtn5);
        registerForContextMenu(recBtn6);
        return view;
    }





    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        switch (v.getId()){
            case R.id.imageButton7:
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()){

        }

        return super.onContextItemSelected(item);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    View.OnLongClickListener recBtnLCL = new View.OnLongClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public boolean onLongClick(View v) {
            final Dialog d = new Dialog(getActivity(), R.style.PauseDialog);
            //  d.getWindow().setBackgroundDrawable(R.color.action_bar_bg);
            d.requestWindowFeature(Window.FEATURE_NO_TITLE);
            d.setContentView(R.layout.dialog_croller);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(d.getWindow().getAttributes());

            DisplayMetrics displaymetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int width = (int) ((int)displaymetrics.widthPixels * 0.75);
            int height = (int) ((int)displaymetrics.heightPixels * 0.55);
            d.getWindow().setLayout(width,height);
            d.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            d.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND); //hz
//            moveViewToScreenCenter(v);
            d.show();
            return false;
        }
    };


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


















    public static int getToolBarHeight(Context context) {
        if (toolBarHeight > 0) {
            return toolBarHeight;
        }
        final Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier("action_bar_size", "dimen", "android");
        toolBarHeight = resourceId > 0 ?
                resources.getDimensionPixelSize(resourceId) :
                (int) convertDpToPixel(context, DEFAULT_TOOLBAR_HEIGHT);
        return toolBarHeight;
    }

    public static float convertDpToPixel(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }


    private void moveViewToScreenCenter( View view )
    {
        RelativeLayout root = (RelativeLayout) getActivity().findViewById( R.id.RelativeLayout );
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics( dm );
        int statusBarOffset = dm.heightPixels - root.getMeasuredHeight();

        int originalPos[] = new int[2];
        view.getLocationOnScreen( originalPos );

        int h = view.getHeight();
        int w = view.getWidth();

        int xDest = dm.widthPixels/2;
        xDest -= (view.getMeasuredWidth()/2);
        int yDest = dm.heightPixels/2 - (view.getMeasuredHeight()/2);

        TranslateAnimation anim = new TranslateAnimation( 0, xDest - originalPos[0] , 0, yDest - originalPos[1] );
        ScaleAnimation anim2 = new ScaleAnimation(1.0f, 2.0f, 1.0f, 2.0f, 0.1f, 0.1f);
        AnimationSet as = new AnimationSet(true);
        as.setFillAfter(false);
        as.setInterpolator(new LinearInterpolator());
        anim2.setDuration(400);
        as.addAnimation(anim2);
        anim.setDuration(400);
        as.addAnimation(anim);
        view.startAnimation(as);
    }





}
