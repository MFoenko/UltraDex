package com.mikhail.pokedex.fragments;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikhail.pokedex.R;
import com.mikhail.pokedex.data.UserDatabase;

/**
 * Created by mchail on 4/16/15.
 */
public abstract class CollectionListFragment<I> extends RecyclerFragment<I, UserDatabase.Collection, CollectionListFragment.CollectionListAdapter.CollectionViewHolder>{


    public static final String TITLE = "Collections";

    @Override
    public ListItemAdapter<UserDatabase.Collection, CollectionListAdapter.CollectionViewHolder> getNewAdapter() {
        return new CollectionListAdapter();
    }

    @Override
    public Filter<UserDatabase.Collection, CollectionListAdapter.CollectionViewHolder> getNewFilter(Activity a) {
        return new CollectionsFilter(mAdapter, a);
    }

    @Override
    public Pair<String, Integer>[] getSortOptions() {
        return new Pair[0];
    }

     @Override
    public String getTitle() {
        return TITLE;
    }

    protected static class CollectionListAdapter extends RecyclerFragment.ListItemAdapter<UserDatabase.Collection, CollectionListAdapter.CollectionViewHolder>{

        @Override
        public CollectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new CollectionViewHolder(inflater.inflate(R.layout.collection_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(CollectionViewHolder holder, int position) {
            UserDatabase.Collection collection = listItems.get(position);
            holder.nameTV.setText(collection.name);
        }

        public class CollectionViewHolder extends RecyclerView.ViewHolder{

            TextView nameTV;

            public CollectionViewHolder(View itemView) {
                super(itemView);
                nameTV = (TextView)itemView.findViewById(R.id.name);
            }
        }
    }

    protected static class CollectionsFilter extends Filter<UserDatabase.Collection, CollectionListAdapter.CollectionViewHolder>{


        public CollectionsFilter(ListItemAdapter<UserDatabase.Collection, CollectionListAdapter.CollectionViewHolder> adapter, Activity a) {
            super(adapter, a);
        }

        @Override
        public boolean isMatchSearch(UserDatabase.Collection item) {
            return item.name.toLowerCase().contains(search.toLowerCase());
        }
    }

}
