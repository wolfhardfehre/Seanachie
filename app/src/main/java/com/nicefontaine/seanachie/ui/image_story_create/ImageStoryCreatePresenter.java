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


import android.content.Intent;
import android.graphics.Bitmap;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;

import com.nicefontaine.seanachie.R;
import com.nicefontaine.seanachie.data.Session;
import com.nicefontaine.seanachie.data.models.Category;
import com.nicefontaine.seanachie.data.models.Form;
import com.nicefontaine.seanachie.data.models.ImageStory;
import com.nicefontaine.seanachie.data.sources.DataSource;
import com.nicefontaine.seanachie.data.sources.image_stories.ImageStoriesRepository;
import com.nicefontaine.seanachie.utils.ImageUtils;

import java.io.IOException;
import java.util.List;

import timber.log.Timber;

import static android.app.Activity.RESULT_OK;
import static com.nicefontaine.seanachie.ui.image_story_create.ImageStoryCreateFragment.REQUEST_SPEECH_TO_TEXT;
import static com.nicefontaine.seanachie.utils.Utils.isNull;


public class ImageStoryCreatePresenter implements
        ImageStoryCreateContract.Presenter,
        DataSource.LoadCountCallback, DataSource.LoadElementCallback<ImageStory> {

    private final ImageStoryCreateContract.View view;
    private final ImageStoriesRepository imageStoriesRepository;
    private final Session session;
    private Form form;
    private int storyId;
    private ImageStory currentStory;

    public ImageStoryCreatePresenter(@NonNull Form form,
                                     @NonNull ImageStoriesRepository imageStoriesRepository,
                                     @NonNull Session session,
                                     @NonNull ImageStoryCreateContract.View view) {
        this.imageStoriesRepository = imageStoriesRepository;
        this.form = form;
        this.view = view;
        this.session = session;
        this.view.setPresenter(this);
    }

    @Override
    public void onResume() {
        storyId = session.get(R.string.pref_editable_image_story, -1);
        if (storyId == -1) {
            imageStoriesRepository.count(this);
        } else {
            imageStoriesRepository.getElement(storyId, this);
        }
    }

    @Override
    public void onRefresh() {}

    @Override
    public void onPause() {}

    @Override
    public void save(ImageStory story) {
        List<Category> categories = story.getForm().getCategories();
        String name = categories.get(0).getValue();
        String text = categories.get(categories.size() - 1).getValue();
        if (name == null) {
            view.notify(R.string.story_create_no_name);
        } else if (text == null) {
            view.notify(R.string.story_create_no_story);
        } else if (storyId == -1) {
            imageStoriesRepository.create(story);
            view.finish();
        } else {
            imageStoriesRepository.edit(story);
            view.finish();
        }
    }

    @Override
    public void cache(ImageStory current) {
        session.store(R.string.pref_cached_image_story, current);
    }

    @Override
    public void result(int requestCode, int resultCode, Intent intent) {
        if (okNotNull(requestCode, resultCode, intent)) {
            setStory(intent);
        }
    }

    private boolean okNotNull(int requestCode, int resultCode, Intent intent) {
        return requestCode == REQUEST_SPEECH_TO_TEXT && resultCode == RESULT_OK && !isNull(intent);
    }

    private void setStory(Intent intent) {
        List<String> result = intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        view.cacheText(result.get(0));
    }

    @Override
    public void displayPhoto(String path, int width) {
        try {
            setBitmap(path, width);
        } catch (IOException e) {
            Timber.e(e);
        }
    }

    private void setBitmap(String path, int width) throws IOException {
        Bitmap bitmap = ImageUtils.loadImage(path, width);
        bitmap = ImageUtils.rotateImage(bitmap, path);
        view.setPhoto(bitmap);
    }

    @Override
    public void onCount(long count) {
        loadImageStory(new ImageStory().position((int) count).form(form));
    }

    @Override
    public void onElementLoaded(ImageStory element) {
        loadImageStory(element);
    }

    private void loadImageStory(ImageStory story) {
        ImageStory cached = session.get(R.string.pref_cached_image_story, new ImageStory());
        currentStory = cached.isEmpty() ? story : cached;
        view.loadImageStory(currentStory);
        view.updateRecycler(currentStory.getForm().getCategories());
    }

    @Override
    public void noElement() {
        view.notify(R.string.no_data);
    }
}
