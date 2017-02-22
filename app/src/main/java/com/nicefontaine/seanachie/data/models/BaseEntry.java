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


import android.support.annotation.NonNull;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;
import java.util.UUID;


abstract class BaseEntry implements Comparable<BaseEntry>, Serializable {

    private static final String COLUMN_NAME_ID = "id";
    private static final String COLUMN_NAME_POSITION = "position";

    @DatabaseField(generatedId = true, columnName = COLUMN_NAME_ID)
    private int id;

    @DatabaseField(columnName = COLUMN_NAME_POSITION, canBeNull = false)
    protected int position;

    BaseEntry(int position) {
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public int getPosition() {
        return position;
    }

    public int compareTo(@NonNull BaseEntry other) {
        return this.position - other.getPosition();
    }
}
