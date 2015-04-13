package com.mikhail.pokedex.fragments;

import android.content.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v4.app.*;
import android.support.v4.view.*;
import android.support.v7.widget.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import com.mikhail.pokedex.*;
import com.mikhail.pokedex.data.*;
import com.mikhail.pokedex.misc.*;
import java.util.*;

/**
 * Created by mchail on 4/6/15.
 */
public class NaturesFragment extends Fragment implements DrawerItem {

    public static final String TITLE = "Natures";
    public static final int ICON = R.drawable.ic_natures;


	SharedPreferences mPrefs;
	
	
    RecyclerView mNaturesList;
    ViewGroup mNaturesTable;

    NaturesListAdapter mAdapter;
	NatureSortAdapter mSortAdapter;

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
        mNaturesList.setAdapter(mAdapter = new NaturesListAdapter());
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
		inflater.inflate(R.menu.sort, menu);
        Spinner sortSpinner = (Spinner)menu.findItem(R.id.sort).getActionView();
        sortSpinner.setAdapter(mSortAdapter = new NatureSortAdapter(mAdapter));
		sortSpinner.setOnItemSelectedListener(mSortAdapter);
        inflater.inflate(R.menu.switch_layout, menu);

	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		menu.findItem(R.id.sort).setVisible(mIsInListForm);
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
						cell.setBackgroundColor(0x66000000+PokedexDatabase.STAT_TOTAL_COLOR);
					}

                }
                row.addView(cell);
            }
            container.addView(row);
        }
    }

    protected static class NatureSortAdapter extends BaseAdapter implements AdapterView.OnItemSelectedListener {

        private Pair<String, Integer>[] sortOptions;
        private int sortBy;
        private NaturesListAdapter mAdapter;
        private PokedexClasses.Nature[] originalList = PokedexDatabase.NATURES;

        public NatureSortAdapter(NaturesListAdapter adapter){
            this.sortOptions = new Pair[]{
                    new Pair<String, Integer>("Name ▲", PokedexClasses.Nature.SORT_BY_NAME_ASC),
                    new Pair<String, Integer>("Name ▼", PokedexClasses.Nature.SORT_BY_NAME_DES),
                    new Pair<String, Integer>("Stat + ▲", PokedexClasses.Nature.SORT_BY_STAT_UP_ASC),
                    new Pair<String, Integer>("Stat + ▼", PokedexClasses.Nature.SORT_BY_STAT_UP_DES),
                    new Pair<String, Integer>("Stat - ▲", PokedexClasses.Nature.SORT_BY_STAT_DOWN_ASC),
                    new Pair<String, Integer>("Stat - ▼", PokedexClasses.Nature.SORT_BY_STAT_DOWN_DES)
            };
            this.mAdapter = adapter;
			mAdapter.listItems = new ArrayList<PokedexClasses.Nature>(Arrays.asList(originalList));
        }

        @Override
        public int getCount(){
            return sortOptions.length;
        }

        @Override
        public Object getItem(int p1){

            return sortOptions[p1];
        }

        @Override
        public long getItemId(int p1){
            return 0;
        }

        @Override
        public View getView(int pos, View view, ViewGroup container){
            if (sortOptions == null)
                return null;

            if (view == null){
                LayoutInflater inflater = LayoutInflater.from(container.getContext());
                view = inflater.inflate(R.layout.sort_spinner_list_item, container, false);
            }

            TextView labelTV = (TextView)view.findViewById(R.id.label);
            labelTV.setText(sortOptions[pos].first);

            return view;
        }

        @Override
        public void onNothingSelected(AdapterView<?> p1){
            //((TextView)((LinearLayout)p1.getChildAt(0)).findViewById(R.id.label)).setTextColor(0xFFFFFFFF);

        }

        public void sort(){
            sort(originalList, sortBy);
        }

        public void sort(PokedexClasses.VarComparable[] inputArr, int sortBy){

            if (inputArr == null || inputArr.length == 0){
                return;
            }

            int length = inputArr.length;
            try{
                quickSort(inputArr, length, sortBy, 0, length - 1);
            }catch(Exception e){
                e.printStackTrace();
            }


        }

        private void quickSort(PokedexClasses.VarComparable[]array, int length, int sortBy, int lowerIndex, int higherIndex){

            int i = lowerIndex;
            int j = higherIndex;
            // calculate pivot number, I am taking pivot as middle index number
            PokedexClasses.VarComparable pivot = array[lowerIndex + (higherIndex - lowerIndex) / 2];
            // Divide into two arrays
            while (i <= j){
                /**
                 * In each iteration, we will identify a number from left side which
                 * is greater then the pivot value, and also we will identify a number
                 * from right side which is less then the pivot value. Once the search
                 * is done, then we exchange both numbers.
                 */
                while (array[i].compareTo(pivot, sortBy) < 0){
                    i++;
                }
                while (array[j].compareTo(pivot, sortBy) > 0){
                    j--;
                }
                if (i <= j){
                    exchangeObjects(array, i, j);
                    //move index to next position on both sides
                    i++;
                    j--;
                }
            }
            // call quickSort() method recursively
            if (lowerIndex < j)
                quickSort(array, length, sortBy, lowerIndex, j);
            if (i < higherIndex)
                quickSort(array, length, sortBy, i, higherIndex);
        }

        private void exchangeObjects(PokedexClasses.VarComparable[] array, int i, int j){
            PokedexClasses.VarComparable temp = array[i];
            array[i] = array[j];
            array[j] = temp;
            adapterSwap(i, j);
        }


        private void adapterSwap(int i, int j){
            ArrayList<PokedexClasses.Nature> list = mAdapter.listItems;
            PokedexClasses.Nature k = list.get(j);
            list.set(j, list.get(i));
            list.set(i, k);
			//mAdapter.notifyItemMoved(i,j);
			//mAdapter.notifyItemMoved(j,i);
        }


        @Override
        public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4){
			sortBy = sortOptions[p3].second;
            sort();
			mAdapter.notifyDataSetChanged();
			/*try {
			 ((TextView) ((LinearLayout) p1.getChildAt(0)).findViewById(R.id.label)).setTextColor(0xFFFFFFFF);
			 }catch(NullPointerException e){
			 e.printStackTrace();
			 }*/
        }

    }


    public static final class NaturesListAdapter extends RecyclerView.Adapter<NatureViewHolder>{

        public ArrayList<PokedexClasses.Nature> listItems = new ArrayList<PokedexClasses.Nature>();

        @Override
        public NatureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new NatureViewHolder(inflater.inflate(R.layout.nature_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(NatureViewHolder holder, int position) {
            PokedexClasses.Nature nature = listItems.get(position);
            holder.nameTV.setText(nature.name);
           if(nature.statUp != nature.statDown){
               holder.statDownTV.setVisibility(View.VISIBLE);
               holder.statUpTV.setText("-" + PokedexDatabase.STAT_LABELS[PokedexDatabase.STAT_LABELS.length - 1][nature.statUp]);
               holder.statUpTV.setBackgroundColor(0x66000000 + PokedexDatabase.STAT_COLORS[PokedexDatabase.STAT_COLORS.length - 1][nature.statUp]);
               holder.statDownTV.setText("+" + PokedexDatabase.STAT_LABELS[PokedexDatabase.STAT_LABELS.length - 1][nature.statDown]);
               holder.statDownTV.setBackgroundColor(0x66000000 + PokedexDatabase.STAT_COLORS[PokedexDatabase.STAT_COLORS.length - 1][nature.statDown]);
           }else{
               holder.statDownTV.setVisibility(View.GONE);
               holder.statUpTV.setText("No Effect");
               holder.statUpTV.setBackgroundColor(0x66000000 + PokedexDatabase.STAT_TOTAL_COLOR);
           }
        }

        @Override
        public int getItemCount() {
            return listItems.size();
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
