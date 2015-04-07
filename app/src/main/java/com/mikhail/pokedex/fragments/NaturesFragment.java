package com.mikhail.pokedex.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mikhail.pokedex.R;
import com.mikhail.pokedex.data.PokedexClasses;
import com.mikhail.pokedex.data.PokedexDatabase;
import com.mikhail.pokedex.misc.DrawerItem;
import android.view.*;
import android.content.*;

/**
 * Created by mchail on 4/6/15.
 */
public class NaturesFragment extends Fragment implements DrawerItem {

    public static final String TITLE = "Natures";
    public static final int ICON = R.drawable.ic_natures;


	SharedPreferences mPrefs;
	
	
    RecyclerView mNaturesList;
    ViewGroup mNaturesTable;
	
	

	public final static String KEY_IS_IN_LIST = "isinlist";
	
	boolean mIsInListForm = true;


    public static final PokedexClasses.Nature[] NATURES = PokedexDatabase.NATURES;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.natures_fragment,container,false);
        mNaturesList = (RecyclerView)root.findViewById(R.id.natures_list);
        mNaturesTable = (ViewGroup)root.findViewById(R.id.natures_table);

        mNaturesList.setLayoutManager(new LinearLayoutManager(container.getContext()));
        mNaturesList.setAdapter(new NaturesListAdapter());
        mNaturesList.setVisibility(View.GONE);

        createNaturesTable(mNaturesTable);

		mPrefs = getActivity().getSharedPreferences("",0);
		mIsInListForm =  mPrefs.getBoolean(KEY_IS_IN_LIST, true);
		redrawLayout();
		
        return root;
    }

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.switch_layout, menu);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		
		menu.findItem(R.id.switch_layout).setIcon(!mIsInListForm? R.drawable.ic_view_stream_white_48dp:R.drawable.ic_view_module_white_48dp);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case R.id.switch_layout:
				mIsInListForm = ! mIsInListForm;
				redrawLayout();
				getActivity().invalidateOptionsMenu();
				mPrefs.edit().putBoolean(KEY_IS_IN_LIST, mIsInListForm).apply();
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void redrawLayout(){
		if(mIsInListForm){
			mNaturesList.setVisibility(View.VISIBLE);
			mNaturesTable.setVisibility(View.GONE);
		}else{
			mNaturesList.setVisibility(View.GONE);
			mNaturesTable.setVisibility(View.VISIBLE);
		}
	}
	

    private static void createNaturesTable(ViewGroup container){
        int NATURES_WIDTH = 5;
        Context context = container.getContext();

        for(int r = -1; r < NATURES_WIDTH; r++){

            LinearLayout row = new LinearLayout(context);
            row.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(ViewPager.LayoutParams.MATCH_PARENT, 0, 1);
            row.setLayoutParams(rowParams);
            for(int c=-1;c<NATURES_WIDTH;c++){

                TextView cell = new TextView(context);
                LinearLayout.LayoutParams cellParams = new LinearLayout.LayoutParams(0, ViewPager.LayoutParams.MATCH_PARENT, 1);
                cell.setLayoutParams(cellParams);
                cell.setGravity(Gravity.CENTER);
                if(r == -1 || c == -1){
                    if(r == -1 && c== -1){
                        cell.setText("+\\-");
                    }else {
                        if (r == -1) {
                            cell.setText(PokedexDatabase.STAT_LABELS[PokedexDatabase.STAT_LABELS.length-1][c+1]);
                            cell.setBackgroundColor(0x66000000 + PokedexDatabase.STAT_COLORS[PokedexDatabase.STAT_COLORS.length-1][c+1]);

                        }else/*if (c == -1) */
                        {
                            cell.setText(PokedexDatabase.STAT_LABELS[PokedexDatabase.STAT_LABELS.length-1][r+1]);
                            cell.setBackgroundColor(0x66000000 + PokedexDatabase.STAT_COLORS[PokedexDatabase.STAT_COLORS.length-1][r+1]);
                        }
                    }

                }else{
                    cell.setText(NATURES[r*NATURES_WIDTH+c].name);
					if(r==c){
						cell.setBackgroundColor(0x66+PokedexDatabase.STAT_TOTAL_COLOR);
					}

                }
                row.addView(cell);
            }
            container.addView(row);
        }
    }

    private static final class NaturesListAdapter extends RecyclerView.Adapter<NatureViewHolder>{

        public static final PokedexClasses.Nature[] NATURES = PokedexDatabase.NATURES;

        @Override
        public NatureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new NatureViewHolder(inflater.inflate(R.layout.nature_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(NatureViewHolder holder, int position) {
            PokedexClasses.Nature nature = NATURES[position];
            holder.nameTV.setText(nature.name);
            holder.statUpTV.setText("-"+PokedexDatabase.STAT_LABELS[PokedexDatabase.STAT_LABELS.length-1][nature.statUp]);
            holder.statUpTV.setBackgroundColor(0x66000000 + PokedexDatabase.STAT_COLORS[PokedexDatabase.STAT_COLORS.length-1][nature.statUp]);
            holder.statDownTV.setText("+"+PokedexDatabase.STAT_LABELS[PokedexDatabase.STAT_LABELS.length-1][nature.statDown]);
            holder.statDownTV.setBackgroundColor(0x66000000 + PokedexDatabase.STAT_COLORS[PokedexDatabase.STAT_COLORS.length-1][nature.statDown]);
        }

        @Override
        public int getItemCount() {
            return NATURES.length;
        }
    }

    private static final class NatureViewHolder extends RecyclerView.ViewHolder{

        TextView nameTV;
        TextView statUpTV;
        TextView statDownTV;

        private NatureViewHolder(View itemView) {
            super(itemView);

            nameTV = (TextView)itemView.findViewById(R.id.name);
            statUpTV = (TextView)itemView.findViewById(R.id.stat_up);
            statDownTV = (TextView)itemView.findViewById(R.id.stat_down);

        }
    }

    @Override
    public String getDrawerItemName() {
        return TITLE;
    }

    @Override
    public int getDrawerItemIconResourceId() {
        return ICON;
    }

    @Override
    public byte getDrawerItemType() {
        return DRAWER_ITEM_TYPE_CLICKABLE;
    }

    @Override
    public boolean onDrawerItemClick(Context context) {
        return true;
    }
}
