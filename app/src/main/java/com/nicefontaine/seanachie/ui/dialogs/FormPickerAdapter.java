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

package com.nicefontaine.seanachie.ui.dialogs;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nicefontaine.seanachie.R;
import com.nicefontaine.seanachie.data.models.Form;

import java.util.List;


public class FormPickerAdapter extends ArrayAdapter<Form> {

    private final Context mContext;
    private final int resource;
    private final List<Form> forms;
    private LayoutInflater inflater;

    public FormPickerAdapter(Context context, int resource, List<Form> forms) {
        super(context, resource, forms);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
        this.mContext = context;
        this.forms = forms;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) convertView = inflater.inflate(resource, null);
        StoryHolder holder = new StoryHolder(convertView);
        holder.name.setText(forms.get(position).getSpecies());
        return convertView;
    }

    public class StoryHolder extends RecyclerView.ViewHolder {
        protected ImageView icon;
        protected TextView name;

        public StoryHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.adapter_form_picker_textview);
        }
    }
}