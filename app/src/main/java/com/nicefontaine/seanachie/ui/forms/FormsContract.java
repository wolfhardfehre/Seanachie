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


import com.nicefontaine.seanachie.data.models.Form;
import com.nicefontaine.seanachie.ui.BasePresenter;
import com.nicefontaine.seanachie.ui.BaseView;

import java.util.List;


public interface FormsContract {

    interface View extends BaseView<Presenter> {

        void loadForms(List<Form> forms);

        void noData();

        void initRecycler();

        void updateRecycler();
    }

    interface Presenter extends BasePresenter {

        void itemMoved(List<Form> forms);

        void itemRemoved(Integer formId);
    }
}
