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

package com.nicefontaine.seanachie.ui.formcreate;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.nicefontaine.seanachie.R;
import com.nicefontaine.seanachie.SeanachieApp;
import com.nicefontaine.seanachie.data.models.Category;
import com.nicefontaine.seanachie.ui.BaseActivity;
import com.nicefontaine.seanachie.ui.ItemTouchCallback;
import com.nicefontaine.seanachie.ui.categories.CategoriesAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.support.design.widget.Snackbar.LENGTH_LONG;
import static android.widget.LinearLayout.VERTICAL;
import static com.nicefontaine.seanachie.ui.BaseActivity.NAVIGATION_FORMS;


public class FormCreateFragment extends Fragment implements
        FormCreateContract.View,
        ItemTouchCallback.OnItemTouchListener {

    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.f_create_form_coordinator) protected CoordinatorLayout coordinator;
    @BindView(R.id.f_create_form_recycler) protected RecyclerView recycler;
    @BindView(R.id.f_create_form_edittext) protected EditText editText;
    private Context context;
    private List<Category> categories;
    private CategoriesAdapter adapter;
    private FormCreateContract.Presenter presenter;

    public static FormCreateFragment newInstance() {
        return new FormCreateFragment();
    }

    public FormCreateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_form_create, container, false);
        ((SeanachieApp) context.getApplicationContext()).getAppComponent().inject(this);
        ButterKnife.bind(this, view);
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        ((BaseActivity) context).initNavigationDrawer(toolbar);
        toolbar.setTitle(R.string.navigation_form_create);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @OnClick(R.id.f_create_form_fab)
    public void createForm() {
        presenter.createForm(editText.getText().toString());
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        categories = adapter.getCategories();
        presenter.itemMoved(categories);
        Snackbar.make(coordinator, "Elements reordered", LENGTH_LONG).show();
    }

    @Override
    public void onItemDismiss(int position) {
        Category category = categories.get(position);
        Integer categoryId = category.getId();
        categories.remove(category);
        presenter.itemRemoved(categoryId);
        Snackbar.make(coordinator, "Element deleted", LENGTH_LONG).show();
    }

    @Override
    public void setPresenter(FormCreateContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void loadCategories(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public void noData() {
        Snackbar.make(coordinator, "No data", LENGTH_LONG).show();
    }

    @Override
    public void initRecycler() {
        if (categories == null) categories = new ArrayList<>();
        this.adapter = new CategoriesAdapter(context, categories);
        recycler.setLayoutManager(new LinearLayoutManager(context, VERTICAL, false));
        recycler.setAdapter(adapter);
        initItemTouchCallback();
    }

    private void initItemTouchCallback() {
        ItemTouchHelper.Callback callback = new ItemTouchCallback(this, adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recycler);
    }

    @Override
    public void updateRecycler() {
        adapter.setCategories(categories);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void finish() {
        ((BaseActivity) context).changeContent(NAVIGATION_FORMS);
    }
}
