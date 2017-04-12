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


import android.content.Context;
import android.support.annotation.NonNull;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.nicefontaine.seanachie.data.sources.DatabaseHelper;
import com.nicefontaine.seanachie.data.models.ImageStory;
import com.nicefontaine.seanachie.data.sources.DataSource;

import java.util.List;
import java.util.concurrent.Callable;

import static dagger.internal.Preconditions.checkNotNull;


public class ImageStoriesLocalDataSource implements DataSource<ImageStory> {

    private static ImageStoriesLocalDataSource instance;
    private RuntimeExceptionDao<ImageStory, Integer> imageStoryDao;

    public static ImageStoriesLocalDataSource getInstance(@NonNull Context context) {
        if (instance == null) {
            instance = new ImageStoriesLocalDataSource(context);
        }
        return instance;
    }

    private ImageStoriesLocalDataSource(@NonNull Context context) {
        checkNotNull(context);
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        imageStoryDao = databaseHelper.getImageStoryDao();
    }

    @Override
    public void getData(@NonNull LoadDataCallback<ImageStory> callback) {
        List<ImageStory> imageStories = imageStoryDao.queryForAll();
        if (imageStories.isEmpty()) {
            callback.noData();
        } else {
            callback.onDataLoaded(imageStories);
        }
    }

    @Override
    public void getElement(@NonNull Integer petId, @NonNull LoadElementCallback<ImageStory> callback) {
        ImageStory imageStory = imageStoryDao.queryForId(petId);
        if (imageStory == null) {
            callback.noElement();
        } else {
            callback.onElementLoaded(imageStory);
        }
    }

    @Override
    public void create(@NonNull ImageStory imageStory) {
        imageStoryDao.create(imageStory);
    }

    @Override
    public void edit(@NonNull ImageStory imageStory) {
        imageStoryDao.deleteById(imageStory.getId());
        imageStoryDao.create(imageStory);
    }

    @Override
    public void delete(@NonNull Integer petId) {
        imageStoryDao.deleteById(petId);
    }

    @Override
    public void swap(@NonNull List<ImageStory> imageStories) {
        imageStoryDao.delete(imageStories);
        imageStoryDao.callBatchTasks(new Callable<Void>() {

            public Void call() throws Exception {
                for (ImageStory imageStory : imageStories) {
                    imageStoryDao.create(imageStory);
                }
                return null;
            }
        });
    }

    @Override
    public void count(@NonNull LoadCountCallback callback) {
        callback.onCount(imageStoryDao.countOf());
    }
}
