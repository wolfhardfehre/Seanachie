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


import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DefaultPersistentData implements PersistentData,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private final Resources resources;
    private final SharedPreferences sharedPreferences;
    private final List<OnPersistentDataChangeListener> persistentDataChangeListeners;

    public DefaultPersistentData(@NonNull Resources resources,
                                  @NonNull SharedPreferences sharedPreferences) {
        this.resources = resources;
        this.sharedPreferences = sharedPreferences;
        this.sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        persistentDataChangeListeners = Collections.synchronizedList(
                new ArrayList<OnPersistentDataChangeListener>());
    }

    @Override
    public boolean contains(final String key) {
        return sharedPreferences.contains(key);
    }

    @Override
    public boolean contains(final int key) {
        return sharedPreferences.contains(getKeyName(key));
    }

    @Override
    public PersistentData store(final String key, final boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
        return this;
    }

    @Override
    public PersistentData store(final int key, final boolean value) {
        sharedPreferences.edit().putBoolean(getKeyName(key), value).apply();
        return this;
    }

    @Override
    public PersistentData store(final String key, final String value) {
        sharedPreferences.edit().putString(key, value).apply();
        return this;
    }

    @Override
    public PersistentData store(final int key, final String value) {
        sharedPreferences.edit().putString(getKeyName(key), value).apply();
        return this;
    }

    @Override
    public PersistentData store(final String key, final int value) {
        sharedPreferences.edit().putInt(key, value).apply();
        return this;
    }

    @Override
    public PersistentData store(final int key, final int value) {
        sharedPreferences.edit().putInt(getKeyName(key), value).apply();
        return this;
    }

    @Override
    public PersistentData store(final String key, final long value) {
        sharedPreferences.edit().putLong(key, value).apply();
        return this;
    }

    @Override
    public PersistentData store(final int key, final long value) {
        sharedPreferences.edit().putLong(getKeyName(key), value).apply();
        return this;
    }

    @Override
    public PersistentData store(final String key, final float value) {
        sharedPreferences.edit().putFloat(key, value).apply();
        return this;
    }

    @Override
    public PersistentData store(final int key, final float value) {
        sharedPreferences.edit().putFloat(getKeyName(key), value).apply();
        return this;
    }

    @Override
    public boolean restore(final String key, final boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    @Override
    public boolean restore(final int key, final boolean defaultValue) {
        return sharedPreferences.getBoolean(getKeyName(key), defaultValue);
    }

    @Override
    public String restore(final String key, final String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    @Override
    public String restore(final int key, final String defaultValue) {
        return sharedPreferences.getString(getKeyName(key), defaultValue);
    }

    @Override
    public int restore(final String key, final int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    @Override
    public int restore(final int key, final int defaultValue) {
        return sharedPreferences.getInt(getKeyName(key), defaultValue);
    }

    @Override
    public long restore(final String key, final long defaultValue) {
        return sharedPreferences.getLong(key, defaultValue);
    }

    @Override
    public long restore(final int key, final long defaultValue) {
        return sharedPreferences.getLong(getKeyName(key), defaultValue);
    }

    @Override
    public float restore(final String key, final float defaultValue) {
        return sharedPreferences.getFloat(key, defaultValue);
    }

    @Override
    public float restore(final int key, final float defaultValue) {
        return sharedPreferences.getFloat(getKeyName(key), defaultValue);
    }

    @Override
    public PersistentData remove(final String key) {
        sharedPreferences.edit().remove(key).apply();
        return this;
    }

    @Override
    public PersistentData remove(final int key) {
        sharedPreferences.edit().remove(getKeyName(key)).apply();
        return this;
    }

    @Override
    public void registerOnPersistentDataChangeListener(final OnPersistentDataChangeListener listener) {
        persistentDataChangeListeners.add(listener);
    }

    @Override
    public void unregisterOnPersistentDataChangeListener(final OnPersistentDataChangeListener listener) {
        persistentDataChangeListeners.remove(listener);
    }

    @Override
    public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
        for (final OnPersistentDataChangeListener persistentDataChangeListener : persistentDataChangeListeners) {
            persistentDataChangeListener.onPersistentDataChanged(key);
        }
    }

    protected String getKeyName(final int key) {
        return resources.getString(key);
    }
}