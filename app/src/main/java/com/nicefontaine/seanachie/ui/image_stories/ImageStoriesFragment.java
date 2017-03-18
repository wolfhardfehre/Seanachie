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

package com.nicefontaine.seanachie.ui.image_stories;


import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nicefontaine.seanachie.R;
import com.nicefontaine.seanachie.SeanachieApp;
import com.nicefontaine.seanachie.data.Session;
import com.nicefontaine.seanachie.data.models.ImageStory;
import com.nicefontaine.seanachie.ui.HomeActivity;
import com.nicefontaine.seanachie.ui.ItemTouchCallback;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.support.design.widget.Snackbar.LENGTH_LONG;
import static android.widget.LinearLayout.VERTICAL;
import static com.nicefontaine.seanachie.ui.HomeActivity.NAVIGATION_PICK_FORM;


public class ImageStoriesFragment extends Fragment implements
        ImageStoriesContract.View,
        ItemTouchCallback.OnItemTouchListener {

    private static final int WRITE_EXTERNAL_STORAGE_PERMISSION = 1;

    private Context context;
    private List<ImageStory> imageStories;
    private ImageStoriesAdapter adapter;
    private ImageStoriesContract.Presenter presenter;

    @Inject protected Session session;

    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.f_base_coordinator) public CoordinatorLayout coordinator;
    @BindView(R.id.f_base_recycler) protected RecyclerView recycler;

    public static ImageStoriesFragment getInstance() {
        return new ImageStoriesFragment();
    }

    public ImageStoriesFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_base, container, false);
        ((SeanachieApp) context.getApplicationContext()).getAppComponent().inject(this);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((HomeActivity) context).initNavigationDrawer(toolbar);
        toolbar.setTitle(R.string.navigation_image_stories);
    }

    @Override
    public void setPresenter(ImageStoriesContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean permission = askPermission(WRITE_EXTERNAL_STORAGE);
        if (permission) {
            presenter.onResume();
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] {WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE_PERMISSION);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @OnClick(R.id.f_base_fab)
    public void addImageStory() {
        session.removeSetting(R.string.pref_cached_image_story);
        ((HomeActivity) context).changeContent(NAVIGATION_PICK_FORM);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        imageStories = adapter.getImageStories();
        presenter.itemMoved(imageStories);
        makeSnackbar(R.string.elements_reordered);
    }

    @Override
    public void onItemDismiss(int position) {
        ImageStory imageStory = imageStories.get(position);
        Integer petId = imageStory.getId();
        presenter.itemRemoved(petId);
        makeSnackbar(R.string.elements_deleted);
    }

    @Override
    public void loadImageStories(List<ImageStory> imageStories) {
        this.imageStories = imageStories;
    }

    @Override
    public void noData() {
        makeSnackbar(R.string.no_data);
    }

    @Override
    public void initRecycler() {
        if (imageStories == null) imageStories = new ArrayList<>();
        this.adapter = new ImageStoriesAdapter(context, imageStories);
        recycler.setLayoutManager(new LinearLayoutManager(context, VERTICAL, false));
        recycler.setAdapter(adapter);
        initItemTouchCallback();
    }

    private void initItemTouchCallback() {
        ItemTouchHelper.Callback callback = new ItemTouchCallback(this, adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recycler);
    }

    @Override
    public void updateRecycler() {
        adapter.setImageStories(imageStories);
        adapter.notifyDataSetChanged();
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
                    presenter.onResume();
                } else {
                    makeSnackbar(R.string.story_create_read_permission);
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
}
