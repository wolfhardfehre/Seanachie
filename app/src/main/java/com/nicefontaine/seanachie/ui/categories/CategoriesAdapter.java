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

package com.nicefontaine.seanachie.ui.categories;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nicefontaine.seanachie.R;
import com.nicefontaine.seanachie.data.models.Category;
import com.nicefontaine.seanachie.ui.ItemTouchCallback;

import java.util.List;

import static com.nicefontaine.seanachie.utils.Utils.leftShift;
import static com.nicefontaine.seanachie.utils.Utils.rightShift;


public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryHolder>
        implements ItemTouchCallback.OnItemTouchListener {

    private LayoutInflater inflater;
    private List<Category> categories;

    public CategoriesAdapter(Context context, List<Category> categories) {
        this.inflater = LayoutInflater.from(context);
        setCategories(categories);
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Category> getCategories() {
        return categories;
    }

    @Override
    public CategoriesAdapter.CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_item_category, parent, false);
        return new CategoriesAdapter.CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoriesAdapter.CategoryHolder holder, int position) {
        final Category category = categories.get(position);
        holder.key.setText(category.getKey());
    }

    @Override
    public void onItemDismiss(int position) {
        notifyItemRemoved(position);
    }

    @Override
    public void onItemMove(int from, int to) {
        categories = (from < to) ? rightShift(categories, from, to) : leftShift(categories, from, to);
        notifyItemMoved(from, to);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class CategoryHolder extends RecyclerView.ViewHolder {
        private TextView key;

        CategoryHolder(View itemView) {
            super(itemView);
            key = (TextView) itemView.findViewById(R.id.recycler_item_category_key);
        }
    }
}
