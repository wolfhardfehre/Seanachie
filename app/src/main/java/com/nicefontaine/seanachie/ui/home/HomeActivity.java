package com.nicefontaine.seanachie.ui.home;


import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.nicefontaine.seanachie.R;
import com.nicefontaine.seanachie.SeanachieApp;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;


public class HomeActivity extends AppCompatActivity implements HomeView {

    @BindView(R.id.a_home_textview)
    protected TextView mStory;

    @BindView(R.id.a_home_imageview)
    protected ImageView mPhoto;

    private HomePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //((SeanachieApp) getApplication()).getAppComponent().inject(this);
        ButterKnife.bind(this);
        presenter = new HomePresenterImpl(this);
    }

    @OnClick(R.id.a_home_cam_fab)
    public void takePhoto() {
        try {
            presenter.camera(this, mStory.getWidth());
        } catch (IOException e) {
            Timber.e(e);
        }
    }

    @OnClick(R.id.a_home_mic_fab)
    public void tellStory() {
        presenter.story(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        presenter.result(requestCode, resultCode, intent);
    }

    @Override
    public void showStory(String story) {
        mStory.setText(story);
    }

    @Override
    public void showImage(Bitmap bitmap) {
        mPhoto.setImageBitmap(bitmap);
    }
}
