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
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;

import com.nicefontaine.seanachie.data.models.Form;
import com.nicefontaine.seanachie.data.models.ImageStory;
import com.nicefontaine.seanachie.data.sources.image_stories.ImageStoriesRepository;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

import static android.app.Activity.RESULT_OK;
import static android.os.Environment.getExternalStoragePublicDirectory;


public class ImageStoryCreatePresenter implements
        ImageStoryCreateContract.Presenter {


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

    @Override
    public void camera(Activity activity) throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            File photoFile = createImageFile();
            Uri photoURI = FileProvider.getUriForFile(activity,
                    "com.nicefontaine.seanachie.fileprovider", photoFile);
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
    public void story(Activity activity) {
        Intent intent = getSpeechIntent();
        activity.startActivityForResult(intent, REQUEST_SPEECH_TO_TEXT);
    }

    private Intent getSpeechIntent() {
        return new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                .putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                .putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                .putExtra(RecognizerIntent.EXTRA_PROMPT, "Sag was!");
    }

    @Override
    public void result(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case REQUEST_SPEECH_TO_TEXT:
                if (okNotNull(resultCode, intent)) setStory(intent); break;
        }
    }

    private boolean okNotNull(int resultCode, Intent intent) {
        return resultCode == RESULT_OK && intent != null;
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
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions = computeOptions(path, width, bmOptions);
        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
        float angle = getRotationAngle(path);
        view.setPhoto(rotateImage(bitmap, angle));
    }

    private BitmapFactory.Options computeOptions(String path, int width, BitmapFactory.Options bmOptions) {
        int imageWidth = getImageWidth(path, bmOptions);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = imageWidth / width;
        bmOptions.inPurgeable = true;
        return bmOptions;
    }

    private int getImageWidth(String path, BitmapFactory.Options bmOptions) {
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bmOptions);
        return bmOptions.outWidth;
    }

    private float getRotationAngle(String path) throws IOException {
        ExifInterface exifInterface = new ExifInterface(path);
        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);
        switch(orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return 90;
            case ExifInterface.ORIENTATION_ROTATE_180:
                return 180;
            case ExifInterface.ORIENTATION_ROTATE_270:
                return 270;
            default:
                return 0;
        }
    }

    private Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
}
