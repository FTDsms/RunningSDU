package com.sdu.runningsdu.Find;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sdu.runningsdu.R;


/**
 * Created by FTDsm on 2018/5/14.
 */

public class FindFragment extends Fragment {

    private LinearLayout trend;
    private LinearLayout scan;
    private LinearLayout shake;
    private LinearLayout look;
    private LinearLayout search;
    private LinearLayout nearby;
    private LinearLayout shop;
    private LinearLayout game;
    private LinearLayout smallProgram;

    public FindFragment() {

    }

    @Override @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_find,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {
        trend = getView().findViewById(R.id.trend);
        scan = getView().findViewById(R.id.scan);
        shake = getView().findViewById(R.id.shake);
        look = getView().findViewById(R.id.look);
        search = getView().findViewById(R.id.search);
        nearby = getView().findViewById(R.id.nearby);
        shop = getView().findViewById(R.id.shop);
        game = getView().findViewById(R.id.game);
        smallProgram = getView().findViewById(R.id.small_program);

        trend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        shake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        nearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        smallProgram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

}
