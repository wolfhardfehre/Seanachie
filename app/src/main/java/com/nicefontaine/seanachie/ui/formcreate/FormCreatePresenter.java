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

package com.nicefontaine.seanachie.ui.formcreate;


import android.support.annotation.NonNull;

import com.nicefontaine.seanachie.data.models.Category;
import com.nicefontaine.seanachie.data.models.Form;
import com.nicefontaine.seanachie.data.sources.categories.CategoriesDataSource;
import com.nicefontaine.seanachie.data.sources.categories.CategoriesRepository;
import com.nicefontaine.seanachie.data.sources.forms.FormsDataSource;
import com.nicefontaine.seanachie.data.sources.forms.FormsRepository;

import java.util.ArrayList;
import java.util.List;


public class FormCreatePresenter implements
        FormCreateContract.Presenter,
        CategoriesDataSource.LoadCategoriesCallback,
        FormsDataSource.LoadFormsCallback {

    private final FormCreateContract.View view;
    private final FormsRepository formsRepository;
    private final CategoriesRepository categoriesRepository;
    private List<Category> categories;
    private List<Form> forms;
    private boolean isResumed = true;

    public FormCreatePresenter(@NonNull FormsRepository formsRepository,
                               @NonNull CategoriesRepository categoriesRepository,
                               @NonNull FormCreateContract.View view) {
        this.formsRepository = formsRepository;
        this.categoriesRepository = categoriesRepository;
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void onCategoriesLoaded(List<Category> categories) {
        this.categories = categories;
        view.loadCategories(categories);
        if (isResumed) {
            view.initRecycler();
        } else {
            view.updateRecycler();
        }
        isResumed = false;
    }

    @Override
    public void onFormsLoaded(List<Form> forms) {
        this.forms = forms;
    }

    @Override
    public void onNoForms() {
        this.forms = new ArrayList<>();
    }

    @Override
    public void onNoCategories() {
        view.noData();
        if (isResumed) {
            view.initRecycler();
        } else {
            view.updateRecycler();
        }
        isResumed = false;
    }

    @Override
    public void createForm(String title) {
        Form form = new Form(title).categories(categories)
                .position(forms.size() + 1);
        formsRepository.createForm(form);
        view.finish();
    }

    @Override
    public void itemMoved(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public void itemRemoved(Integer categoryId) {
        for (Category category : categories) {
            if (category.getId() == categoryId) {
                categories.remove(category);
            }
        }
    }

    @Override
    public void onResume() {
        formsRepository.getForms(this);
        categoriesRepository.getCategories(this);
    }

    @Override
    public void onRefresh() {
        view.loadCategories(categories);
    }

    @Override
    public void onPause() {
        isResumed = true;
    }
}
