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
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nicefontaine.seanachie.R;
import com.nicefontaine.seanachie.SeanachieApp;
import com.nicefontaine.seanachie.data.Session;
import com.nicefontaine.seanachie.data.models.Category;
import com.nicefontaine.seanachie.data.models.ImageStory;
import com.nicefontaine.seanachie.ui.HomeActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.support.design.widget.Snackbar.LENGTH_LONG;
import static android.widget.LinearLayout.VERTICAL;
import static com.nicefontaine.seanachie.ui.HomeActivity.NAVIGATION_IMAGE_STORIES;
import static com.nicefontaine.seanachie.utils.Utils.isNull;


public class ImageStoryCreateFragment extends Fragment implements
        ImageStoryCreateContract.View,
        ImageStoryCreateAdapter.OnImageClickedListener, Toolbar.OnMenuItemClickListener {

    private static final int WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    private static final int RECORD_AUDIO_PERMISSION = 2;

    private Context context;
    private List<Category> categories;
    private ImageStoryCreateAdapter adapter;
    private ImageStoryCreateContract.Presenter presenter;
    private int currentPosition;
    private ImageStory currentStory;
    private int storyId;

    @Inject protected Session session;

    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.collapsing) protected CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.f_image_story_create_coordinator) protected CoordinatorLayout coordinator;
    @BindView(R.id.f_image_story_create_recycler) protected RecyclerView recycler;
    @BindView(R.id.f_image_story_create_backdrop) protected ImageView photo;

    public static ImageStoryCreateFragment getInstance() {
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.image_story_create_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onStart() {
        super.onStart();
        ((HomeActivity) context).initNavigationDrawer(toolbar);
        toolbarLayout.setTitle(getString(R.string.navigation_image_story_create));
        toolbar.setOnMenuItemClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        storyId = session.get(R.string.pref_editable_image_story, -1);
        if (storyId == -1) {
            presenter.onResume();
        } else {
            presenter.onEditImageStory(storyId);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        presenter.result(requestCode, resultCode, intent);
    }

    @OnClick(R.id.f_image_story_create_photo_card)
    public void takePicture() {
        boolean permission = askPermission(WRITE_EXTERNAL_STORAGE);
        if (permission) {
            presenter.takePicture(getActivity());
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] {WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE_PERMISSION);
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
        if (story.isEmpty()) {
            makeSnackbar(R.string.story_create_empty_story);
        }
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
        currentStory.categories(categories);
        session.store(R.string.pref_cached_image_story, currentStory);
    }

    @Override
    public void setPresenter(ImageStoryCreateContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void finish() {
        ((HomeActivity) context).changeContent(NAVIGATION_IMAGE_STORIES);
    }

    @Override
    public void onEditText(int position, String text) {
        currentPosition = position;
        setStory(text);
    }

    @Override
    public void onSpeechToText(int position) {
        currentPosition = position;
        boolean permission = askPermission(RECORD_AUDIO);
        if (permission) {
            presenter.story(getActivity(), getString(R.string.story_create_speech_to_text));
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] {RECORD_AUDIO},
                    RECORD_AUDIO_PERMISSION);
        }
    }

    private boolean askPermission(String permission) {
        return ContextCompat.checkSelfPermission(getActivity(),
                permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int request, @NonNull String permissions[],
                                           @NonNull int[] results) {
        switch (request) {
            case WRITE_EXTERNAL_STORAGE_PERMISSION:
                if (isGranted(results)) {
                    presenter.takePicture(getActivity());
                } else {
                    makeSnackbar(R.string.story_create_write_permission);
                }
                break;
            case RECORD_AUDIO_PERMISSION:
                if (isGranted(results)) {
                    presenter.story(getActivity(), getString(R.string.story_create_speech_to_text));
                } else {
                    makeSnackbar(R.string.story_create_audio_permission);
                }
                break;
        }
    }

    private boolean isGranted(@NonNull int[] results) {
        return results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED;
    }

    private void makeSnackbar(int text) {
        Snackbar.make(coordinator, text, LENGTH_LONG).show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.m_image_story_send:
                sendEmail(); break;
            case R.id.m_image_story_save:
                saveImageStory(); break;
        }
        return true;
    }

    public void sendEmail() {
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND)
                .setType("application/image")
                .putExtra(android.content.Intent.EXTRA_SUBJECT,
                        currentStory.getCategoriesContent())
                .putExtra(android.content.Intent.EXTRA_TEXT,
                        getString(R.string.story_create_send_with));                ;
        String path = currentStory.getImagePath();
        if (!isNull(path)) {
            emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(path)));
        }
        startActivity(Intent.createChooser(emailIntent, getString(R.string.story_create_sending)));
    }

    public void saveImageStory() {
        String name = categories.get(0).getValue();
        String story = categories.get(categories.size() - 1).getValue();
        if (name == null) {
            makeSnackbar(R.string.story_create_no_name);
        } else if (story == null) {
            makeSnackbar(R.string.story_create_no_story);
        } else if (storyId == -1) {
            presenter.saveImageStory(currentStory);
        } else {
            presenter.editImageStory(currentStory);
        }
    }
}
