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

package com.nicefontaine.seanachie.data.models;


import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.nicefontaine.seanachie.utils.Utils.isNull;


@DatabaseTable(tableName = "image_story")
public class ImageStory extends BaseEntry {

    @DatabaseField(columnName = "image_path")
    private String imagePath;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private Form form;

    public ImageStory() {
        // ORMLite needs a no-arg constructor
        super(-1);
    }

    public String getImagePath() {
        return imagePath;
    }

    public ImageStory position(int position) {
        this.position = position;
        return this;
    }

    public ImageStory image(String imagePath) {
        this.imagePath = imagePath;
        return this;
    }

    public ImageStory form(Form form) {
        this.form = form;
        return this;
    }

    public ImageStory categories(List<Category> categories) {
        form.categories(categories);
        return this;
    }

    public Form getForm() {
        return form;
    }

    public int count() {
        if (!isNull(form)) {
            List<Category> categories = form.getCategories();
            if (!isNull(categories)) {
                int count = 0;
                for (Category category : categories) {
                    if (!isNull(category.getValue())) {
                        count++;
                    }
                }
                return count;
            }
        }
        return 0;
    }

    public boolean isEmpty() {
        return position == -1;
    }

    public String getCategoriesContent() {
        StringBuilder text = new StringBuilder();
        for (Category category : form.getCategories()) {
            text.append(category.getKey())
                    .append(": ")
                    .append(category.getValue())
                    .append("\n");
        }
        return text.toString();
    }
}
