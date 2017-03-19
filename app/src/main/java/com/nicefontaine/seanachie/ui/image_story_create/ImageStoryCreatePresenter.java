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


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;

import com.nicefontaine.seanachie.data.models.Form;
import com.nicefontaine.seanachie.data.models.ImageStory;
import com.nicefontaine.seanachie.data.sources.DataSource;
import com.nicefontaine.seanachie.data.sources.image_stories.ImageStoriesRepository;
import com.nicefontaine.seanachie.utils.ImageUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

import static android.app.Activity.RESULT_OK;
import static android.os.Environment.getExternalStoragePublicDirectory;
import static com.nicefontaine.seanachie.utils.Utils.isNull;


public class ImageStoryCreatePresenter implements
        ImageStoryCreateContract.Presenter,
        DataSource.LoadCountCallback, DataSource.LoadElementCallback<ImageStory> {

    private static final String REPOSITORY = "com.nicefontaine.seanachie.fileprovider";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int REQUEST_SPEECH_TO_TEXT = 2;

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
    public void saveImageStory(ImageStory imageStory) {
        imageStoriesRepository.create(imageStory);
        view.finish();
    }

    @Override
    public void editImageStory(ImageStory imageStory) {
        imageStoriesRepository.edit(imageStory);
        view.finish();
    }

    @Override
    public void onResume() {
        imageStoriesRepository.count(this);
    }

    @Override
    public void onEditImageStory(int id) {
        imageStoriesRepository.getElement(id, this);
    }

    @Override
    public void onRefresh() {}

    @Override
    public void onPause() {}

    @Override
    public void takePicture(Activity activity) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Uri photoURI = FileProvider.getUriForFile(activity, REPOSITORY, photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private File createImageFile() throws IOException {
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File photoFile = File.createTempFile(getImageFileName(), ".jpg", storageDir);
        String path = photoFile.getAbsolutePath();
        view.loadImagePath(path);
        return photoFile;
    }

    private String getImageFileName() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        return "JPEG_" + timeStamp + "_";
    }

    @Override
    public void story(Activity activity, String text) {
        Intent intent = getSpeechIntent(text);
        activity.startActivityForResult(intent, REQUEST_SPEECH_TO_TEXT);
    }

    private Intent getSpeechIntent(String text) {
        return new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                .putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                .putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                .putExtra(RecognizerIntent.EXTRA_PROMPT, text);
    }

    @Override
    public void result(int requestCode, int resultCode, Intent intent) {
        if (okNotNull(requestCode, resultCode, intent)) setStory(intent);
    }

    private boolean okNotNull(int requestCode, int resultCode, Intent intent) {
        return requestCode == REQUEST_SPEECH_TO_TEXT && resultCode == RESULT_OK && !isNull(intent);
    }

    private void setStory(Intent intent) {
        List<String> result = intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        view.setStory(result.get(0));
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
        ImageStory imageStory = new ImageStory()
                .position((int) count)
                .form(form);
        view.loadImageStory(imageStory);
        view.initRecycler();
    }

    @Override
    public void onElementLoaded(ImageStory element) {
        view.loadImageStory(element);
        view.initRecycler();
    }

    @Override
    public void noElement() {
        view.noData();
    }
}
