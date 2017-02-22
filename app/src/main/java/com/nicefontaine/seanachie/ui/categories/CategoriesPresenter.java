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

package com.nicefontaine.seanachie.ui.categories;


import android.support.annotation.NonNull;

import com.nicefontaine.seanachie.data.models.Category;
import com.nicefontaine.seanachie.data.sources.categories.CategoriesDataSource;
import com.nicefontaine.seanachie.data.sources.categories.CategoriesRepository;

import java.util.List;


public class CategoriesPresenter implements
        CategoriesContract.Presenter, CategoriesDataSource.LoadCategoriesCallback {

    private final CategoriesRepository categoriesRepository;
    private final CategoriesContract.View view;
    private boolean isResumed = true;

    public CategoriesPresenter(@NonNull CategoriesRepository categoriesRepository,
            @NonNull CategoriesContract.View view) {
        this.categoriesRepository = categoriesRepository;
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void onResume() {
        categoriesRepository.getCategories(this);
    }

    @Override
    public void onRefresh() {
        categoriesRepository.getCategories(this);
    }

    @Override
    public void onPause() {
        isResumed = true;
    }

    @Override
    public void itemMoved(List<Category> categories) {
        categoriesRepository.swapCategory(categories);
    }

    @Override
    public void itemRemoved(Integer categoryId) {
        categoriesRepository.deleteCategory(categoryId);
    }

    @Override
    public void onCategoriesLoaded(List<Category> categories) {
        view.loadCategories(categories);
        if (isResumed) {
            view.initRecycler();
        } else {
            view.updateRecycler();
        }
        isResumed = false;
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
}
