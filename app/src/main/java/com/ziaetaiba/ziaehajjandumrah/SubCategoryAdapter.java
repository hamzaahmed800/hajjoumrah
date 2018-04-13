package com.ziaetaiba.ziaehajjandumrah;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ziaetaiba.hajjandumrah.R;
import com.ziaetaiba.ziaehajjandumrah.models.SubCategory;

import java.util.List;

public class SubCategoryAdapter extends ArrayAdapter<SubCategory> {

	private List<SubCategory> itemList,backupItemList;
	private Context context;
	private Typeface urduFont = null;
	private float textSize = 40f;
	private boolean isDefaultItem = true;

	/**
	 * @return the isDefaultItem
	 */
	public boolean getIsDefaultItem() {
		return isDefaultItem;
	}

	/**
	 * @param isDefaultItem the isDefaultItem to set
	 */
	public void setIsDefaultItem(boolean isDefaultItem) {
		this.isDefaultItem = isDefaultItem;
	}

	
	public float getTextSize() {
		return textSize;
	}

	public void setTextSize(float textSize) {
		this.textSize = textSize;
	}

	public SubCategoryAdapter(List<SubCategory> itemList, Context ctx) {
		super(ctx, android.R.layout.simple_list_item_1, itemList);
		urduFont = Typeface.createFromAsset(ctx.getApplicationContext()
				.getAssets(), "Jameel_Noori_Nastaleeq.ttf");
		this.itemList = itemList;
	//	this.backupItemList = itemList;
		this.context = ctx;
	}


	public int getCount() {
		if (itemList != null)
			return itemList.size();
		return 0;
	}

	public SubCategory getItem(int position) {
		if (itemList != null)
			return itemList.get(position);
		return null;
	}

	public long getItemId(int position) {
		if (itemList != null)
			return itemList.get(position).hashCode();
		return 0;
	}



	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View v = convertView;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.sub_category_item_list, null);
		}
		SubCategory item = itemList.get(position);
		
		ImageView itemIcon = (ImageView) v.findViewById(R.id.item_icon);
		if(item.getChildCategoryAvailable() > 0) {
			itemIcon.setImageResource(R.drawable.item_list_child);
		} else {
			itemIcon.setImageResource(R.drawable.item_list_parent);
		}
		
		TextView txtItems = (TextView) v.findViewById(R.id.sub_category);
//		txtItems.setPadding(15, 15, 15, 15);
		txtItems.setTypeface(urduFont);
		txtItems.setGravity(Gravity.RIGHT);
		txtItems.setText(item.getSubCategory());
		txtItems.setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextSize());
		txtItems.setTag(item);
		
		//txtItems.setOnClickListener(mOnTitleClickListener);

		return v;

	}


//
//	@Override
//	public Filter getFilter() {
//		return new Filter() {
//			@Override
//			protected FilterResults performFiltering(CharSequence charSequence) {
//				String charString = charSequence.toString();
//				if (charString.isEmpty()) {
//					itemList = backupItemList;
//				} else {
//					List<SubCategory> filteredList = new ArrayList<SubCategory>();
//					for (SubCategory row : backupItemList) {
//						if (row.getSubCategory().toLowerCase().contains(charString.toLowerCase())) {
//							filteredList.add(row);
//						}
//					}
//
//					itemList = filteredList;
//				}
//
//				FilterResults filterResults = new FilterResults();
//				filterResults.values = itemList;
//				return filterResults;
//			}
//
//			@Override
//			protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//				itemList = (ArrayList<SubCategory>) filterResults.values;
//				notifyDataSetChanged();
//			}
//		};
//	}

	public List<SubCategory> getItemList() {
		return itemList;
	}

	public void setItemList(List<SubCategory> itemList) {
		this.itemList = itemList;
	}
	
}
