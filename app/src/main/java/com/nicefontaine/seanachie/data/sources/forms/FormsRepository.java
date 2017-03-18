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

package com.nicefontaine.seanachie.data.sources.forms;


import android.support.annotation.NonNull;

import com.nicefontaine.seanachie.data.models.Form;
import com.nicefontaine.seanachie.data.sources.DataSource;

import java.util.List;

import static dagger.internal.Preconditions.checkNotNull;


public class FormsRepository implements DataSource<Form> {

    private static FormsRepository instance;

    private DataSource<Form> formsLocalDataSource;

    public static FormsRepository getInstance(@NonNull DataSource<Form> formsLocalDataSource) {
        if (instance == null) {
            instance = new FormsRepository(formsLocalDataSource);
        }
        return instance;
    }

    private FormsRepository(@NonNull DataSource<Form> formsLocalDataSource) {
        this.formsLocalDataSource = checkNotNull(formsLocalDataSource);
    }

    @Override
    public void getData(@NonNull LoadDataCallback<Form> callback) {
        formsLocalDataSource.getData(callback);
    }

    @Override
    public void getElement(@NonNull Integer formId, @NonNull LoadElementCallback<Form> callback) {
        formsLocalDataSource.getElement(formId, callback);
    }

    @Override
    public void create(@NonNull Form form) {
        formsLocalDataSource.create(form);
    }

    @Override
    public void edit(@NonNull Form form) {
        formsLocalDataSource.edit(form);
    }

    @Override
    public void delete(@NonNull Integer formId) {
        formsLocalDataSource.delete(formId);
    }

    @Override
    public void swap(@NonNull List<Form> forms) {
        formsLocalDataSource.swap(forms);
    }

    @Override
    public void count(@NonNull LoadCountCallback callback) {
        formsLocalDataSource.count(callback);
    }
}
