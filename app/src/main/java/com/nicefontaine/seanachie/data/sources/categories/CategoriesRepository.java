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

package com.nicefontaine.seanachie.data.sources.categories;


import android.support.annotation.NonNull;

import com.nicefontaine.seanachie.data.models.Category;

import java.util.List;

import static dagger.internal.Preconditions.checkNotNull;


public class CategoriesRepository implements CategoriesDataSource {

    private static CategoriesRepository instance;

    private CategoriesDataSource categoriesLocalDataSource;

    public static CategoriesRepository getInstance(
            @NonNull CategoriesDataSource categoriesLocalDataSource) {
        if (instance == null) {
            instance = new CategoriesRepository(categoriesLocalDataSource);
        }
        return instance;
    }

    private CategoriesRepository(@NonNull CategoriesDataSource categoriesLocalDataSource) {
        this.categoriesLocalDataSource = checkNotNull(categoriesLocalDataSource);
    }

    @Override
    public void getCategories(@NonNull LoadCategoriesCallback callback) {
        categoriesLocalDataSource.getCategories(callback);
    }

    @Override
    public void getCategory(@NonNull Integer categoryId, @NonNull LoadCategoryCallback callback) {
        categoriesLocalDataSource.getCategory(categoryId, callback);
    }

    @Override
    public void createCategory(@NonNull Category category) {
        categoriesLocalDataSource.createCategory(category);
    }

    @Override
    public void editCategory(@NonNull Category category) {
        categoriesLocalDataSource.editCategory(category);
    }

    @Override
    public void deleteCategory(@NonNull Integer categoryId) {
        categoriesLocalDataSource.deleteCategory(categoryId);
    }

    @Override
    public void swapCategory(@NonNull List<Category> categories) {
        categoriesLocalDataSource.swapCategory(categories);
    }
}
