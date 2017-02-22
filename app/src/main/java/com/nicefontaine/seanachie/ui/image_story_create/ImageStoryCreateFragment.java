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
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nicefontaine.seanachie.R;
import com.nicefontaine.seanachie.SeanachieApp;
import com.nicefontaine.seanachie.data.models.Category;
import com.nicefontaine.seanachie.data.models.Form;
import com.nicefontaine.seanachie.data.models.ImageStory;
import com.nicefontaine.seanachie.ui.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.support.design.widget.Snackbar.LENGTH_LONG;
import static android.widget.LinearLayout.VERTICAL;
import static com.nicefontaine.seanachie.ui.BaseActivity.NAVIGATION_IMAGE_STORIES;


public class ImageStoryCreateFragment extends Fragment implements
        ImageStoryCreateContract.View {

    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.f_pet_create_coordinator) protected CoordinatorLayout coordinator;
    @BindView(R.id.f_pet_create_recycler) protected RecyclerView recycler;
    private Context context;
    private List<Category> categories;
    private ImageStoryCreateAdapter adapter;
    private ImageStoryCreateContract.Presenter presenter;


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
        View view = inflater.inflate(R.layout.fragment_pet_create, container, false);
        ((SeanachieApp) context.getApplicationContext()).getAppComponent().inject(this);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((BaseActivity) context).initNavigationDrawer(toolbar);
        toolbar.setTitle(R.string.navigation_pet_create);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @OnClick(R.id.recycler_item_pet_create_image)
    public void makePhoto() {

    }

    @OnClick(R.id.f_pet_create_fab)
    public void createPet() {
        ImageStory imageStory = new ImageStory();
        imageStory.setName("Rolf");
        imageStory.setStory("yeah!");
        presenter.createImageStory(imageStory);
    }

    @Override
    public void loadForm(Form form) {
        this.categories = new ArrayList<>();
        this.categories.add(new Category(0,getString(R.string.fragment_pet_create_name)));
        this.categories.addAll(form.getCategories());
        this.categories.add(new Category(categories.size(),getString(R.string.fragment_pet_create_story)));
    }

    @Override
    public void noData() {
        Snackbar.make(coordinator, "No data", LENGTH_LONG).show();
    }

    @Override
    public void initRecycler() {
        if (categories == null) categories = new ArrayList<>();
        this.adapter = new ImageStoryCreateAdapter(context, categories);
        recycler.setLayoutManager(new LinearLayoutManager(context, VERTICAL, false));
        recycler.setAdapter(adapter);
    }

    @Override
    public void updateRecycler() {
        adapter.setCategories(categories);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void intercept() {

    }

    @Override
    public void release() {

    }

    @Override
    public void setPresenter(ImageStoryCreateContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void finish() {
        ((BaseActivity) context).changeContent(NAVIGATION_IMAGE_STORIES);
    }
}
