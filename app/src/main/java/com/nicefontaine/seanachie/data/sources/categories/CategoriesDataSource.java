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

import java.util.List;


public interface CategoriesDataSource {

    interface LoadCategoriesCallback {

        void onCategoriesLoaded(List<Category> categories);

        void onNoCategories();
    }

    interface LoadCategoryCallback {

        void onCategoryLoaded(Category category);

        void onNoCategory();
    }

    void getCategories(@NonNull LoadCategoriesCallback callback);

    void getCategory(@NonNull Integer categoryId, @NonNull LoadCategoryCallback callback);

    void createCategory(@NonNull Category category);

    void editCategory(@NonNull Category category);

    void deleteCategory(@NonNull Integer categoryId);

    void swapCategory(@NonNull List<Category> categories);
}
