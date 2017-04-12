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


import android.content.Context;
import android.support.annotation.NonNull;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.nicefontaine.seanachie.data.sources.DatabaseHelper;
import com.nicefontaine.seanachie.data.models.Form;
import com.nicefontaine.seanachie.data.sources.DataSource;

import java.util.List;
import java.util.concurrent.Callable;

import static dagger.internal.Preconditions.checkNotNull;


public class FormsLocalDataSource implements DataSource<Form> {

    private static FormsLocalDataSource instance;
    private RuntimeExceptionDao<Form, Integer> formDao;

    public static FormsLocalDataSource getInstance(@NonNull Context context) {
        if (instance == null) {
            instance = new FormsLocalDataSource(context);
        }
        return instance;
    }

    private FormsLocalDataSource(@NonNull Context context) {
        checkNotNull(context);
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        formDao = databaseHelper.getFormDao();
    }


    @Override
    public void getData(@NonNull LoadDataCallback<Form> callback) {
        List<Form> forms = formDao.queryForAll();
        if (forms.isEmpty()) {
            callback.noData();
        } else {
            callback.onDataLoaded(forms);
        }
    }

    @Override
    public void getElement(@NonNull Integer formId, @NonNull LoadElementCallback<Form> callback) {
        Form form = formDao.queryForId(formId);
        if (form == null) {
            callback.noElement();
        } else {
            callback.onElementLoaded(form);
        }
    }

    @Override
    public void create(@NonNull Form form) {
        formDao.create(form);
    }

    @Override
    public void edit(@NonNull Form form) {
        formDao.deleteById(form.getId());
        formDao.create(form);
    }

    @Override
    public void delete(@NonNull Integer formId) {
        formDao.deleteById(formId);
    }

    @Override
    public void swap(@NonNull List<Form> forms) {
        formDao.delete(forms);
        formDao.callBatchTasks(new Callable<Void>() {

            public Void call() throws Exception {
                for (Form form : forms) {
                    formDao.create(form);
                }
                return null;
            }
        });
    }

    @Override
    public void count(@NonNull LoadCountCallback callback) {
        long count = formDao.countOf();
        callback.onCount(count);
    }
}
