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
import com.nicefontaine.seanachie.data.sources.DataSource;

import java.util.List;

import static dagger.internal.Preconditions.checkNotNull;


public class ImageStoriesRepository implements DataSource<ImageStory> {

    private static ImageStoriesRepository instance;

    private DataSource<ImageStory> imageStoriesLocalDataSource;

    public static ImageStoriesRepository getInstance(@NonNull DataSource<ImageStory> imageStoriesLocalDataSource) {
        if (instance == null) {
            instance = new ImageStoriesRepository(imageStoriesLocalDataSource);
        }
        return instance;
    }

    private ImageStoriesRepository(@NonNull DataSource<ImageStory> imageStoriesLocalDataSource) {
        this.imageStoriesLocalDataSource = checkNotNull(imageStoriesLocalDataSource);
    }

    @Override
    public void getData(@NonNull LoadDataCallback<ImageStory> callback) {
        imageStoriesLocalDataSource.getData(callback);
    }

    @Override
    public void getElement(@NonNull Integer id, @NonNull LoadElementCallback<ImageStory> callback) {
        imageStoriesLocalDataSource.getElement(id, callback);
    }

    @Override
    public void create(@NonNull ImageStory imageStory) {
        imageStoriesLocalDataSource.create(imageStory);
    }

    @Override
    public void edit(@NonNull ImageStory imageStory) {
        imageStoriesLocalDataSource.edit(imageStory);
    }

    @Override
    public void delete(@NonNull Integer id) {
        imageStoriesLocalDataSource.delete(id);
    }

    @Override
    public void swap(@NonNull List<ImageStory> imageStories) {
        imageStoriesLocalDataSource.swap(imageStories);
    }

    @Override
    public void count(@NonNull LoadCountCallback callback) {
        imageStoriesLocalDataSource.count(callback);
    }
}
