package com.mikhail.pokedex.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikhail.pokedex.R;
import com.mikhail.pokedex.activities.ItemInfoActivity;
import com.mikhail.pokedex.data.PokedexClasses;
import com.mikhail.pokedex.data.PokedexClasses.*;

/**
 * Created by mchail on 4/4/15.
 */
public abstract class ItemListFragment<I> extends RecyclerFragment<I, Item, ItemListFragment.ItemListItemAdapter.ItemViewHolder> {


    @Override
    public ListItemAdapter getNewAdapter() {
        return new ItemListItemAdapter();
    }

    @Override
    public Filter getNewFilter(Activity a) {
        return new ItemFilter(mAdapter, a);
    }

    @Override
    public Pair<String, Integer>[] getSortOptions() {
        return new Pair[]{
                new Pair<String, Integer>("Name \u25B2", Pokemon.SORT_BY_NAME_ASC),
                new Pair<String, Integer>("Name \u25BC", Pokemon.SORT_BY_NAME_DES),
        };
    }

    protected static class ItemFilter extends RecyclerFragment.Filter<Item, ItemListItemAdapter.ItemViewHolder>{

        public LoadIconsTask task;
        Activity mActivity;


        public ItemFilter(final ListItemAdapter<Item, ItemListItemAdapter.ItemViewHolder> adapter, final Activity a) {
            super(adapter, a);
            mActivity = a;
        }

            @Override
        public boolean isMatchSearch(Item item) {
            return true;
        }

        @Override
        public void sort(VarComparable[] inputArr, int sortBy) {
            super.sort(inputArr, sortBy);
            if(task != null){
                task.cancel(true);
            }
            task = new LoadIconsTask(mActivity, adapter);
            task.execute(adapter.listItems.toArray(new Item[0]));
        }
    }



    protected static class ItemListItemAdapter extends RecyclerFragment.ListItemAdapter<Item, ItemListItemAdapter.ItemViewHolder>{


        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new ItemViewHolder(inflater.inflate(R.layout.item_list_item, parent, false));

        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            Item i = listItems.get(position);
            holder.mIconIV.setImageBitmap(i.icon);
            holder.mNameTV.setText(i.name);

        }

        public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            public ImageView mIconIV;
            public TextView mNameTV;

            public ItemViewHolder(View view){
                super(view);
                mIconIV = (ImageView)view.findViewById(R.id.icon);
                mNameTV = (TextView)view.findViewById(R.id.name);
                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ItemInfoActivity.class);
                i.putExtra(ItemInfoActivity.EXTRA_ID_ARRAY, getIdArray());
                i.putExtra(ItemInfoActivity.EXTRA_ID_INDEX, getPosition());
                v.getContext().startActivity(i);
            }
        }

    }

    private static class LoadIconsTask extends AsyncTask<Item ,Integer, Void> {

        Context con;
        RecyclerView.Adapter mAdapter;

        public LoadIconsTask(Context con, RecyclerView.Adapter mAdapter) {
            this.con = con;
            this.mAdapter = mAdapter;
        }

        @Override
        protected Void doInBackground(Item[] p1) {

            int len = p1.length;
            for (int i=0;i < len;i++) {
                Item item = p1[i];
                item.loadBitmap(con);
                publishProgress(i);
                if (isCancelled()) {
                    break;
                }

            }

            return null;

        }

        @Override
        protected void onProgressUpdate(Integer[] values) {
            super.onProgressUpdate(values);
            mAdapter.notifyItemChanged(values[0]);

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }


    }


}
