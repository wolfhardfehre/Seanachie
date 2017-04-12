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

package com.nicefontaine.seanachie.ui.category;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import com.nicefontaine.seanachie.R;
import com.nicefontaine.seanachie.SeanachieApp;
import com.nicefontaine.seanachie.data.models.Category;
import com.nicefontaine.seanachie.data.sources.DataSource;
import com.nicefontaine.seanachie.data.sources.categories.CategoriesRepository;

import java.util.List;

import javax.inject.Inject;


public class CategoryFragmentDialog extends DialogFragment implements
        DataSource.LoadDataCallback<Category> {

    @Inject protected CategoriesRepository categoriesRepository;

    private static OnCategoryListener listener;
    private Context context;
    private EditText editText;

    public interface OnCategoryListener {
        void onRefreshCategories();
    }

    public static CategoryFragmentDialog getInstance(OnCategoryListener listener) {
        CategoryFragmentDialog.listener = listener;
        return new CategoryFragmentDialog();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow()
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ((SeanachieApp) context.getApplicationContext()).getAppComponent().inject(this);
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        @SuppressLint("InflateParams")
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(R.string.category_create_title)
                .setView(getActivity()
                        .getLayoutInflater()
                        .inflate(R.layout.fragment_category, null))
                .setNegativeButton(R.string.category_create_cancel, (dialog, id) -> dismiss())
                .setPositiveButton(R.string.category_create_save, (dialog, id) -> {
                    editText = (EditText) getDialog().findViewById(R.id.f_create_category_edittext);
                    categoriesRepository.getData(this);
                });
        return builder.create();
    }

    @Override
    public void onDataLoaded(List<Category> categories) {
        createCategory(categories.size() + 1);
    }

    @Override
    public void noData() {
        createCategory(1);
    }

    private void createCategory(int position) {
        String name = editText.getText().toString();
        Category category = new Category(position, name);
        categoriesRepository.create(category);
        listener.onRefreshCategories();
        dismiss();
    }
}
