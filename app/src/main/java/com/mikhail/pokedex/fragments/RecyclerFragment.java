
package com.mikhail.pokedex.fragments;

import android.app.*;
import android.os.*;
import android.support.v7.widget.*;
import android.text.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.mikhail.pokedex.*;
import com.mikhail.pokedex.data.PokedexClasses.*;
import com.mikhail.pokedex.misc.*;
import java.util.*;

public abstract class RecyclerFragment<I, T extends VarComparableDexObject<T>/*, VarComparable<T>*/, VH extends RecyclerView.ViewHolder> extends InfoPagerFragment<I>{


	protected RecyclerView mRecyclerView;
	protected ScrollBarView mScrollerView;
	protected ListItemAdapter<T, VH> mAdapter;
	protected Filter mFilter;
	protected SortAdapter mSortAdapter;
	protected T[] mData;

    protected View.OnClickListener mClearFiltersOnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            mFilter.clear();
            mFilter.filter();
        }
    };

	Menu menu;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		View layout = inflater.inflate(R.layout.item_list, container, false);

		mRecyclerView = (RecyclerView)layout.findViewById(R.id.recycler_view);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
		mRecyclerView.setAdapter(mAdapter = getNewAdapter());

		mScrollerView = (ScrollBarView)layout.findViewById(R.id.scrollBar);
		mScrollerView.setRecyclerView(mRecyclerView);

		mFilter = getNewFilter(getActivity());
        mFilter.filter();

		return layout;
	}


	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		super.onCreateOptionsMenu(menu, inflater);
        menuItems = new MenuItem[2];
		this.menu = menu;
		//if (menuItems[0] == null){
			//menu.clear();
			inflater.inflate(R.menu.search, menu);
			MenuItem search = menuItems[0] = menu.findItem(R.id.search);
			View searchBar = search.getActionView();
			EditText searchView = (EditText)searchBar.findViewById(R.id.search_box);
			searchView.addTextChangedListener(new TextWatcher(){

					@Override
					public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4){
					}

					@Override
					public void onTextChanged(CharSequence p1, int p2, int p3, int p4){
						mFilter.search = p1.toString();
						mFilter.filter();
					}

					@Override
					public void afterTextChanged(Editable p1){
					}


				});
//		}
	//	if (menuItems[1] == null){
			inflater.inflate(R.menu.sort, menu);
			menuItems[1] = menu.findItem(R.id.sort);
			Spinner sortSpinner = (Spinner)menuItems[1].getActionView();
			sortSpinner.setAdapter(mSortAdapter = new SortAdapter(getSortOptions(), mFilter));
			sortSpinner.setOnItemSelectedListener(mSortAdapter);
	//	}
	}

	@Override
	public void onDestroyOptionsMenu(){
		super.onDestroyOptionsMenu();
		
	}



    public void clearFilters(View view){
        mFilter = getNewFilter(getActivity());
        mFilter.filter();
    }


	public abstract ListItemAdapter<T, VH> getNewAdapter();
	public abstract Filter<T, VH> getNewFilter(Activity a);
	public abstract Pair<String,Integer>[] getSortOptions();


	@Override
	public boolean displayData(){
		if (mData == null || mRecyclerView == null){
			return false;
		}
		mAdapter.setData(new ArrayList<T>(Arrays.asList(mData)));
		mFilter.setOriginalList(mData);
		mFilter.filter();
		return true;
	}



	protected abstract static class ListItemAdapter<LT extends DexObject, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>{


		ArrayList<LT> listItems;
		public int sortBy;

		public ListItemAdapter(ArrayList<LT> listItems){
			this.listItems = listItems;
		}

		public ListItemAdapter(){
			this.listItems = null;
		}

		public void setData(ArrayList<LT> listItems){
			this.listItems = listItems;
			sortBy = 0;
			notifyDataSetChanged();
		}

		@Override
		public int getItemCount(){
			if (listItems == null){
				return 0;
			}
			return listItems.size();
		}


		public int[] getIdArray(){
			int[] idArray = new int[listItems.size()];
			int len = idArray.length;
			for (int i=0;i < len;i++){
				idArray[i] = listItems.get(i).id;
			}
			return idArray;
		}


	}


	protected static class SortAdapter extends BaseAdapter implements OnItemSelectedListener{

		private Pair<String, Integer>[] sortOptions;
		private Filter mFilter;

		public SortAdapter(Pair<String, Integer>[] sortOptions, Filter mFilter){
			this.sortOptions = sortOptions;
			this.mFilter = mFilter;
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




		@Override
		public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4){
			mFilter.sortBy = sortOptions[p3].second;
			mFilter.filter();
			/*try {
			 ((TextView) ((LinearLayout) p1.getChildAt(0)).findViewById(R.id.label)).setTextColor(0xFFFFFFFF);
			 }catch(NullPointerException e){
			 e.printStackTrace();
			 }*/
		}






	}


	protected static abstract class Filter<T extends VarComparableDexObject<T>, VH extends RecyclerView.ViewHolder>{

		ListItemAdapter<T, VH> adapter;
		T[] mOriginalList;
		ArrayList<T> listItems;
		TimerTask filterTask;
		protected Activity mActivity;
		

		int sortBy=0;
		public String search = "";

		public Filter(final ListItemAdapter<T, VH> adapter, final Activity a){
			this.adapter = adapter;
			this.mActivity = a;
			listItems = adapter.listItems;
			filterTask = new TimerTask(){
				public void run(){
					listItems = adapter.listItems;
					if (listItems == null) return;

					listItems.clear();
					for (T item : mOriginalList){
						if (isMatchFilter(item))
							listItems.add(item);
					}
					/*	if(adapter.sortBy != sortBy) {*/
					sort(adapter.listItems.toArray(new VarComparable[0]), sortBy);
					adapter.sortBy = sortBy;
					//	}
					a.runOnUiThread(new Runnable(){
							public void run(){

								adapter.notifyDataSetChanged();
							}
						});
				}
			};
		}

		/*public Filter(ListItemAdapter<T, VH> adapter, T[] mOriginalList){
		 this(adapter);
		 setOriginalList(mOriginalList);
		 }*/

		public void setOriginalList(T[] mOriginalList){
			this.mOriginalList = mOriginalList;
		}

        public void clear(){
            search = "";
        }

		public void filter(){
			filterTask.cancel();
			filterTask.run();
		}

		public boolean isMatchFilter(T item){
			return isMatchSearch(item);
		}

		public abstract boolean isMatchSearch(T item);

        private void adapterSwap(int i, int j){
            ArrayList<T> list = adapter.listItems;
            T k = list.get(j);
            list.set(j, list.get(i));
            list.set(i, k);
        }








		public void sort(VarComparable[] inputArr, int sortBy){

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

		private void quickSort(VarComparable[]array, int length, int sortBy, int lowerIndex, int higherIndex){

			int i = lowerIndex;
			int j = higherIndex;
			// calculate pivot number, I am taking pivot as middle index number
			VarComparable pivot = array[lowerIndex + (higherIndex - lowerIndex) / 2];
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

		private void exchangeObjects(VarComparable[] array, int i, int j){
			VarComparable temp = array[i];
			array[i] = array[j];
			array[j] = temp;
			adapterSwap(i, j);
		}


	}

}
