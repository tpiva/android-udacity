package com.thiago.bakingapp.fragments;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.thiago.bakingapp.R;
import com.thiago.bakingapp.bean.Step;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailsDescriptionFragment extends Fragment {

    private static final String CURRENT_STEP = "current_step_fragment";

    @BindView(R.id.step_video)
    SimpleExoPlayerView mExoPlayerView;
    @Nullable
    @BindView(R.id.step_description)
    TextView mTextViewDescription;
    @BindView(R.id.step_image)
    ImageView mImageView;
    @BindBool(R.bool.tablet)
    boolean mIsTable;

    private SimpleExoPlayer mExoPlayer;
    private Step mCurrentStep;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_description, container, false);
        ButterKnife.bind(this, view);
        if (savedInstanceState != null) {
            mCurrentStep = savedInstanceState.getParcelable(CURRENT_STEP);
        }

        initialize();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mCurrentStep = savedInstanceState.getParcelable(CURRENT_STEP);
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
                && !mIsTable) {
            getActivity().getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    private void initialize() {
        if (mCurrentStep != null) {
            setDescription();
            if (mCurrentStep.getVideoUrl() != null
                    && !"".equals(mCurrentStep.getVideoUrl())) {
                mExoPlayerView.setVisibility(View.VISIBLE);
                mImageView.setVisibility(View.GONE);
                setPlayer();
            } else {
                releasePlayer();
                String image = mCurrentStep.getThumbnailURL();
                mExoPlayerView.setVisibility(View.GONE);
                mImageView.setVisibility(View.VISIBLE);
                if (image != null && !"".equals(image)) {
                    Picasso.with(getContext())
                            .load(image)
                            .placeholder(R.drawable.default_food)
                            .error(R.drawable.default_food)
                            .into(mImageView);
                } else {
                    mImageView.setImageResource(R.drawable.default_food);
                }
            }
        }
    }

    /**
     * Initialize ExoPlayer.
     */
    private void setPlayer() {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mExoPlayerView.setPlayer(mExoPlayer);
            mExoPlayerView.bringToFront();
            String userAgent = Util.getUserAgent(getContext(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(mCurrentStep.getVideoUrl()),
                    new DefaultDataSourceFactory(
                            getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void setDescription() {
        if (mTextViewDescription != null) {
            mTextViewDescription.setText(mCurrentStep.getDescription());
        }
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        releasePlayer();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(CURRENT_STEP, mCurrentStep);
        super.onSaveInstanceState(outState);
    }

    public void setStep(Step step) {
        this.mCurrentStep = step;
    }
}
