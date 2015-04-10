package com.mikhail.pokedex.fragments;

import android.content.*;
import android.os.*;
import android.support.v4.app.*;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.mikhail.pokedex.*;
import com.mikhail.pokedex.data.*;
import com.mikhail.pokedex.data.PokedexClasses.*;
import com.mikhail.pokedex.misc.*;
import android.widget.AdapterView.*;
import android.text.*;

import java.util.ArrayList;

public class IVCalculatorFragment extends Fragment implements DrawerItem {

	public static final String TITLE = "IV Calculator";
	public static final int ICON = R.drawable.ic_iv_calculator;

	AutoCompleteTextView mPokemonACTV;
	EditText mLevelET;
	Spinner mNatureSpinner;
	EditText[] mStatETs;
    EditText[] mEVsETs;
    TextView[] mIVsTVs;
    Button mCalculateButton;

	PokemonAutoCompleteAdapter mAutoAdapter;

    public static final String ERR_NO_POKEMON = "No Pokemon Selected";
    public static final String ERR_INVALID_LEVEL = "Invalid Level";
    public static final String ERR_LEVEL_OOB = "Level must be between 1 and 100";
    public static final String ERR_STAT_OOB = "";
    public static final String ERR_EV_OOB = " EV must be between 0 and 255";

    public static final String ERR_INVALID_STAT_SUFFIX = " is invalid";
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup)inflater.inflate(R.layout.iv_calculator_fragment, container, false);
		mPokemonACTV = (AutoCompleteTextView)root.findViewById(R.id.pokemon_auto_complete);
		mLevelET = (EditText)root.findViewById(R.id.level);
		mNatureSpinner = (Spinner)root.findViewById(R.id.nature);
        mStatETs = new EditText[6];
        mEVsETs = new EditText[6];
        mIVsTVs = new TextView[6];
        mCalculateButton = (Button)root.findViewById(R.id.calculate_button);


        for(int i=3; i<=8;i++){
            ViewGroup rowVG = (ViewGroup)root.getChildAt(i);
            mStatETs[i-3] = (EditText)rowVG.getChildAt(1);

            mEVsETs[i-3] = (EditText)rowVG.getChildAt(2);
            mIVsTVs[i-3] = (TextView)rowVG.getChildAt(3);
        }

		
		mPokemonACTV.setAdapter(mAutoAdapter = new PokemonAutoCompleteAdapter(PokedexDatabase.getInstance(getActivity()).getAllPokemon()));
		mPokemonACTV.setOnItemClickListener(mAutoAdapter);
		mPokemonACTV.setThreshold(1);

		mNatureSpinner.setAdapter(new NatureSpinnerAdapter());

        mCalculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calculateIVs(v);

            }
        });
		return root;
	}


	private static class NatureSpinnerAdapter extends BaseAdapter {

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

    public void calculateIVs(View v){

        int[] baseStats = null;
        int lvl = 0;
        float[] natureMod = new float[]{1,1,1,1,1,1};
        int[] actualStats = new int[mStatETs.length];
        int[] evs = new int[mEVsETs.length];

        try {
            baseStats = mAutoAdapter.mPokemon.stats;
        } catch (NullPointerException e) {
            toast(ERR_NO_POKEMON);
        }

        try {
            lvl = Integer.parseInt(mLevelET.getText().toString());
        }catch(NumberFormatException e){
            toast(ERR_INVALID_LEVEL);
        }

        if(lvl <1 || lvl >100){
            toast(ERR_LEVEL_OOB);
        }

        Nature nature = (Nature)mNatureSpinner.getSelectedItem();
        if(nature.statUp != nature.statDown) {
            natureMod[nature.statUp] = 1.1f;
            natureMod[nature.statDown] = .9f;
        }

        for(int i = 0; i<mIVsTVs.length;i++){
            try {
                actualStats[i] = Integer.parseInt(mStatETs[i].getText().toString());
            }catch(NumberFormatException e){
                toast(PokedexDatabase.STAT_LABELS[PokedexDatabase.getStatVersion()][i] + ERR_INVALID_STAT_SUFFIX);
            }
            try {
                evs[i] = Integer.parseInt(mEVsETs[i].getText().toString());
            }catch(NumberFormatException e){
                toast(PokedexDatabase.STAT_LABELS[PokedexDatabase.getStatVersion()][i] + " EV" + ERR_INVALID_STAT_SUFFIX);
            }

            if(evs[i] < 0 || evs[i] > 255){
                toast(PokedexDatabase.STAT_LABELS[PokedexDatabase.getStatVersion()][i] + ERR_EV_OOB);
            }

            StringBuilder statRange = new StringBuilder();
            ArrayList<Integer> stats = new ArrayList<>();
            for(int iv=0;iv<=31;iv++) {
                if (i == 0) {
                    if((int)(((iv+(2f*baseStats[i])+evs[i]/4f+100f)*lvl)/100f + 10f) == actualStats[i]){
                        stats.add(iv);
                    }
                } else {
                    if((int)(((((iv+(2*baseStats[i]) + (evs[i]/4f)) *lvl)/100f)+5f)*natureMod[i]) == actualStats[i]){
                        stats.add(iv);
                    }
                }
            }
            if(stats.size() == 0){

            }else {
                boolean rangeStart = true;
                statRange.append(stats.get(0));

                for (int j = 1; j < stats.size(); j++) {
                    if(!rangeStart){
                        statRange.append(",").append(stats.get(j));
                        rangeStart = true;
                    }else
                    if(stats.get(j) != stats.get(j-1)+1 || j == stats.size()-1){
                        statRange.append("-").append(stats.get(j));
                        rangeStart = false;
                    }
                }
            }
            //statRange.append(stats.toString());
            mIVsTVs[i].setText(statRange.toString());

        }

    }

    public void toast(String text){
        Toast.makeText(getActivity(),text,Toast.LENGTH_SHORT).show();
    }
	
	/*public static class EditTextBounds implements TextWatcher {

        int mUpperBound;
        int mLowerBound;

        public EditTextBounds(int mLowerBound, int mUpperBound) {
            this.mUpperBound = mUpperBound;
            this.mLowerBound = mLowerBound;
        }

        @Override
		public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {
			// TODO: Implement this method
		}

		@Override
		public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {
            try {
                int num = Integer.parseInt(p1.toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

		@Override
		public void afterTextChanged(Editable p1) {
			// TODO: Implement this method
		}
		
		public void throwError(String error){

        }

	}*/


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
