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


public interface FormsDataSource {

    interface LoadFormsCallback {

        void onFormsLoaded(List<Form> forms);

        void onNoForms();
    }

    interface LoadFormCallback {

        void onFormLoaded(Form form);

        void onNoForm();
    }

    void getForms(@NonNull LoadFormsCallback callback);

    void getForm(@NonNull Integer formId, @NonNull LoadFormCallback callback);

    void createForm(@NonNull Form form);

    void editForm(@NonNull Form form);

    void deleteForm(@NonNull Integer formId);

    void swapForm(@NonNull List<Form> forms);
}
