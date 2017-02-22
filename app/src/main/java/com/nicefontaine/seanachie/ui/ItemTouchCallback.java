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

package com.nicefontaine.seanachie.ui;


import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import static android.support.v7.widget.helper.ItemTouchHelper.DOWN;
import static android.support.v7.widget.helper.ItemTouchHelper.END;
import static android.support.v7.widget.helper.ItemTouchHelper.START;
import static android.support.v7.widget.helper.ItemTouchHelper.UP;


public class ItemTouchCallback extends ItemTouchHelper.Callback {

    private final OnItemTouchListener[] listeners;

    public interface OnItemTouchListener {

        void onItemMove(int fromPosition, int toPosition);

        void onItemDismiss(int position);
    }

    public ItemTouchCallback(OnItemTouchListener... listeners) {
        this.listeners = listeners;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(UP | DOWN, START | END);
    }

    @Override
    public boolean onMove(RecyclerView view, RecyclerView.ViewHolder holder,
                          RecyclerView.ViewHolder target) {
        for (OnItemTouchListener listener : listeners) {
            listener.onItemMove(holder.getAdapterPosition(), target.getAdapterPosition());
        }
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        for (OnItemTouchListener listener : listeners) {
            listener.onItemDismiss(viewHolder.getAdapterPosition());
        }
    }
}