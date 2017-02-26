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

package com.nicefontaine.seanachie.data;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.nicefontaine.seanachie.R;
import com.nicefontaine.seanachie.data.models.Form;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;


public final class Session {

    private static Session instance;
    private static final Gson gson = new Gson();

    private final Map<Integer, Object> cache;
    private final PersistentData persistentData;

    public static Session getInstance(@NonNull Context context,
                                      @NonNull PersistentData persistentData) {
        if (instance == null) {
            instance = new Session(context, persistentData);
        }
        return instance;
    }

    @SuppressLint("UseSparseArrays")
    private Session(@NonNull Context context, @NonNull PersistentData persistentData) {
        this.persistentData = persistentData;
        cache = new HashMap<>();
        initialize(context);
    }

    private void initialize(Context context) {
        try {
            final PackageInfo pInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            cache.put(R.string.app_version_name, pInfo.versionName);
            cache.put(R.string.app_version_code, pInfo.versionCode);
        } catch (final PackageManager.NameNotFoundException e) {
            Timber.e(e.getMessage());
        }
    }

    public boolean contains(final int key) {
        return cache.containsKey(key) || persistentData.contains(key);
    }

    public void set(final int key, final boolean value) {
        cache.put(key, value);
    }

    public void set(final int key, final String value) {
        cache.put(key, value);
    }

    public void set(final int key, final int value) {
        cache.put(key, value);
    }

    public void set(final int key, final long value) {
        cache.put(key, value);
    }

    public void set(final int key, final float value) {
        cache.put(key, value);
    }

    public void set(final int key, final Form value) {
        cache.put(key, value);
    }

    public void store(final int key, final boolean value) {
        set(key, value);
        persistentData.store(key, value);
    }

    public void store(final int key, final String value) {
        set(key, value);
        persistentData.store(key, value);
    }

    public void store(final int key, final int value) {
        set(key, value);
        persistentData.store(key, value);
    }

    public void store(final int key, final long value) {
        set(key, value);
        persistentData.store(key, value);
    }

    public void store(final int key, final float value) {
        set(key, value);
        persistentData.store(key, value);
    }

    public void store(final int key, final Form value) {
        set(key, value);
        String json = gson.toJson(value);
        persistentData.store(key, json);
    }

    public boolean get(final int key, final boolean defaultValue) {
        Object value = cache.get(key);
        if (value == null) {
            value = persistentData.restore(key, defaultValue);
            cache.put(key, value);
            return (boolean) value;
        } else {
            return (boolean) value;
        }
    }

    public String get(final int key, final String defaultValue) {
        Object value = cache.get(key);
        if (value == null) {
            value = persistentData.restore(key, defaultValue);
            if (value != null) {
                cache.put(key, value);
                return (String) value;
            }
        } else {
            return (String) value;
        }

        return defaultValue;
    }

    public int get(final int key, final int defaultValue) {
        Object value = cache.get(key);
        if (value == null) {
            value = persistentData.restore(key, defaultValue);
            cache.put(key, value);
            return (int) value;
        } else {
            return (int) value;
        }
    }

    public long get(final int key, final long defaultValue) {
        Object value = cache.get(key);
        if (value == null) {
            value = persistentData.restore(key, defaultValue);
            cache.put(key, value);
            return (long) value;
        } else {
            return (long) value;
        }
    }

    public float get(final int key, final float defaultValue) {
        Object value = cache.get(key);
        if (value == null) {
            value = persistentData.restore(key, defaultValue);
            cache.put(key, value);
            return (float) value;
        } else {
            return (float) value;
        }
    }

    public Form get(final int key, final Form defaultValue) {
        Object value = cache.get(key);
        if (value == null) {
            String json = gson.toJson(defaultValue);
            json = persistentData.restore(key, json);
            return gson.fromJson(json, Form.class);
        } else {
            return (Form) value;
        }
    }

    public void removeSetting(int key) {
        persistentData.remove(key);
        if (cache.containsKey(key)) {
            cache.remove(key);
        }
    }
}
