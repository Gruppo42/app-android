package com.gruppo42.app.ui.search;

import androidx.recyclerview.widget.DiffUtil;
import com.gruppo42.app.api.models.MovieItem;

import java.util.List;

public class DiffCallback extends DiffUtil.Callback {

    private List<MovieItem> oldItems;
    private List<MovieItem> newItems;

    public DiffCallback(List<MovieItem> oldItems, List<MovieItem> newItems)
    {
        this.oldItems = oldItems;
        this.newItems = newItems;
    }

    @Override
    public int getOldListSize() {
        return this.oldItems.size();
    }

    @Override
    public int getNewListSize() {
        return this.newItems.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return this.oldItems.get(oldItemPosition).getId()==this.newItems.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return this.oldItems.get(oldItemPosition).equals(this.newItems.get(newItemPosition));
    }
}
