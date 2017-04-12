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
import com.nicefontaine.seanachie.data.sources.DatabaseHelper;
import com.nicefontaine.seanachie.data.models.Category;
import com.nicefontaine.seanachie.data.sources.DataSource;

import java.util.List;
import java.util.concurrent.Callable;

import static dagger.internal.Preconditions.checkNotNull;


public class CategoriesLocalDataSource implements DataSource<Category> {

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
    public void getData(@NonNull LoadDataCallback<Category> callback) {
        List<Category> categories = categoriesDao.queryForAll();
        if (categories.isEmpty()) {
            callback.noData();
        } else {
            callback.onDataLoaded(categories);
        }
    }

    @Override
    public void getElement(@NonNull Integer categoryId,
                           @NonNull LoadElementCallback<Category> callback) {
        Category category = categoriesDao.queryForId(categoryId);
        if (category == null) {
            callback.noElement();
        } else {
            callback.onElementLoaded(category);
        }
    }

    @Override
    public void create(@NonNull Category category) {
        categoriesDao.create(category);
    }

    @Override
    public void edit(@NonNull Category category) {
        categoriesDao.deleteById(category.getId());
        categoriesDao.create(category);
    }

    @Override
    public void delete(@NonNull Integer categoryId) {
        categoriesDao.deleteById(categoryId);
    }

    @Override
    public void swap(@NonNull List<Category> categories) {
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

    @Override
    public void count(@NonNull LoadCountCallback callback) {
        long count = categoriesDao.countOf();
        callback.onCount(count);
    }
}