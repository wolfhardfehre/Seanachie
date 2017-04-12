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
import com.nicefontaine.seanachie.ui.BaseActivity;
import com.nicefontaine.seanachie.ui.categories.CategoriesFragment;
import com.nicefontaine.seanachie.ui.form.FormFragment;
import com.nicefontaine.seanachie.ui.category.CategoryFragmentDialog;
import com.nicefontaine.seanachie.ui.forms.FormsFragment;
import com.nicefontaine.seanachie.injection.modules.AppModule;
import com.nicefontaine.seanachie.data.sources.session.Session;
import com.nicefontaine.seanachie.ui.imagestory.ImageStoryFragment;
import com.nicefontaine.seanachie.ui.imagestories.ImageStoriesFragment;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(modules={
        AppModule.class,
        SourceModule.class
})
public interface AppComponent {

    // activities
    void inject(BaseActivity baseActivity);

    // fragments
    void inject(FormsFragment formsFragment);
    void inject(ImageStoriesFragment imageStoriesFragment);
    void inject(CategoriesFragment categoriesFragment);
    void inject(FormFragment formFragment);
    void inject(ImageStoryFragment imageStoryFragment);
    void inject(CategoryFragmentDialog categoryFragmentDialog);

    // Session
    void inject(Session session);
}
