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

package com.nicefontaine.seanachie.injection.components;


import com.nicefontaine.seanachie.injection.modules.SourceModule;
import com.nicefontaine.seanachie.ui.HomeActivity;
import com.nicefontaine.seanachie.ui.categories.CategoriesFragment;
import com.nicefontaine.seanachie.ui.formcreate.FormCreateFragment;
import com.nicefontaine.seanachie.ui.dialogs.CreateCategoryFragmentDialog;
import com.nicefontaine.seanachie.ui.forms.FormsFragment;
import com.nicefontaine.seanachie.injection.modules.AppModule;
import com.nicefontaine.seanachie.data.Session;
import com.nicefontaine.seanachie.ui.image_story_create.ImageStoryCreateFragment;
import com.nicefontaine.seanachie.ui.image_stories.ImageStoriesFragment;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules={AppModule.class, SourceModule.class})
public interface AppComponent {

    // activities
    void inject(HomeActivity homeActivity);

    // fragments
    void inject(FormsFragment formsFragment);
    void inject(ImageStoriesFragment imageStoriesFragment);
    void inject(CategoriesFragment categoriesFragment);
    void inject(FormCreateFragment formCreateFragment);
    void inject(ImageStoryCreateFragment imageStoryCreateFragment);
    void inject(CreateCategoryFragmentDialog createCategoryFragmentDialog);

    // Session
    void inject(Session session);
}
