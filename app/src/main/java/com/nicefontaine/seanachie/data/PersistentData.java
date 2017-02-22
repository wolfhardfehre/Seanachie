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

public interface PersistentData {

    boolean contains(String key);

    boolean contains(int key);

    PersistentData store(String key, boolean value);

    PersistentData store(int key, boolean value);

    PersistentData store(String key, String value);

    PersistentData store(int key, String value);

    PersistentData store(String key, int value);

    PersistentData store(int key, int value);

    PersistentData store(String key, long value);

    PersistentData store(int key, long value);

    PersistentData store(String key, float value);

    PersistentData store(int key, float value);

    boolean restore(String key, boolean defaultValue);

    boolean restore(int key, boolean defaultValue);

    String restore(String key, String defaultValue);

    String restore(int key, String defaultValue);

    int restore(String key, int defaultValue);

    int restore(int key, int defaultValue);

    long restore(String key, long defaultValue);

    long restore(int key, long defaultValue);

    float restore(String key, float defaultValue);

    float restore(int key, float defaultValue);

    PersistentData remove(String key);

    PersistentData remove(int key);

    void registerOnPersistentDataChangeListener(OnPersistentDataChangeListener listener);

    void unregisterOnPersistentDataChangeListener(OnPersistentDataChangeListener listener);

    interface OnPersistentDataChangeListener {

        void onPersistentDataChanged(String key);
    }
}