package com.mikhail.pokedex.fragments;

import android.content.*;
import android.os.*;
import android.support.v7.widget.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.mikhail.pokedex.*;
import com.mikhail.pokedex.activities.*;
import com.mikhail.pokedex.data.*;
import com.mikhail.pokedex.data.PokedexClasses.*;
import com.mikhail.pokedex.misc.*;
import java.util.*;
import com.mikhail.pokedex.fragments.RecyclerFragment.*;

public abstract class MoveListFragment<T> extends RecyclerFragment<T, Move, MoveListFragment.MoveListAdapter.MoveViewHolder>
{

	@Override
	public RecyclerFragment.ListItemAdapter<PokedexClasses.Move, MoveListFragment.MoveListAdapter.MoveViewHolder> getNewAdapter(){
		return new MoveListAdapter();
	}

	@Override
	public RecyclerFragment.Filter<PokedexClasses.Move, MoveListFragment.MoveListAdapter.MoveViewHolder> getNewFilter(){
		return new MoveFilter(mAdapter);
	}


	
	
	protected static class MoveListAdapter extends ListItemAdapter<Move, MoveListAdapter.MoveViewHolder>{

		
		@Override
		public MoveListFragment.MoveListAdapter.MoveViewHolder onCreateViewHolder(ViewGroup p1, int p2){
			LayoutInflater inflater = LayoutInflater.from(p1.getContext());
			View moveView = inflater.inflate(R.layout.move_list_item, p1, false);
			MoveViewHolder holder = new MoveViewHolder(moveView);
			moveView.setOnClickListener(holder);
			return holder;
		}

		@Override
		public void onBindViewHolder(MoveListFragment.MoveListAdapter.MoveViewHolder p1, int p2){
			Move m = listItems.get(p2);
			p1.nameTV.setText(m.name);
			p1.typeTV.setType(m.type);
		}

		public int[] getIdArray(){
			
			
			int[] idArray = new int[listItems.size()];
			int len = idArray.length;
			for(int i=0;i<len;i++){
				idArray[i] = listItems.get(i).id;
			}
			return idArray;
		}

		public class MoveViewHolder extends RecyclerView.ViewHolder implements OnClickListener{
			
			public final TextView nameTV;
			public final TypeView typeTV;
			public final ImageView classIV;
			
			
			public MoveViewHolder(View v){
				super(v);
				nameTV = (TextView)v.findViewById(R.id.name);
				typeTV = (TypeView)v.findViewById(R.id.type);
				classIV = (ImageView)v.findViewById(R.id.damage_class);
			}
			

			@Override
			public void onClick(View p1){
				Intent intent = new Intent(p1.getContext(), MoveInfoActivity.class);
				intent.putExtra(MoveInfoActivity.EXTRA_ID_ARRAY, getIdArray());
				intent.putExtra(MoveInfoActivity.EXTRA_ID_INDEX, getPosition());
				p1.getContext().startActivity(intent);
			}
			
		}
		
	}
	
	private static class MoveFilter extends Filter<Move, MoveListAdapter.MoveViewHolder>{

		public MoveFilter(ListItemAdapter<Move, MoveListAdapter.MoveViewHolder> adapter){
			super(adapter);
		}
		
		
		@Override
		public boolean isMatchSearch(PokedexClasses.Move item){
			return item.name.toLowerCase().indexOf(search.toLowerCase()) > -1 || item.description.toLowerCase().indexOf(search.toLowerCase())>-1;
		}
		
		
	}
	
	
}
