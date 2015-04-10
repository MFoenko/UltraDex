package com.mikhail.pokedex.fragments;

import android.content.*;
import android.os.*;
import android.support.v4.app.*;
import android.view.*;
import android.widget.*;
import com.mikhail.pokedex.*;
import com.mikhail.pokedex.data.*;
import com.mikhail.pokedex.data.PokedexClasses.*;
import com.mikhail.pokedex.misc.*;
import android.widget.AdapterView.*;
import android.text.*;

public class IVCalculatorFragment extends Fragment implements DrawerItem {

	public static final String TITLE = "IV Calculator";
	public static final int ICON = R.drawable.ic_iv_calculator;

	AutoCompleteTextView mPokemonACTV;
	EditText mLevelET;
	Spinner mNatureSpinner;
	
	PokemonAutoCompleteAdapter mAutoAdapter;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.iv_calculator_fragment, container, false);
		mPokemonACTV = (AutoCompleteTextView)root.findViewById(R.id.pokemon_auto_complete);
		mLevelET = (EditText)root.findViewById(R.id.level);
		mNatureSpinner = (Spinner)root.findViewById(R.id.nature);
		
		
		mPokemonACTV.setAdapter(mAutoAdapter = new PokemonAutoCompleteAdapter(PokedexDatabase.getInstance(getActivity()).getAllPokemon()));
		mPokemonACTV.setOnItemClickListener(mAutoAdapter);
		mPokemonACTV.setThreshold(1);
		
		mLevelET.addTextChangedListener(
		
		mNatureSpinner.setAdapter(new NatureSpinnerAdapter());
		return root;
	}


	private class NatureSpinnerAdapter extends BaseAdapter {

		public static final Nature[] NATURES = PokedexDatabase.NATURES;
		
		@Override
		public int getCount() {
			return NATURES.length;
		}

		@Override
		public Object getItem(int p1) {
			return NATURES[p1];
		}

		@Override
		public long getItemId(int p1) {
			return 0;
		}

		@Override
		public View getView(int p1, View itemView, ViewGroup p3) {

			if(itemView ==null){
				LayoutInflater inflater = LayoutInflater.from(p3.getContext());
				itemView = inflater.inflate(R.layout.nature_list_item, p3, false);
			}

			TextView nameTV = (TextView)itemView.findViewById(R.id.name);
            TextView statUpTV = (TextView)itemView.findViewById(R.id.stat_up);
			TextView statDownTV = (TextView)itemView.findViewById(R.id.stat_down);
			
			
			PokedexClasses.Nature nature = NATURES[p1];
            nameTV.setText(nature.name);
			if(nature.statUp != nature.statDown){
				statDownTV.setVisibility(View.VISIBLE);
				statUpTV.setText("-" + PokedexDatabase.STAT_LABELS[PokedexDatabase.STAT_LABELS.length - 1][nature.statUp]);
				statUpTV.setBackgroundColor(0x66000000 + PokedexDatabase.STAT_COLORS[PokedexDatabase.STAT_COLORS.length - 1][nature.statUp]);
				statDownTV.setText("+" + PokedexDatabase.STAT_LABELS[PokedexDatabase.STAT_LABELS.length - 1][nature.statDown]);
				statDownTV.setBackgroundColor(0x66000000 + PokedexDatabase.STAT_COLORS[PokedexDatabase.STAT_COLORS.length - 1][nature.statDown]);
			}else{
				statDownTV.setVisibility(View.GONE);
				statUpTV.setText("No Effect");
				statUpTV.setBackgroundColor(0x66000000 + PokedexDatabase.STAT_TOTAL_COLOR);
			}
			
			return itemView;
		}
		
		
	}
	
	public static class EditTextBounds implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
			// TODO: Implement this method
		}

		@Override
		public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
			// TODO: Implement this method
		}

		@Override
		public void afterTextChanged(Editable p1) {
			// TODO: Implement this method
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
