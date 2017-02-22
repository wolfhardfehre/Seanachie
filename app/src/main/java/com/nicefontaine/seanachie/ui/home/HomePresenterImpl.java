package com.nicefontaine.seanachie.ui.home;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.os.Environment.getExternalStoragePublicDirectory;


class HomePresenterImpl implements HomePresenter {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_SPEECH_TO_TEXT = 2;
    private final HomeView homeView;

    private String mCurrentPhotoPath;
    private int width;

    HomePresenterImpl(HomeView homeView) {
        this.homeView = homeView;
    }

    @Override
    public void camera(Activity activity, int width) throws IOException {
        this.width = width;
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
        mCurrentPhotoPath = photoFile.getAbsolutePath();
        return photoFile;
    }

    private String getImageFileName() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
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
            case REQUEST_IMAGE_CAPTURE:
                if (okNotNull(resultCode, intent)) setBitmap(); break;
        }
    }

    private void setStory(Intent intent) {
        List<String> result = intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        homeView.showStory(result.get(0));
    }

    private boolean okNotNull(int resultCode, Intent intent) {
        return resultCode == RESULT_OK && intent != null;
    }

    private void setBitmap() {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions = computeOptions(bmOptions);
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        homeView.showImage(bitmap);
    }

    private BitmapFactory.Options computeOptions(BitmapFactory.Options bmOptions) {
        int imageWidth = getImageWidth(bmOptions);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = imageWidth / width;
        bmOptions.inPurgeable = true;
        return bmOptions;
    }

    private int getImageWidth(BitmapFactory.Options bmOptions) {
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        return bmOptions.outWidth;
    }
}
