package com.thiago.bakingapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.thiago.bakingapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailsNavigateFragment extends Fragment {

    @BindView(R.id.step_navigation_previous)
    Button mBtnPrevious;
    @BindView(R.id.step_navigation_next)
    Button mBtnNext;

    private OnChangeStepListener mCallback;
    private int mCurrentPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_navigation, container, false);
        ButterKnife.bind(this, view);

        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mBtnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnChangeStepListener) context;
        } catch (ClassCastException e) {

        }
    }

    public void setCurrentPosition(int position) {
        this.mCurrentPosition = position;
    }

    /**
     *
     */
    public interface OnChangeStepListener {
        void nextStepSelected(int nextPostion);

        void previousStepSelected(int previousPosition);
    }
}
