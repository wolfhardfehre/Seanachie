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


import android.content.Context;

import com.nicefontaine.seanachie.data.sources.categories.CategoriesLocalDataSource;
import com.nicefontaine.seanachie.data.sources.categories.CategoriesRepository;
import com.nicefontaine.seanachie.data.sources.forms.FormsLocalDataSource;
import com.nicefontaine.seanachie.data.sources.forms.FormsRepository;
import com.nicefontaine.seanachie.data.sources.image_stories.ImageStoriesLocalDataSource;
import com.nicefontaine.seanachie.data.sources.image_stories.ImageStoriesRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class SourceModule {

    @Provides
    @Singleton
    FormsLocalDataSource provideFormsLocalDataSource(final Context context) {
        return FormsLocalDataSource.getInstance(context);
    }

    @Provides
    @Singleton
    FormsRepository provideFormsRepository(final FormsLocalDataSource formsLocalDataSource) {
        return FormsRepository.getInstance(formsLocalDataSource);
    }

    @Provides
    @Singleton
    ImageStoriesLocalDataSource provideImageStoriesLocalDataSource(final Context context) {
        return ImageStoriesLocalDataSource.getInstance(context);
    }

    @Provides
    @Singleton
    ImageStoriesRepository provideImageStoriesRepository(
            final ImageStoriesLocalDataSource imageStoriesLocalDataSource) {
        return ImageStoriesRepository.getInstance(imageStoriesLocalDataSource);
    }

    @Provides
    @Singleton
    CategoriesLocalDataSource provideCategoriesLocalDataSource(final Context context) {
        return CategoriesLocalDataSource.getInstance(context);
    }

    @Provides
    @Singleton
    CategoriesRepository provideCategoriesRepository(
            final CategoriesLocalDataSource categoriesLocalDataSource) {
        return CategoriesRepository.getInstance(categoriesLocalDataSource);
    }
}
