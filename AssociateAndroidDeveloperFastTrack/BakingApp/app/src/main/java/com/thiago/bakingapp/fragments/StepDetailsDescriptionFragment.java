package com.thiago.bakingapp.fragments;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.thiago.bakingapp.R;
import com.thiago.bakingapp.bean.Step;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailsDescriptionFragment extends Fragment {

    @BindView(R.id.step_video)
    SimpleExoPlayerView mExoPlayerView;
    @BindView(R.id.step_description)
    TextView mTextViewDescription;

    private SimpleExoPlayer mExoPlayer;
    private Step mCurrentStep;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_description, container, false);
        ButterKnife.bind(this, view);
        initialize();
        return view;
    }

    private void initialize() {
        if (mCurrentStep != null) {
            setDescription();
            if (mCurrentStep.getVideoUrl() != null
                    && !"".equals(mCurrentStep.getVideoUrl())) {
                setPlayer();
            } else {
                releasePlayer();
                String image = mCurrentStep.getThumbnailURL();
                if (image != null && !"".equals(image)) {
                    if (!image.endsWith("mp4")) {
                        // TODO load from picasso
                    } else {
                        mExoPlayerView.setDefaultArtwork(BitmapFactory.decodeResource
                                (getResources(), R.drawable.default_food_step));
                    }
                } else {
                    mExoPlayerView.setDefaultArtwork(BitmapFactory.decodeResource
                            (getResources(), R.drawable.folder_food));
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
            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(mCurrentStep.getVideoUrl()),
                    new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void setDescription() {
        mTextViewDescription.setText(mCurrentStep.getDescription());
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
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        releasePlayer();
    }

    public void setStep(Step step) {
        this.mCurrentStep = step;
    }
}
