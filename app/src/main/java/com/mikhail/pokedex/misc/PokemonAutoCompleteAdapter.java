package com.mikhail.pokedex.misc;

import android.util.*;
import android.view.*;
import android.widget.*;

public class PokemonAutoCompleteAdapter extends BaseAdapter implements Filterable 
{

	Filter filter = new PokemonSearchFilter();
	
	@Override
	public int getCount() {
		return 0;
	}

	@Override
	public Object getItem(int p1) {
		return null;
	}

	@Override
	public long getItemId(int p1) {
		return 0;
	}

	@Override
	public View getView(int p1, View p2, ViewGroup p3) {
		return null;
	}

	@Override
	public Filter getFilter() {
		return filter;
	}
	
	private static final class PokemonSearchFilter extends Filter {

		@Override
		protected Filter.FilterResults performFiltering(CharSequence p1) {
			Log.e("AAA", p1.toString());
			return null;
		}

		@Override
		protected void publishResults(CharSequence p1, Filter.FilterResults p2) {
			// TODO: Implement this method
		}
	}
	
}
