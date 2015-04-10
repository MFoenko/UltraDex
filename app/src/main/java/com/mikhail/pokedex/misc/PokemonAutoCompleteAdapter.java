package com.mikhail.pokedex.misc;

import android.util.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.mikhail.pokedex.*;
import com.mikhail.pokedex.data.*;
import java.util.*;

public class PokemonAutoCompleteAdapter extends BaseAdapter implements Filterable, OnItemClickListener
{

    PokedexClasses.Pokemon[] allPokemon;
    ArrayList<PokedexClasses.Pokemon> filteredList;
    Filter filter;

	public PokedexClasses.Pokemon mPokemon;

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
		return filteredList.get(p1).id;
	}

	@Override
	public View getView(int p1, View p2, ViewGroup p3) {

        if(p2 == null){
            LayoutInflater inflater = LayoutInflater.from(p3.getContext());
            p2 = inflater.inflate(R.layout.pokemon_list_item, p3, false);
        }

        PokedexClasses.Pokemon poke = filteredList.get(p1);

        ImageView iconIV = (ImageView)p2.findViewById(R.id.icon);
		TextView idTV = (TextView)p2.findViewById(R.id.id);
        TextView nameTV = (TextView)p2.findViewById(R.id.name);

        iconIV.setImageBitmap(poke.loadBitmap(p2.getContext()));
		idTV.setText(String.valueOf(poke.dispId));
        nameTV.setText(poke.name);

        return p2;
	}

	@Override
	public Filter getFilter() {
		return filter;
	}

	@Override
	public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4) {

		 mPokemon = filteredList.get(p3);
	
	}

	
	
	
	private final class PokemonSearchFilter extends Filter {

                @Override
		protected Filter.FilterResults performFiltering(CharSequence p1) {

			if(p1 == null) return null;
			
//          int favoritesIndex = 0;
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
            notifyDataSetChanged();
			if(filteredList.size() == 1){
				mPokemon = filteredList.get(0);
			}
			else if(filteredList.size() > 1 && p1 != null)
			if(filteredList.get(0).name.toLowerCase().equals(p1.toString().toLowerCase())){
				mPokemon = filteredList.get(0);
			}
		}
	}
	
}
