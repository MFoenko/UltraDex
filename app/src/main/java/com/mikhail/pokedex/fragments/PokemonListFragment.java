package com.mikhail.pokedex.fragments;
import android.content.*;
import android.os.*;
import android.support.v7.widget.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.mikhail.pokedex.*;
import com.mikhail.pokedex.data.*;
import com.mikhail.pokedex.data.PokedexClasses.*;
import com.mikhail.pokedex.misc.*;
import java.util.*;
import com.mikhail.pokedex.activities.*;

public class PokemonListFragment extends RecyclerFragment implements DrawerItem{

	public static final String DRAWER_ITEM_NAME = "Pokedex";
	public static final int DRAWER_ITEM_ICON = DRAWER_ICON_NONE;
	public LoadIconsTask task;

	PokemonListAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState){
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState){

		mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
		
		Pokemon[] pokes = PokedexDatabase.getInstance(view.getContext()).getAllPokemon();
		mRecyclerView.setAdapter(adapter = new PokemonListAdapter(pokes));
		task = new LoadIconsTask(view.getContext());
		task.execute(pokes);
	}


	private static class PokemonListAdapter extends RecyclerView.Adapter<PokemonListAdapter.PokemonViewHolder>{

		ArrayList<Pokemon> pokemonList;

		public PokemonListAdapter(Pokemon[] pokemonList){
			this.pokemonList = new ArrayList<Pokemon>(Arrays.asList(pokemonList));
		}
		
		

		@Override
		public void onBindViewHolder(PokemonListAdapter.PokemonViewHolder p1, int p2){
			Pokemon p = pokemonList.get(p2);
			p1.name.setText(p.name);
			p1.id.setText(String.valueOf(p.dispId));
			p1.icon.setImageBitmap(p.icon);
		}


		@Override
		public PokemonViewHolder onCreateViewHolder(ViewGroup p1, int p2){
			LayoutInflater inflater = LayoutInflater.from(p1.getContext());
			View view = inflater.inflate(R.layout.pokemon_list_item, p1, false);
			PokemonViewHolder holder = new PokemonViewHolder(view);
			view.setOnClickListener(holder);
			return holder;
		}

		@Override
		public int getItemCount(){
			// TODO: Implement this method
			return pokemonList.size();
		}


		public int[] getIdArray(){
			int[] idArray = new int[pokemonList.size()];
			int len = idArray.length;
			for(int i=0;i<len;i++){
				idArray[i] = pokemonList.get(i).id;
			}
			return idArray;
		}
		
		public class PokemonViewHolder extends RecyclerView.ViewHolder implements OnClickListener{

			public final ImageView icon;
			public final TextView id;
			public final TextView name;
			public final TextView extra;

			public PokemonViewHolder(View view){
				super(view);
				this.icon = (ImageView)view.findViewById(R.id.icon);
				this.id = (TextView)view.findViewById(R.id.id);
				this.name = (TextView)view.findViewById(R.id.name);
				this.extra = (TextView)view.findViewById(R.id.extra);
			}

			@Override
			public void onClick(View p1){
				Intent intent = new Intent(p1.getContext(), PokemonInfoActivity.class);
				intent.putExtra(PokemonInfoActivity.EXTRA_ID_ARRAY, getIdArray());
				Log.i("AAA", "click position "+getPosition());
				intent.putExtra(PokemonInfoActivity.EXTRA_ID_INDEX, this.getPosition());
				p1.getContext().startActivity(intent);
			}
		}
		
	}
	
	
	
	@Override
	public String getDrawerItemName(){
		return DRAWER_ITEM_NAME;
	}

	@Override
	public int getDrawerItemIconResourceId(){
		return DRAWER_ITEM_ICON;
	}

	@Override
	public byte getDrawerItemType(){
		return DRAWER_ITEM_TYPE_CLICKABLE;
	}

	@Override
	public boolean onDrawerItemClick(Context context){
		return true;
	}
	
	private class LoadIconsTask extends AsyncTask<Pokemon,Integer, Void>{

		Context con;

		public LoadIconsTask(Context con){
			this.con = con;
		}
		
		@Override
		protected Void doInBackground(Pokemon[] p1){
			
			int len = p1.length;
			for(int i=0;i<len;i++){
				Pokemon p = p1[i];
				p.loadBitmap(con);
				Log.i("AAA", ""+p.icon);
				publishProgress(i);
				if(isCancelled()){
					break;
				}
				
			}
			
			return null;
			
		}

		@Override
		protected void onProgressUpdate(Integer[] values){
			super.onProgressUpdate(values);
			adapter.notifyItemChanged(values[0]);
		}

		
		
	}

}
