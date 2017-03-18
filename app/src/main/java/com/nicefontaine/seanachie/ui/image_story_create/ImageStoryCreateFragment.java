/*
 * Copyright 2017, Wolfhard Fehre
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nicefontaine.seanachie.ui.image_story_create;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.nicefontaine.seanachie.R;
import com.nicefontaine.seanachie.SeanachieApp;
import com.nicefontaine.seanachie.data.Session;
import com.nicefontaine.seanachie.data.models.Category;
import com.nicefontaine.seanachie.data.models.ImageStory;
import com.nicefontaine.seanachie.ui.BaseActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

import static android.support.design.widget.Snackbar.LENGTH_LONG;
import static android.widget.LinearLayout.VERTICAL;
import static com.nicefontaine.seanachie.ui.BaseActivity.NAVIGATION_IMAGE_STORIES;


public class ImageStoryCreateFragment extends Fragment implements
        ImageStoryCreateContract.View,
        ImageStoryCreateAdapter.OnImageClickedListener {

    private Context context;
    private List<Category> categories;
    private ImageStoryCreateAdapter adapter;
    private ImageStoryCreateContract.Presenter presenter;
    private int currentPosition;
    private ImageStory currentStory;

    @Inject protected Session session;

    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.collapsing) protected CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.f_image_story_create_coordinator) protected CoordinatorLayout coordinator;
    @BindView(R.id.f_image_story_create_recycler) protected RecyclerView recycler;
    @BindView(R.id.f_image_story_create_backdrop) protected ImageView photo;

    public static ImageStoryCreateFragment newInstance() {
        return new ImageStoryCreateFragment();
    }

    public ImageStoryCreateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_story_create, container, false);
        ((SeanachieApp) context.getApplicationContext()).getAppComponent().inject(this);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((BaseActivity) context).initNavigationDrawer(toolbar);
        collapsingToolbarLayout.setTitle(getString(R.string.navigation_image_story_create));
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        presenter.result(requestCode, resultCode, intent);
    }

    @OnClick(R.id.f_image_story_create_photo_card)
    public void takePicture() {
        try {
            presenter.camera(getActivity());
        } catch (IOException e) {
            Timber.e(e);
        }
    }

    @OnClick(R.id.f_image_story_send_fab)
    public void sendImageStory() {
        Uri uri = Uri.fromFile(new File(currentStory.getImagePath()));
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND)
                .setType("application/image")
                .putExtra(android.content.Intent.EXTRA_SUBJECT, currentStory.getCategoriesContent())
                .putExtra(android.content.Intent.EXTRA_TEXT, "... send with Seanachie")
                .putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }

    @OnClick(R.id.f_image_story_create_fab)
    public void createImageStory() {
        String name = categories.get(0).getValue();
        String story = categories.get(categories.size() - 1).getValue();
        if (name == null) {
            makeSnackbar(R.string.story_create_no_name);
        } else if (story == null) {
            makeSnackbar(R.string.story_create_no_story);
        } else {
            presenter.createImageStory(currentStory);
        }
    }

    @Override
    public void loadImageStory(ImageStory imageStory) {
        ImageStory cached = session.get(R.string.pref_cached_image_story, new ImageStory());
        currentStory = cached.isEmpty() ? imageStory : cached;
        categories = currentStory.getForm().getCategories();
        String path = currentStory.getImagePath();
        if (path != null) presenter.displayPhoto(path, 300);
    }

    @Override
    public void noData() {
        makeSnackbar(R.string.no_data);
    }

    @Override
    public void initRecycler() {
        if (categories == null) categories = new ArrayList<>();
        this.adapter = new ImageStoryCreateAdapter(this, context, categories);
        recycler.setLayoutManager(new LinearLayoutManager(context, VERTICAL, false));
        recycler.setAdapter(adapter);
    }

    @Override
    public void updateRecycler() {
        adapter.setCategories(categories);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setStory(String story) {
        if (story.equals("")) makeSnackbar(R.string.story_create_empty_story);
        categories.get(currentPosition).setValue(story);
        cache();
    }

    @Override
    public void setPhoto(Bitmap bitmap) {
        photo.setImageBitmap(bitmap);
    }

    @Override
    public void loadImagePath(String path) {
        currentStory.image(path);
        cache();
    }

    private void cache() {
        currentStory.getForm().categories(categories);
        session.store(R.string.pref_cached_image_story, currentStory);
    }

    @Override
    public void setPresenter(ImageStoryCreateContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void finish() {
        ((BaseActivity) context).changeContent(NAVIGATION_IMAGE_STORIES);
    }

    @Override
    public void onEditText(int position, String text) {
        this.currentPosition = position;
        setStory(text);
    }

    @Override
    public void onSpeechToText(int position) {
        presenter.story(getActivity(), getString(R.string.story_create_speech_to_text));
        this.currentPosition = position;
    }

    private void makeSnackbar(int text) {
        Snackbar.make(coordinator, text, LENGTH_LONG).show();
    }
}
