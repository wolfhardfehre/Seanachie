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


@DatabaseTable(tableName = "image_story")
public class ImageStory extends BaseEntry {

    @DatabaseField(columnName = "image_path")
    private String imagePath;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private Form form;

    @DatabaseField
    private String name;

    @DatabaseField
    private String story;

    public ImageStory() {
        // ORMLite needs a no-arg constructor
        super(-1);
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getFirst() {
        if (form != null && form.getCategories() != null && !form.getCategories().isEmpty()) {
            Category category = form.getCategories().get(0);
            return String.format("%s: %s", category.getKey(), category.getValue());
        }
        return "...";
    }
}
