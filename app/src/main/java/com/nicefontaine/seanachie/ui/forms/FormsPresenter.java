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

package com.nicefontaine.seanachie.ui.forms;


import android.support.annotation.NonNull;

import com.nicefontaine.seanachie.data.models.Form;
import com.nicefontaine.seanachie.data.sources.DataSource;
import com.nicefontaine.seanachie.data.sources.forms.FormsRepository;

import java.util.List;

public class FormsPresenter implements
        FormsContract.Presenter,
        DataSource.LoadDataCallback<Form> {

    private final FormsRepository formsRepository;
    private final FormsContract.View view;
    private boolean isResumed = true;

    public FormsPresenter(@NonNull FormsRepository formsRepository,
                          @NonNull FormsContract.View view) {
        this.formsRepository = formsRepository;
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void onResume() {
        formsRepository.getData(this);
    }

    @Override
    public void onRefresh() {
        formsRepository.getData(this);
    }

    @Override
    public void onPause() {
        isResumed = true;
    }

    @Override
    public void itemMoved(List<Form> forms) {
        formsRepository.swap(forms);
    }

    @Override
    public void itemRemoved(Integer formId) {
        formsRepository.delete(formId);
    }

    @Override
    public void onDataLoaded(List<Form> forms) {
        view.loadForms(forms);
        if (isResumed) {
            view.initRecycler();
        } else {
            view.updateRecycler();
        }
        isResumed = false;
    }

    @Override
    public void noData() {
        view.noData();
        if (isResumed) {
            view.initRecycler();
        } else {
            view.updateRecycler();
        }
        isResumed = false;
    }
}
