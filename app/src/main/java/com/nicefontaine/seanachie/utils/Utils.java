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

package com.nicefontaine.seanachie.utils;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.IOException;
import java.util.Collections;
import java.util.List;


public class Utils {

    private Utils() {}

    public static boolean isNull(Object object) {
        return object == null;
    }

    public static <T> List<T> rightShift(List<T> list, int from, int to) {
        for (int i = from; i < to; i++) {
            Collections.swap(list, i, i + 1);
        }
        return list;
    }

    public static <T> List<T> leftShift(List<T> list, int from, int to) {
        for (int i = from; i > to; i--) {
            Collections.swap(list, i, i - 1);
        }
        return list;
    }
}
