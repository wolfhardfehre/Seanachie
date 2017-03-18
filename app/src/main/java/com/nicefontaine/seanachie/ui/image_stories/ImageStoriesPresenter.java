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

import com.nicefontaine.seanachie.data.models.ImageStory;
import com.nicefontaine.seanachie.data.sources.DataSource;
import com.nicefontaine.seanachie.data.sources.image_stories.ImageStoriesRepository;

import java.util.List;


public class ImageStoriesPresenter implements
        ImageStoriesContract.Presenter,
        DataSource.LoadDataCallback<ImageStory> {

    private final ImageStoriesRepository imageStoriesRepository;
    private final ImageStoriesContract.View view;
    private boolean isResumed = true;

    public ImageStoriesPresenter(@NonNull ImageStoriesRepository imageStoriesRepository,
                                 @NonNull ImageStoriesContract.View view) {
        this.imageStoriesRepository = imageStoriesRepository;
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void onResume() {
        imageStoriesRepository.getData(this);
    }

    @Override
    public void onRefresh() {
        imageStoriesRepository.getData(this);
    }

    @Override
    public void onPause() {
        isResumed = true;
    }

    @Override
    public void itemMoved(List<ImageStory> imageStories) {
        imageStoriesRepository.swap(imageStories);
    }

    @Override
    public void itemRemoved(Integer imageStoryId) {
        imageStoriesRepository.delete(imageStoryId);
    }

    @Override
    public void onDataLoaded(List<ImageStory> imageStories) {
        view.loadImageStories(imageStories);
        if (isResumed) {
            view.initRecycler();
        } else {
            view.updateRecycler();
        }
        isResumed = false;
    }

    @Override
    public void noData() {
        view.noData();
        if (isResumed) {
            view.initRecycler();
        } else {
            view.updateRecycler();
        }
        isResumed = false;
    }
}
