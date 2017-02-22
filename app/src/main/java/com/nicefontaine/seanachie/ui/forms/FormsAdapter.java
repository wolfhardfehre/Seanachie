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

package com.nicefontaine.seanachie.ui.forms;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nicefontaine.seanachie.R;
import com.nicefontaine.seanachie.data.models.Form;
import com.nicefontaine.seanachie.ui.ItemTouchCallback;

import java.util.List;

import static com.nicefontaine.seanachie.utils.Utils.leftShift;
import static com.nicefontaine.seanachie.utils.Utils.rightShift;


public class FormsAdapter extends RecyclerView.Adapter<FormsAdapter.FormHolder>
        implements ItemTouchCallback.OnItemTouchListener {

    private final Context mContext;
    private LayoutInflater inflater;
    private List<Form> forms;

    public FormsAdapter(Context context, List<Form> forms) {
        this.inflater = LayoutInflater.from(context);
        this.mContext = context;
        setForms(forms);
    }

    public void setForms(List<Form> forms) {
        this.forms = forms;
    }

    public List<Form> getForms() {
        return forms;
    }

    @Override
    public FormHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_item_form, parent, false);
        return new FormHolder(view);
    }

    @Override
    public void onBindViewHolder(FormHolder holder, int position) {
        final Form form = forms.get(position);
        holder.heading.setText(form.getSpecies());
        String categories = mContext.getString(R.string.recycler_forms_categories);
        holder.content.setText(String.format("%s %s", form.getCount(), categories));
    }

    @Override
    public void onItemDismiss(int position) {
        forms.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onItemMove(int from, int to) {
        forms = (from < to) ? rightShift(forms, from, to) : leftShift(forms, from, to);
        notifyItemMoved(from, to);
    }

    @Override
    public int getItemCount() {
        return forms.size();
    }

    class FormHolder extends RecyclerView.ViewHolder {
        private TextView heading;
        private TextView content;

        FormHolder(View itemView) {
            super(itemView);
            heading = (TextView) itemView.findViewById(R.id.recycler_forms_item_heading);
            content = (TextView) itemView.findViewById(R.id.recycler_forms_item_content);
        }
    }
}
