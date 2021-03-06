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

package com.nicefontaine.seanachie.ui.imagestory;


import android.content.Intent;
import android.graphics.Bitmap;

import com.nicefontaine.seanachie.data.models.Category;
import com.nicefontaine.seanachie.data.models.ImageStory;
import com.nicefontaine.seanachie.ui.BasePresenter;
import com.nicefontaine.seanachie.ui.BaseView;

import java.util.List;


interface ImageStoryContract {

    interface View extends BaseView<Presenter> {

        void loadImageStory(ImageStory imageStory);

        void updateRecycler(List<Category> categories);

        void cacheText(String story);

        void setPhoto(Bitmap bitmap);

        void notify(int text);

        void finish();
    }

    interface Presenter extends BasePresenter {

        void save(ImageStory imageStory);

        void cache(ImageStory current);

        void displayPhoto(String path, int width);

        void result(int requestCode, int resultCode, Intent intent);
    }
}
