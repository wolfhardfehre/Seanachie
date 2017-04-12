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

package com.nicefontaine.seanachie.ui.imagestory;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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
import com.nicefontaine.seanachie.data.models.Category;
import com.nicefontaine.seanachie.data.models.ImageStory;
import com.nicefontaine.seanachie.ui.BaseActivity;
import com.nicefontaine.seanachie.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.support.design.widget.Snackbar.LENGTH_LONG;
import static android.widget.LinearLayout.VERTICAL;
import static com.nicefontaine.seanachie.ui.BaseActivity.NAVIGATION_IMAGE_STORIES;
import static com.nicefontaine.seanachie.utils.Utils.isNull;


public class ImageStoryFragment extends Fragment implements
        ImageStoryContract.View,
        ImageStoryAdapter.OnImageClickedListener, Toolbar.OnMenuItemClickListener {

    private static final String REPOSITORY = "com.nicefontaine.seanachie.fileprovider";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int REQUEST_SPEECH_TO_TEXT = 2;
    private static final int WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    private static final int RECORD_AUDIO_PERMISSION = 2;

    private Context context;
    private List<Category> categories;
    private ImageStoryAdapter adapter;
    private ImageStoryContract.Presenter presenter;
    private int currentPosition;
    private ImageStory currentStory;

    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.collapsing) protected CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.f_imagestory_coordinator) protected CoordinatorLayout coordinator;
    @BindView(R.id.f_imagestory_recycler) protected RecyclerView recycler;
    @BindView(R.id.f_imagestory_backdrop) protected ImageView photo;

    public static ImageStoryFragment getInstance() {
        return new ImageStoryFragment();
    }

    public ImageStoryFragment() {
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
        View view = inflater.inflate(R.layout.fragment_imagestory, container, false);
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
        inflater.inflate(R.menu.imagestory_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onStart() {
        super.onStart();
        ((BaseActivity) context).initNavigationDrawer(toolbar);
        toolbarLayout.setTitle(getString(R.string.navigation_image_story_create));
        toolbar.setOnMenuItemClickListener(this);
        initRecycler();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    public void initRecycler() {
        List<Category> categories = new ArrayList<>();
        adapter = new ImageStoryAdapter(this, context, categories);
        recycler.setLayoutManager(new LinearLayoutManager(context, VERTICAL, false));
        recycler.setAdapter(adapter);
    }

    @Override
    public void setPresenter(ImageStoryContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        presenter.result(requestCode, resultCode, intent);
    }

    @OnClick(R.id.f_imagestory_photo_card)
    public void takePhoto() {
        boolean permission = askPermission(WRITE_EXTERNAL_STORAGE);
        if (permission) {
            photo();
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] {WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE_PERMISSION);
        }
    }

    @Override
    public void loadImageStory(ImageStory imageStory) {
        currentStory = imageStory;
        categories = currentStory.getForm().getCategories();
        String path = currentStory.getImagePath();
        if (path != null) {
            presenter.displayPhoto(path, 300);
        }
    }

    @Override
    public void updateRecycler(List<Category> categories) {
        adapter.setCategories(categories);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onEditText(int position, String text) {
        currentPosition = position;
        cacheText(text);
    }

    @Override
    public void cacheText(String story) {
        if (story.isEmpty()) notify(R.string.story_create_empty_story);
        categories.get(currentPosition).setValue(story);
        cache();
    }

    @Override
    public void setPhoto(Bitmap bitmap) {
        photo.setImageBitmap(bitmap);
    }

    public void cacheImagePath(String path) {
        currentStory.image(path);
        cache();
    }

    private void cache() {
        currentStory.categories(categories);
        presenter.cache(currentStory);
    }

    @Override
    public void finish() {
        ((BaseActivity) context).changeContent(NAVIGATION_IMAGE_STORIES);
    }

    @Override
    public void onSpeechToText(int position) {
        currentPosition = position;
        if (askPermission(RECORD_AUDIO)) {
            story();
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
                    photo();
                } else {
                    notify(R.string.story_create_write_permission);
                }
                break;
            case RECORD_AUDIO_PERMISSION:
                if (isGranted(results)) {
                    story();
                } else {
                    notify(R.string.story_create_audio_permission);
                }
                break;
        }
    }

    private boolean isGranted(@NonNull int[] results) {
        return results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void notify(int text) {
        Snackbar.make(coordinator, text, LENGTH_LONG).show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.m_image_story_send:
                sendEmail(); break;
            case R.id.m_image_story_save:
                presenter.save(currentStory); break;
        }
        return true;
    }

    public void sendEmail() {
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND)
                .setType("application/image")
                .putExtra(android.content.Intent.EXTRA_SUBJECT,
                        currentStory.getCategoriesContent())
                .putExtra(android.content.Intent.EXTRA_TEXT,
                        getString(R.string.story_create_send_with));
        String path = currentStory.getImagePath();
        if (!isNull(path)) {
            emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(path)));
        }
        startActivity(Intent.createChooser(emailIntent, getString(R.string.story_create_sending)));
    }

    private void photo() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            try {
                File photoFile = FileUtils.createImageFile();
                cacheImagePath(photoFile.getAbsolutePath());
                Uri photoURI = FileProvider.getUriForFile(getActivity(), REPOSITORY, photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                getActivity().startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } catch (IOException e) {
                Timber.e(e);
            }
        }
    }

    public void story() {
        Intent intent = getSpeechIntent(getString(R.string.story_create_speech_to_text));
        getActivity().startActivityForResult(intent, REQUEST_SPEECH_TO_TEXT);
    }

    private Intent getSpeechIntent(String text) {
        return new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                .putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                .putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                .putExtra(RecognizerIntent.EXTRA_PROMPT, text);
    }
}
