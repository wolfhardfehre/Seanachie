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

package com.nicefontaine.seanachie.injection.modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.nicefontaine.seanachie.data.sources.session.DefaultPersistentData;
import com.nicefontaine.seanachie.data.sources.session.PersistentData;
import com.nicefontaine.seanachie.data.sources.session.Session;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class AppModule {

    private Application mApplication;

    public AppModule(final Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    PersistentData providePersistentData(final Application application) {
        Resources resources = application.getResources();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(application);
        return new DefaultPersistentData(resources, preferences);
    }

    @Provides
    @Singleton
    Session provideSession(final Context context, final PersistentData persistentData) {
        return new Session(context, persistentData);
    }
}