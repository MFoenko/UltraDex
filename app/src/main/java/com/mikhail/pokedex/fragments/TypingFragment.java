package com.mikhail.pokedex.fragments;
import android.graphics.drawable.*;
import android.os.*;
import android.support.v4.app.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;
import com.mikhail.pokedex.*;
import com.mikhail.pokedex.data.*;
import java.util.*;

public abstract class TypingFragment<T> extends InfoPagerFragment<T>{

	private static final String TITLE = "Typing";


	protected int[] mTypes;
	protected RecyclerView mRecyclerView;
	protected TypingAdapter mAdapter;
	GridLayoutManager mLayoutManager;

	public int mTypeVersion = PokedexDatabase.GEN_TYPE_VERSIONS[PokedexDatabase.GEN];


	@Override
	public boolean displayData(){
		if (mRecyclerView == null || mTypes == null){
			return false;
		}
		mRecyclerView.setAdapter(mAdapter = new TypingAdapter(generateTypeEffeciency()));
		mLayoutManager.setSpanSizeLookup(mAdapter.new SpanSizeLookup(mLayoutManager.getSpanCount()));
		return true;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		mRecyclerView  = new RecyclerView(container.getContext());
		int span = (int)(container.getWidth() / getResources().getDisplayMetrics().density / 100);
		span = 3;
		mRecyclerView.setLayoutManager(mLayoutManager = new GridLayoutManager(mRecyclerView.getContext(), span));
		return mRecyclerView;
	}
	static final int NOT_EFFECTIVE = 0;
	static final int NOT_VERY_EFFECTIVE = 1;
	static final int REGULLARLY_EFFECTIVE = 2;
	static final int SUPER_EFFECTIVE = 3;

	public TypeItem[] generateTypeEffeciency(){

		ArrayList<TypeItem>[] effectivesLists = {new ArrayList<TypeItem>(), new ArrayList<TypeItem>(), new ArrayList<TypeItem>(), new ArrayList<TypeItem>()};

		final int numTypes = PokedexDatabase.TYPE_EFFICIENCY[0].length;

		for (int oT = 0;oT < numTypes;oT++){
			float effectiveness = getEffectiveness(mTypes, oT);
			TypeItem typeItem = new TypeItem(PokedexDatabase.TYPE_NAMES[mTypeVersion][oT], effectiveness, oT);

			if (effectiveness == 1){
				effectivesLists[REGULLARLY_EFFECTIVE].add(typeItem);
			}else
			if (effectiveness == 0){
				effectivesLists[NOT_EFFECTIVE].add(typeItem);
			}else
			if (effectiveness < 1){
				effectivesLists[NOT_VERY_EFFECTIVE].add(typeItem);
			}else
			if (effectiveness > 1){
				effectivesLists[SUPER_EFFECTIVE].add(typeItem);
			}
		}


		ArrayList<TypeItem> itemListWithHeaders = new ArrayList<TypeItem>();
		int len = effectivesLists.length;
		for (int i=len - 1;i >= 0;i--){
			ArrayList<TypeItem> itemList = effectivesLists[i];
			if (itemList.size() > 0){
				itemListWithHeaders.add(new TypeItem(getHeaderLabel(i)));
			}
			itemListWithHeaders.addAll(itemList);
		}

		return itemListWithHeaders.toArray(new TypeItem[0]);

	}


	/**
	 @param section Sections are 0 for 0x, 1 for between 0x and 1x, 2 for 1x, 3 for >1x
	 */
	protected abstract String getHeaderLabel(int section);

	//protected abstract String getHeaderLabel(float effectivity);

	protected float getEffectiveness(int[] mTypes, int otherType){
		float total = 1;
		for (int mT:mTypes){
			total *= getEffectiveness(mT, otherType);
		}
		return total;
	}

	protected abstract float getEffectiveness(int mType, int otherType);

	private static class TypingAdapter extends RecyclerView.Adapter<TypeItemViewHolder>{

		private TypeItem[] displayList;

