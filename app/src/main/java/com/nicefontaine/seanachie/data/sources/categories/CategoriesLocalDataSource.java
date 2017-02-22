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


import android.content.Context;
import android.support.annotation.NonNull;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.nicefontaine.seanachie.data.DatabaseHelper;
import com.nicefontaine.seanachie.data.models.Category;

import java.util.List;
import java.util.concurrent.Callable;

import static dagger.internal.Preconditions.checkNotNull;


public class CategoriesLocalDataSource implements CategoriesDataSource {

    private static CategoriesLocalDataSource instance;
    private RuntimeExceptionDao<Category, Integer> categoriesDao;

    public static CategoriesLocalDataSource getInstance(@NonNull Context context) {
        if (instance == null) {
            instance = new CategoriesLocalDataSource(context);
        }
        return instance;
    }

    private CategoriesLocalDataSource(@NonNull Context context) {
        checkNotNull(context);
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        categoriesDao = databaseHelper.getCategoryDao();
    }


    @Override
    public void getCategories(@NonNull LoadCategoriesCallback callback) {
        List<Category> categories = categoriesDao.queryForAll();
        if (categories.isEmpty()) {
            callback.onNoCategories();
        } else {
            callback.onCategoriesLoaded(categories);
        }
    }

    @Override
    public void getCategory(@NonNull Integer categoryId, @NonNull LoadCategoryCallback callback) {
        Category category = categoriesDao.queryForId(categoryId);
        if (category == null) {
            callback.onNoCategory();
        } else {
            callback.onCategoryLoaded(category);
        }
    }

    @Override
    public void createCategory(@NonNull Category category) {
        categoriesDao.create(category);
    }

    @Override
    public void editCategory(@NonNull Category category) {
        categoriesDao.deleteById(category.getId());
        categoriesDao.create(category);
    }

    @Override
    public void deleteCategory(@NonNull Integer categoryId) {
        categoriesDao.deleteById(categoryId);
    }

    @Override
    public void swapCategory(@NonNull List<Category> categories) {
        categoriesDao.delete(categories);
        categoriesDao.callBatchTasks(new Callable<Void>() {

            public Void call() throws Exception {
                for (Category category : categories) {
                    categoriesDao.create(category);
                }
                return null;
            }
        });
    }
}