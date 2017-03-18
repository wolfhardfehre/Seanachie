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


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.nicefontaine.seanachie.R;
import com.nicefontaine.seanachie.data.models.Form;

import java.util.List;


public class FormPickerDialogFragment extends DialogFragment {

    private static List<Form> forms;
    private Context context;
    private OnFormSelectedListener mCallback;

    public interface OnFormSelectedListener {
        void onFormSelected(Form form);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        this.mCallback = (OnFormSelectedListener) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        FormPickerAdapter adapter = new FormPickerAdapter(context, R.layout.adapter_form_picker, forms);
        String text = String.format("%s", context.getString(R.string.navigation_forms));
        final AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(text)
                .setAdapter(adapter,
                        (dialog, position) -> mCallback.onFormSelected(forms.get(position)))
                .setNegativeButton(R.string.category_create_cancel,
                        (dialog, id) -> dismiss());
        return builder.create();
    }

    public static FormPickerDialogFragment getInstance(List<Form> forms) {
        FormPickerDialogFragment.forms = forms;
        return new FormPickerDialogFragment();
    }
}