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
import com.nicefontaine.seanachie.data.sources.DataSource;

import java.util.List;

import static dagger.internal.Preconditions.checkNotNull;


public class CategoriesRepository implements DataSource<Category> {

    private static CategoriesRepository instance;

    private DataSource<Category> categoriesLocalDataSource;

    public static CategoriesRepository getInstance(
            @NonNull DataSource<Category> categoriesLocalDataSource) {
        if (instance == null) {
            instance = new CategoriesRepository(categoriesLocalDataSource);
        }
        return instance;
    }

    private CategoriesRepository(@NonNull DataSource<Category> categoriesLocalDataSource) {
        this.categoriesLocalDataSource = checkNotNull(categoriesLocalDataSource);
    }

    @Override
    public void getData(@NonNull LoadDataCallback<Category> callback) {
        categoriesLocalDataSource.getData(callback);
    }

    @Override
    public void getElement(@NonNull Integer categoryId, @NonNull LoadElementCallback<Category> callback) {
        categoriesLocalDataSource.getElement(categoryId, callback);
    }

    @Override
    public void create(@NonNull Category category) {
        categoriesLocalDataSource.create(category);
    }

    @Override
    public void edit(@NonNull Category category) {
        categoriesLocalDataSource.edit(category);
    }

    @Override
    public void delete(@NonNull Integer categoryId) {
        categoriesLocalDataSource.delete(categoryId);
    }

    @Override
    public void swap(@NonNull List<Category> categories) {
        categoriesLocalDataSource.swap(categories);
    }

    @Override
    public void count(@NonNull LoadCountCallback callback) {
        categoriesLocalDataSource.count(callback);
    }
}
