package com.mikhail.pokedex.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.*;

import com.mikhail.pokedex.R;
import com.mikhail.pokedex.data.PokedexDatabase;
import com.mikhail.pokedex.misc.*;
import android.content.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class TypeChartFragment extends Fragment implements DrawerItem
{

    public static final String TITLE = "Type Chart";
    public static final int ICON = R.drawable.ic_type_chart;

    int mTypeVersion;

    TypeChartView mTypeChart;
    Spinner mAttackSpinner;
    Spinner mDefendSpinner1;
    Spinner mDefendSpinner2;
    TextView mDamage;

    AdapterView.OnItemSelectedListener mSpinnerListener = new AdapterView.OnItemSelectedListener(){
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            mTypeChart.deselectAllCols();
            mTypeChart.deselectAllRows();

            int attackType = (Integer)mAttackSpinner.getSelectedItem();
            int defendType1 = (Integer)mDefendSpinner1.getSelectedItem();
            int defendType2 = (Integer)mDefendSpinner2.getSelectedItem();



            if(defendType1 > -1){
                mTypeChart.selectCol(defendType1);
            }
            if(defendType2 > -1){
                mTypeChart.selectCol(defendType2);
            }



            String damageMultiplierText;
            if(attackType > -1){
                mTypeChart.selectRow(attackType);

                if(defendType1 == -1 && defendType2==-1){
                    damageMultiplierText = "-";
                }else{
                    float damageMultiplier = 1;

                    if(defendType1 > -1){
                        damageMultiplier *= PokedexDatabase.TYPE_EFFICIENCY[mTypeVersion][attackType][defendType1];
                    }
                    if(defendType2>-1){
                        damageMultiplier *= PokedexDatabase.TYPE_EFFICIENCY[mTypeVersion][attackType][defendType2];
                    }

                    damageMultiplierText = "x" + String.valueOf(PokedexDatabase.getDamageMultiplierChar(damageMultiplier));

                }

            }else{
                damageMultiplierText = "-";
            }
            mDamage.setText(damageMultiplierText);

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTypeVersion = PokedexDatabase.getTypeVersion();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mLayout = inflater.inflate(R.layout.type_chart_fragment, container, false);

        mTypeChart = (TypeChartView)mLayout.findViewById(R.id.type_chart);
        mAttackSpinner = (Spinner)mLayout.findViewById(R.id.attack_type);
        mDefendSpinner1 = (Spinner)mLayout.findViewById(R.id.defend_type_1);
        mDefendSpinner2 = (Spinner)mLayout.findViewById(R.id.defend_type_2);
        mDamage = (TextView)mLayout.findViewById(R.id.damage_output);

        Context context = inflater.getContext();
        mAttackSpinner.setAdapter(new TypeSpinnerAdapter(PokedexDatabase.getInstance(context)));
        mDefendSpinner1.setAdapter(new TypeSpinnerAdapter(PokedexDatabase.getInstance(context)));
        mDefendSpinner2.setAdapter(new TypeSpinnerAdapter(PokedexDatabase.getInstance(context)));

        mAttackSpinner.setOnItemSelectedListener(mSpinnerListener);
        mDefendSpinner1.setOnItemSelectedListener(mSpinnerListener);
        mDefendSpinner2.setOnItemSelectedListener(mSpinnerListener);

        return mLayout;
    }

    private static class TypeSpinnerAdapter extends BaseAdapter{

        private int mTypeVersion;
        private PokedexDatabase mPokedexDatabase;

        private int[] mTypes;

        private TypeSpinnerAdapter(int mTypeVersion, PokedexDatabase dex) {
            this.mTypeVersion = mTypeVersion;
            this.mPokedexDatabase = dex;
            mTypes = new int[PokedexDatabase.TYPES[mTypeVersion].length+1];
			mTypes[0] = -1;
			for(int i=1;i<mTypes.length;i++){
				mTypes[i] = PokedexDatabase.TYPES[mTypeVersion][i-1];
			}
        }

        private TypeSpinnerAdapter(PokedexDatabase dex) {
            this(dex.getTypeVersion(), dex);
        }

        @Override
        public int getCount() {
            return mTypes.length;
        }

        @Override
        public Object getItem(int position) {
            return mTypes[position];
        }

        @Override
        public long getItemId(int position) {
            return mTypes[position];
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = new TypeView(parent.getContext());
            }

            ((TypeView)convertView).setType(mTypes[position]);
            return convertView;
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
