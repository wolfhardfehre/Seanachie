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


import android.support.annotation.NonNull;

import com.nicefontaine.seanachie.data.models.Form;
import com.nicefontaine.seanachie.data.models.ImageStory;
import com.nicefontaine.seanachie.data.sources.forms.FormsDataSource;
import com.nicefontaine.seanachie.data.sources.forms.FormsRepository;
import com.nicefontaine.seanachie.data.sources.image_stories.ImageStoriesRepository;
import com.nicefontaine.seanachie.data.sources.image_stories.ImageStoryDataSource;

import java.util.List;


public class ImageStoriesPresenter implements
        ImageStoriesContract.Presenter,
        ImageStoryDataSource.LoadImageStoriesCallback,
        FormsDataSource.LoadFormsCallback {

    private final ImageStoriesRepository imageStoriesRepository;
    private final ImageStoriesContract.View view;
    private final FormsRepository formsRepository;
    private boolean isResumed = true;

    public ImageStoriesPresenter(@NonNull ImageStoriesRepository imageStoriesRepository,
                                 @NonNull FormsRepository formsRepository,
                                 @NonNull ImageStoriesContract.View view) {
        this.imageStoriesRepository = imageStoriesRepository;
        this.formsRepository = formsRepository;
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void onResume() {
        imageStoriesRepository.getImageStories(this);
    }

    @Override
    public void onRefresh() {
        imageStoriesRepository.getImageStories(this);
    }

    @Override
    public void onPause() {
        isResumed = true;
    }

    @Override
    public void addPet() {
        formsRepository.getForms(this);
    }

    @Override
    public void itemMoved(List<ImageStory> imageStories) {
        imageStoriesRepository.swapImageStory(imageStories);
    }

    @Override
    public void itemRemoved(Integer imageStoryId) {
        imageStoriesRepository.deleteImageStory(imageStoryId);
    }

    @Override
    public void onImageStoriesLoaded(List<ImageStory> imageStories) {
        view.loadPets(imageStories);
        if (isResumed) {
            view.initRecycler();
        } else {
            view.updateRecycler();
        }
        isResumed = false;
    }

    @Override
    public void onNoImageStories() {
        view.noData();
        if (isResumed) {
            view.initRecycler();
        } else {
            view.updateRecycler();
        }
        isResumed = false;
    }

    @Override
    public void onFormsLoaded(List<Form> forms) {
        view.loadForms(forms);
    }

    @Override
    public void onNoForms() {
        view.noData();
    }
}
