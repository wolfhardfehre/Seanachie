package com.nicefontaine.seanachie.utils;


import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class FileUtils {

    private FileUtils() {}

    public static File createImageFile() throws IOException {
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(getImageFileName(), ".jpg", storageDir);
    }

    private static String getImageFileName() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        return "JPEG_" + timeStamp + "_";
    }
}
