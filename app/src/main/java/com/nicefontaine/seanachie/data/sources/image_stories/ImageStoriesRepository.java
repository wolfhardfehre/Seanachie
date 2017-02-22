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

package com.nicefontaine.seanachie.data.sources.image_stories;


import android.support.annotation.NonNull;

import com.nicefontaine.seanachie.data.models.ImageStory;

import java.util.List;

import static dagger.internal.Preconditions.checkNotNull;


public class ImageStoriesRepository implements ImageStoryDataSource {

    private static ImageStoriesRepository instance;

    private ImageStoryDataSource imageStoriesLocalDataSource;

    public static ImageStoriesRepository getInstance(@NonNull ImageStoryDataSource imageStoriesLocalDataSource) {
        if (instance == null) {
            instance = new ImageStoriesRepository(imageStoriesLocalDataSource);
        }
        return instance;
    }

    private ImageStoriesRepository(@NonNull ImageStoryDataSource imageStoriesLocalDataSource) {
        this.imageStoriesLocalDataSource = checkNotNull(imageStoriesLocalDataSource);
    }

    @Override
    public void getImageStories(@NonNull LoadImageStoriesCallback callback) {
        imageStoriesLocalDataSource.getImageStories(callback);
    }

    @Override
    public void getImageStory(@NonNull Integer formId, @NonNull LoadImageStoryCallback callback) {
        imageStoriesLocalDataSource.getImageStory(formId, callback);
    }

    @Override
    public void createImageStory(@NonNull ImageStory imageStory) {
        imageStoriesLocalDataSource.createImageStory(imageStory);
    }

    @Override
    public void editImageStory(@NonNull ImageStory imageStory) {
        imageStoriesLocalDataSource.editImageStory(imageStory);
    }

    @Override
    public void deleteImageStory(@NonNull Integer petId) {
        imageStoriesLocalDataSource.deleteImageStory(petId);
    }

    @Override
    public void swapImageStory(@NonNull List<ImageStory> imageStories) {
        imageStoriesLocalDataSource.swapImageStory(imageStories);
    }
}
