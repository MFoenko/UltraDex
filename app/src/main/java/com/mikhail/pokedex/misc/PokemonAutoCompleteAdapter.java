package com.mikhail.pokedex.misc;

import android.util.*;
import android.view.*;
import android.widget.*;

import com.mikhail.pokedex.R;
import com.mikhail.pokedex.data.PokedexClasses;

import java.util.ArrayList;
import java.util.Arrays;

public class PokemonAutoCompleteAdapter extends BaseAdapter implements Filterable 
{

    PokedexClasses.Pokemon[] allPokemon;
    ArrayList<PokedexClasses.Pokemon> filteredList;
    Filter filter;

    public PokemonAutoCompleteAdapter(PokedexClasses.Pokemon[] pokemonArray){
        allPokemon = pokemonArray;
        filteredList = new ArrayList<PokedexClasses.Pokemon>();
        filter = new PokemonSearchFilter();
    }
	
	@Override
	public int getCount() {
		return filteredList.size();
	}

	@Override
	public Object getItem(int p1) {
		return filteredList.get(p1);
	}

	@Override
	public long getItemId(int p1) {
		return 0;
	}

	@Override
	public View getView(int p1, View p2, ViewGroup p3) {

        if(p2 == null){
            LayoutInflater inflater = LayoutInflater.from(p3.getContext());
            p2 = inflater.inflate(R.layout.pokemon_list_item, p3, false);
        }

        PokedexClasses.Pokemon poke = filteredList.get(p1);

        ImageView iconIV = (ImageView)p2.findViewById(R.id.icon);
        TextView nameTV = (TextView)p2.findViewById(R.id.name);

        iconIV.setImageBitmap(poke.loadBitmap(p2.getContext()));
        nameTV.setText(poke.name);

        return p2;
	}

	@Override
	public Filter getFilter() {
		return filter;
	}
	
	private final class PokemonSearchFilter extends Filter {

                @Override
		protected Filter.FilterResults performFiltering(CharSequence p1) {

            int favoritesIndex = 0;
            int firstLetterIndex = 0;
            int otherMatchesIndex = 0;
            filteredList.clear();

            for(PokedexClasses.Pokemon poke:allPokemon){
                if(poke.name.toLowerCase().indexOf(p1.toString().toLowerCase()) == 0){
                    filteredList.add(firstLetterIndex, poke);
                    firstLetterIndex++;
                    otherMatchesIndex++;
                }else if(poke.name.toLowerCase().contains(p1.toString().toLowerCase())){
                    filteredList.add(otherMatchesIndex, poke);
                    otherMatchesIndex++;
                }
            }

			return null;
		}

		@Override
		protected void publishResults(CharSequence p1, Filter.FilterResults p2) {
            Log.i("AAA", p1.toString());
            notifyDataSetChanged();
		}
	}
	
}