		public TypingAdapter(TypeItem[] displayList){
			this.displayList = displayList;
		}

		
		public class SpanSizeLookup extends GridLayoutManager.SpanSizeLookup{

			int mMaxSize = 1;
			public int[] mSpanSizes;

			public SpanSizeLookup(int mMaxSize){
				this.mMaxSize = mMaxSize;
				mSpanSizes = new int[]{1, mMaxSize};
			}

			@Override
			public int getSpanSize(int p1){
				return mSpanSizes[displayList[p1].itemType];
			}


		};

		@Override
		public TypingFragment.TypeItemViewHolder onCreateViewHolder(ViewGroup p1, int type){
			LayoutInflater inflater = LayoutInflater.from(p1.getContext());
			switch (type){
				case TypeItem.TYPE_EFFECTIVENESS:
					View typeItem = inflater.inflate(R.layout.type_effectiveness_item, p1, false);
					GradientDrawable bg = (GradientDrawable)p1.getContext().getResources().getDrawable(R.drawable.type_background);
					typeItem.setBackgroundDrawable(bg);
					return new TypeItemViewHolder(typeItem, TypeItem.TYPE_EFFECTIVENESS);

				case TypeItem.TYPE_HEADER:
					TextView tv = (TextView)inflater.inflate(R.layout.header, p1, false);
					return new TypeItemViewHolder(tv, TypeItem.TYPE_HEADER);
				default:
					return null;
			}
		}

		@Override
		public void onBindViewHolder(TypingFragment.TypeItemViewHolder p1, int p2){
			TypeItem item = displayList[p2];
			switch (item.itemType){

				case TypeItem.TYPE_EFFECTIVENESS:
					p1.mLabelTV.setText(item.label);
					String effectiveness = "x";
					switch ((int)(item.effectiveness * 10)){
						case 5:
							effectiveness += "\u00BD";
							break;
						case 2:
							effectiveness += "\u00BC";
							break;
						case 1:
							effectiveness += "\u215B";
							break;
						default:
							effectiveness += (int)item.effectiveness;
							break;

					}
					p1.mEffectivenessTV.setText(effectiveness);
					GradientDrawable bg = (GradientDrawable) p1.mView.getBackground();
					bg.setColor(PokedexDatabase.TYPE_COLORS[2][item.type]);
					break;
				case TypeItem.TYPE_HEADER:
					p1.mLabelTV.setText(item.label);

					break;

			}


		}

		@Override
		public int getItemCount(){
			return displayList.length;
		}

		@Override
		public int getItemViewType(int position){
			return displayList[position].itemType;
		}

	}

	private static class TypeItemViewHolder extends RecyclerView.ViewHolder{

		final View mView;
		final TextView mLabelTV;
		final TextView mEffectivenessTV;

		public TypeItemViewHolder(View view, byte type){
			super(view);
			mView = view;
			switch (type){
				case TypeItem.TYPE_EFFECTIVENESS:
					mLabelTV = (TextView)view.findViewById(R.id.label);
					mEffectivenessTV = (TextView)view.findViewById(R.id.effectiveness);
					break;
				case TypeItem.TYPE_HEADER:
					mLabelTV = (TextView)view;
					mEffectivenessTV = null;					
					break;
				default:
					mLabelTV = null;
					mEffectivenessTV = null;				
					break;
			}
		}
	}

	protected static class TypeItem{
		public static final byte TYPE_EFFECTIVENESS = 0;
		public static final byte TYPE_HEADER = 1;

		final String label;
		final float effectiveness;
		final byte itemType;
		final int type;

		public TypeItem(String label, float effectiveness, int type){
			this(label, effectiveness, TYPE_EFFECTIVENESS, type);
		}

		public TypeItem(String label){
			this(label, 0, TYPE_HEADER, -1);
		}


		public TypeItem(String label, float effectiveness, byte itemType, int type){
			this.label = label;
			this.effectiveness = effectiveness;
			this.itemType = itemType;
			this.type = type;
		}

		@Override
		public String getTitle(){
			return TITLE;
		}


	}

}
