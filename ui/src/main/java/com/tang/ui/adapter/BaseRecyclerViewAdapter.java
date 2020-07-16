/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tang.ui.adapter;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 添加点击事件的RecyclerViewAdapter
 */

public abstract class BaseRecyclerViewAdapter<V extends BaseRecyclerViewAdapter.ViewHolder> extends RecyclerView.Adapter<V> {

    private OnRecyclerViewItemClickListener itemClickListener;
    private OnRecyclerViewItemLongClickListener itemLongClickListener;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public void setOnItemLongClickListener(OnRecyclerViewItemLongClickListener itemLongClickListener){
        this.itemLongClickListener = itemLongClickListener;
    }

    public interface OnRecyclerViewItemClickListener {

        void OnItemClick(View v, int position);

    }

    public interface OnRecyclerViewItemLongClickListener {

        void onItemLongClick(View v, int position);

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(itemClickListener != null){
                itemClickListener.OnItemClick(v,getLayoutPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if(itemLongClickListener != null){
                itemLongClickListener.onItemLongClick(v,getLayoutPosition());
                return true;
            }
            return false;
        }

    }
}
