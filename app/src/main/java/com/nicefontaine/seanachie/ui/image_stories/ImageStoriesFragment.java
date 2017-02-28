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
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
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
import com.nicefontaine.seanachie.data.models.Form;
import com.nicefontaine.seanachie.data.models.ImageStory;
import com.nicefontaine.seanachie.ui.BaseActivity;
import com.nicefontaine.seanachie.ui.ItemTouchCallback;
import com.nicefontaine.seanachie.ui.dialogs.FormPickerDialogFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.support.design.widget.Snackbar.LENGTH_LONG;
import static android.widget.LinearLayout.VERTICAL;


public class ImageStoriesFragment extends Fragment implements
        ImageStoriesContract.View,
        ItemTouchCallback.OnItemTouchListener {

    private static final String FORM_PICKER = "form_picker";

    private Context context;
    private List<ImageStory> imageStories;
    private ImageStoriesAdapter adapter;
    private ImageStoriesContract.Presenter presenter;

    @Inject protected Session session;

    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.f_base_coordinator) public CoordinatorLayout coordinator;
    @BindView(R.id.f_base_recycler) protected RecyclerView recycler;

    public static ImageStoriesFragment newInstance() {
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
        ((BaseActivity) context).initNavigationDrawer(toolbar);
        toolbar.setTitle(R.string.navigation_image_stories);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @OnClick(R.id.f_base_fab)
    public void addImageStory() {
        session.removeSetting(R.string.pref_cached_image_story);
        presenter.addImageStory();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        imageStories = adapter.getImageStories();
        presenter.itemMoved(imageStories);
        Snackbar.make(coordinator, "Elements reordered", LENGTH_LONG).show();
    }

    @Override
    public void onItemDismiss(int position) {
        ImageStory imageStory = imageStories.get(position);
        Integer petId = imageStory.getId();
        presenter.itemRemoved(petId);
        Snackbar.make(coordinator, "Element deleted", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void setPresenter(ImageStoriesContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void loadForms(List<Form> forms) {
        DialogFragment df = FormPickerDialogFragment.newInstance(forms);
        df.show(getActivity().getSupportFragmentManager(), FORM_PICKER);
    }

    @Override
    public void loadImageStories(List<ImageStory> imageStories) {
        this.imageStories = imageStories;
    }

    @Override
    public void noData() {
        Snackbar.make(coordinator, "No data", LENGTH_LONG).show();
    }
    @Override
    public void initRecycler() {
        if (imageStories == null) imageStories = new ArrayList<>();
        this.adapter = new ImageStoriesAdapter(context, imageStories);
        recycler.setLayoutManager(new LinearLayoutManager(context, VERTICAL, false));
        recycler.setAdapter(adapter);
        initItemTouchCallback();
    }

    @Override
    public void updateRecycler() {
        adapter.setImageStories(imageStories);
        adapter.notifyDataSetChanged();
    }

    private void initItemTouchCallback() {
        ItemTouchHelper.Callback callback = new ItemTouchCallback(this, adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recycler);
    }
}
