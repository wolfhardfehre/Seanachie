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
import java.util.List;


@DatabaseTable(tableName = "forms")
public class Form extends BaseEntry {

    @DatabaseField(canBeNull = false)
    private String species;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private ArrayList<Category> categories;

    public Form() {
        // ORMLite needs a no-arg constructor
        super(-1);
        this.species = "Form";
    }

    public Form(int position, String species) {
        super(position);
        this.species = species;
        this.categories = new ArrayList<>();
    }

    public Form categories(List<Category> categories) {
        this.categories = (ArrayList<Category>) categories;
        return this;
    }

    public Form categories(List<Category> categories, String name, String story) {
        this.categories.add(new Category(0, name));
        this.categories.addAll(categories);
        this.categories.add(new Category(this.categories.size(), story));
        return this;
    }

    public String getSpecies() {
        return species;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public int getCount() {
        return categories.size();
    }
}
