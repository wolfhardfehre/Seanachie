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

package com.nicefontaine.seanachie.ui;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.nicefontaine.seanachie.R;
import com.nicefontaine.seanachie.SeanachieApp;
import com.nicefontaine.seanachie.data.models.Form;
import com.nicefontaine.seanachie.data.sources.categories.CategoriesRepository;
import com.nicefontaine.seanachie.data.sources.forms.FormsRepository;
import com.nicefontaine.seanachie.data.sources.image_stories.ImageStoriesRepository;
import com.nicefontaine.seanachie.ui.categories.CategoriesFragment;
import com.nicefontaine.seanachie.ui.categories.CategoriesPresenter;
import com.nicefontaine.seanachie.ui.dialogs.FormPickerDialogFragment;
import com.nicefontaine.seanachie.ui.formcreate.FormCreateFragment;
import com.nicefontaine.seanachie.ui.formcreate.FormCreatePresenter;
import com.nicefontaine.seanachie.ui.forms.FormsFragment;
import com.nicefontaine.seanachie.ui.forms.FormsPresenter;
import com.nicefontaine.seanachie.ui.image_story_create.ImageStoryCreateFragment;
import com.nicefontaine.seanachie.ui.image_story_create.ImageStoryCreatePresenter;
import com.nicefontaine.seanachie.ui.image_stories.ImageStoriesFragment;
import com.nicefontaine.seanachie.ui.image_stories.ImageStoriesPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BaseActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        FormPickerDialogFragment.OnFormSelectedListener {

    private static final String SELECTED_FRAGMENT = "selected_fragment";

    public static final int NAVIGATION_CATEGORY = R.id.navigation_categories;
    public static final int NAVIGATION_FORMS = R.id.navigation_forms;
    public static final int NAVIGATION_NEW_FROM = R.string.navigation_form_create;
    public static final int NAVIGATION_IMAGE_STORIES = R.id.navigation_pets;
    public static final int NAVIGATION_NEW_IMAGE_STORIES = R.string.navigation_pet_create;
    public static final int NAVIGATION_ABOUT = R.id.navigation_about;

    @Inject protected FormsRepository formsRepository;
    @Inject protected ImageStoriesRepository imageStoriesRepository;
    @Inject protected CategoriesRepository categoriesRepository;

    @BindView(R.id.a_base_drawer_layout) protected DrawerLayout drawer;
    @BindView(R.id.a_base_navigation_view) protected NavigationView navigator;

    private FragmentManager fragmentManager;
    private int selectedFragment = NAVIGATION_IMAGE_STORIES;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        ((SeanachieApp) getApplicationContext()).getAppComponent().inject(this);
        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();
    }

    public void initNavigationDrawer(Toolbar toolbar) {
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        setSupportActionBar(toolbar);
        navigator.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        selectedFragment = savedInstanceState.getInt(SELECTED_FRAGMENT);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_FRAGMENT, selectedFragment);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeContent(selectedFragment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        changeContent(item.getItemId());
        return true;
    }

    public void changeContent(int item) {
        switch (item) {
            case NAVIGATION_FORMS:
                setupFormsFragment();
                break;
            case NAVIGATION_NEW_FROM:
                setupFormCreateFragment();
                break;
            case NAVIGATION_IMAGE_STORIES:
                setupPetsFragment();
                break;
            case NAVIGATION_CATEGORY:
                setupCategoryFragment();
                break;
            case NAVIGATION_ABOUT:
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        selectedFragment = item;
    }

    private void setupFormCreateFragment() {
        FormCreateFragment formCreateFragment = FormCreateFragment.newInstance();
        new FormCreatePresenter(formsRepository, categoriesRepository, formCreateFragment);
        replaceContainerFragment(formCreateFragment);
    }

    private void setupPetCreateFragment(Form form) {
        ImageStoryCreateFragment imageStoryCreateFragment = ImageStoryCreateFragment.newInstance();
        new ImageStoryCreatePresenter(form, imageStoriesRepository, imageStoryCreateFragment);
        replaceContainerFragment(imageStoryCreateFragment);
    }

    private void setupFormsFragment() {
        FormsFragment formsFragment = FormsFragment.newInstance();
        new FormsPresenter(formsRepository, formsFragment);
        replaceContainerFragment(formsFragment);
    }

    private void setupPetsFragment() {
        ImageStoriesFragment imageStoriesFragment = ImageStoriesFragment.newInstance();
        new ImageStoriesPresenter(imageStoriesRepository, formsRepository, imageStoriesFragment);
        replaceContainerFragment(imageStoriesFragment);
    }

    private void setupCategoryFragment() {
        CategoriesFragment categoriesFragment = CategoriesFragment.newInstance();
        new CategoriesPresenter(categoriesRepository, categoriesFragment);
        replaceContainerFragment(categoriesFragment);
    }

    private void replaceContainerFragment(Fragment baseFragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction()
                .replace(R.id.a_base_content, baseFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onFormSelected(Form form) {
        setupPetCreateFragment(form);
    }
}
