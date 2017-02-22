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


import android.support.annotation.NonNull;

import com.nicefontaine.seanachie.data.models.Form;
import com.nicefontaine.seanachie.data.models.ImageStory;
import com.nicefontaine.seanachie.data.sources.image_stories.ImageStoriesRepository;


public class ImageStoryCreatePresenter implements
        ImageStoryCreateContract.Presenter {

    private final ImageStoryCreateContract.View view;
    private final ImageStoriesRepository imageStoriesRepository;
    private Form form;

    public ImageStoryCreatePresenter(@NonNull Form form,
                                     @NonNull ImageStoriesRepository imageStoriesRepository,
                                     @NonNull ImageStoryCreateContract.View view) {
        this.imageStoriesRepository = imageStoriesRepository;
        this.form = form;
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void createImageStory(ImageStory imageStory) {
        imageStoriesRepository.createImageStory(imageStory);
        view.finish();
    }

    @Override
    public void onResume() {
        view.loadForm(form);
        view.initRecycler();
    }

    @Override
    public void onRefresh() {}

    @Override
    public void onPause() {}
}
