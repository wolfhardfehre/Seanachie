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

package com.nicefontaine.seanachie.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.nicefontaine.seanachie.R;
import com.nicefontaine.seanachie.data.models.Category;
import com.nicefontaine.seanachie.data.models.Form;
import com.nicefontaine.seanachie.data.models.ImageStory;

import java.sql.SQLException;


public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "seanachie.db";
    private static final int DATABASE_VERSION = 15;

    private static DatabaseHelper instance;
    private RuntimeExceptionDao<Category, Integer> categoriesDao;
    private RuntimeExceptionDao<Form, Integer> formDao;
    private RuntimeExceptionDao<ImageStory, Integer> imageStoryDao;

    public static DatabaseHelper getInstance(@NonNull Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context);
        }
        return instance;
    }

    private DatabaseHelper(@NonNull Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Category.class, true);
            TableUtils.dropTable(connectionSource, Form.class, true);
            TableUtils.dropTable(connectionSource, ImageStory.class, true);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Category.class);
            TableUtils.createTable(connectionSource, Form.class);
            TableUtils.createTable(connectionSource, ImageStory.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public RuntimeExceptionDao<Category, Integer> getCategoryDao() {
        if (categoriesDao == null) {
            categoriesDao = getRuntimeExceptionDao(Category.class);
        }
        return categoriesDao;
    }

    public RuntimeExceptionDao<Form, Integer> getFormDao() {
        if (formDao == null) {
            formDao = getRuntimeExceptionDao(Form.class);
        }
        return formDao;
    }

    public RuntimeExceptionDao<ImageStory, Integer> getImageStoryDao() {
        if (imageStoryDao == null) {
            imageStoryDao = getRuntimeExceptionDao(ImageStory.class);
        }
        return imageStoryDao;
    }

    @Override
    public void close() {
        super.close();
        categoriesDao = null;
        formDao = null;
        imageStoryDao = null;
    }
}
