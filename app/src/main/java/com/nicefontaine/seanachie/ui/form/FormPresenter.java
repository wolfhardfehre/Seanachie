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

package com.nicefontaine.seanachie.ui.form;


import android.support.annotation.NonNull;

import com.nicefontaine.seanachie.data.models.Category;
import com.nicefontaine.seanachie.data.models.Form;
import com.nicefontaine.seanachie.data.sources.DataSource;
import com.nicefontaine.seanachie.data.sources.categories.CategoriesRepository;
import com.nicefontaine.seanachie.data.sources.forms.FormsRepository;

import java.util.List;


public class FormPresenter implements
        FormContract.Presenter,
        DataSource.LoadDataCallback<Category>,
        DataSource.LoadCountCallback {

    private final FormContract.View view;
    private final FormsRepository formsRepository;
    private final CategoriesRepository categoriesRepository;
    private List<Category> categories;
    private boolean isResumed = true;
    private long currentCount;

    public FormPresenter(@NonNull FormsRepository formsRepository,
                         @NonNull CategoriesRepository categoriesRepository,
                         @NonNull FormContract.View view) {
        this.formsRepository = formsRepository;
        this.categoriesRepository = categoriesRepository;
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void onDataLoaded(List<Category> categories) {
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
    public void noData() {
        view.noData();
        if (isResumed) {
            view.initRecycler();
        } else {
            view.updateRecycler();
        }
        isResumed = false;
    }

    @Override
    public void createForm(String title, String name, String story) {
        Form form = new Form((int) currentCount + 1, title).categories(categories, name, story);
        formsRepository.create(form);
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
        categoriesRepository.getData(this);
    }

    @Override
    public void onRefresh() {
        view.loadCategories(categories);
    }

    @Override
    public void onPause() {
        isResumed = true;
    }

    @Override
    public void onCount(long count) {
        currentCount = count;
    }
}
