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

package com.nicefontaine.seanachie.ui.formpicker;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nicefontaine.seanachie.R;
import com.nicefontaine.seanachie.data.models.Form;

import java.util.List;


class FormPickerAdapter extends ArrayAdapter<Form> {

    private final int resource;
    private final List<Form> forms;
    private LayoutInflater inflater;

    FormPickerAdapter(Context context, int resource, List<Form> forms) {
        super(context, resource, forms);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
        this.forms = forms;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) convertView = inflater.inflate(resource, null);
        StoryHolder holder = new StoryHolder(convertView);
        Form form = forms.get(position);
        holder.name.setText(form.getSpecies());
        return convertView;
    }

    private class StoryHolder extends RecyclerView.ViewHolder {
        protected TextView name;

        StoryHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.adapter_formpicker_textview);
        }
    }
}