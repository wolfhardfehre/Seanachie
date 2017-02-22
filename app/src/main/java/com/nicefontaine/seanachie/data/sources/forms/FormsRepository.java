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

import java.util.List;

import static dagger.internal.Preconditions.checkNotNull;


public class FormsRepository implements FormsDataSource {

    private static FormsRepository instance;

    private FormsDataSource formsLocalDataSource;

    public static FormsRepository getInstance(@NonNull FormsDataSource formsLocalDataSource) {
        if (instance == null) {
            instance = new FormsRepository(formsLocalDataSource);
        }
        return instance;
    }

    private FormsRepository(@NonNull FormsDataSource formsLocalDataSource) {
        this.formsLocalDataSource = checkNotNull(formsLocalDataSource);
    }

    @Override
    public void getForms(@NonNull LoadFormsCallback callback) {
        formsLocalDataSource.getForms(callback);
    }

    @Override
    public void getForm(@NonNull Integer formId, @NonNull LoadFormCallback callback) {
        formsLocalDataSource.getForm(formId, callback);
    }

    @Override
    public void createForm(@NonNull Form form) {
        formsLocalDataSource.createForm(form);
    }

    @Override
    public void editForm(@NonNull Form form) {
        formsLocalDataSource.editForm(form);
    }

    @Override
    public void deleteForm(@NonNull Integer formId) {
        formsLocalDataSource.deleteForm(formId);
    }

    @Override
    public void swapForm(@NonNull List<Form> forms) {
        formsLocalDataSource.swapForm(forms);
    }
}
