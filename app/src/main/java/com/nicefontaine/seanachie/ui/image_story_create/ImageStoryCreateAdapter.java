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

package com.nicefontaine.seanachie.ui.image_story_create;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nicefontaine.seanachie.R;
import com.nicefontaine.seanachie.data.models.Category;

import java.util.List;


public class ImageStoryCreateAdapter extends RecyclerView.Adapter<ImageStoryCreateAdapter.CategoryHolder> {

    private final OnImageClickedListener callback;
    private LayoutInflater inflater;
    private List<Category> categories;

    public interface OnImageClickedListener {
        void onImageClicked(int position);
    }

    public ImageStoryCreateAdapter(ImageStoryCreateFragment imageStoryCreateFragment, Context context, List<Category> categories) {
        this.inflater = LayoutInflater.from(context);
        this.callback = imageStoryCreateFragment;
        setCategories(categories);
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Category> getCategories() {
        return categories;
    }

    @Override
    public ImageStoryCreateAdapter.CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_item_image_story_create, parent, false);
        return new ImageStoryCreateAdapter.CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageStoryCreateAdapter.CategoryHolder holder, int position) {
        final Category category = categories.get(position);
        holder.key.setText(category.getKey());
        String value = category.getValue();
        if (value != null) holder.value.setText(value);
        holder.image.setOnClickListener(v -> callback.onImageClicked(position));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CategoryHolder extends RecyclerView.ViewHolder {
        private final ImageView image;
        private TextView key;
        public EditText value;

        public CategoryHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.recycler_item_image_story_create_image);
            key = (TextView) itemView.findViewById(R.id.recycler_item_image_story_create_key);
            value = (EditText) itemView.findViewById(R.id.recycler_item_image_story_create_edittext);
        }
    }
}
