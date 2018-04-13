package com.ziaetaiba.ziaehajjandumrah;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.text.util.Linkify;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ziaetaiba.hajjandumrah.R;
import com.ziaetaiba.ziaehajjandumrah.models.Detail;

import java.util.List;

public class DetailAdapter extends ArrayAdapter<Detail> {

	private List<Detail> itemList;
	private Context context;
	private Typeface urduFont = null, arialFont = null, arabicFont = null;
	private float textSize = 35f;
	private boolean isDefaultItem = true;
	int viewType;

//	private String htmlLayout = "<!DOCTYPE html><html><head><style>@font-face {font-family: \"arabicFont\"; src: url('file:///android_asset/fonts/arial.ttf')}" +
//                " h1 { font-family:\"arabicFont\"} </style> <title>Hajj-o-Umrah</title></head><body>%s</body></html>";
	
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

	public DetailAdapter(List<Detail> itemList, Context ctx) {
		super(ctx, android.R.layout.simple_list_item_1, itemList);
		urduFont = Typeface.createFromAsset(ctx.getApplicationContext()
				.getAssets(), "Jameel_Noori_Nastaleeq.ttf");
		
		arialFont = Typeface.createFromAsset(ctx.getApplicationContext()
				.getAssets(), "arial.ttf");

		arabicFont = Typeface.createFromAsset(ctx.getApplicationContext()
				.getAssets(), "Trad_Arabic_Bold.ttf");
		
		Log.d("Font Path", "ctx.getApplicationContext().getAssets() :::::::::::: " + ctx.getApplicationContext()
				.getAssets());
		
		this.itemList = itemList;
		this.context = ctx;
		this.viewType = viewType;
	}

	public int getCount() {
		if (itemList != null)
			return itemList.size();
		return 0;
	}

	public Detail getItem(int position) {
		if (itemList != null)
			return itemList.get(position);
		return null;
	}

	public long getItemId(int position) {
		if (itemList != null)
			return itemList.get(position).hashCode();
		return 0;
	}

	static class MyHolder {
        TextView mDetailItem;
    }
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		MyHolder holder = null;
		
		View v = convertView;
		if (v == null ) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.detail_item_list, null);
			
			holder = new MyHolder(); 
			holder.mDetailItem = (TextView) v.findViewById(R.id.detailItem);
		
            v.setTag(holder); 
		}
//		else if(v == null && viewType == 1){
//
//		}
		else {
			holder = (MyHolder)v.getTag(); 
		}
		
		Detail item = itemList.get(position);
		
		if(item != null) {
			if(holder != null && holder.mDetailItem != null) {

				holder.mDetailItem.setPadding(15, 15, 15, 15);
				
				//holder.mDetailItem.setGravity(Gravity.RIGHT);
				//holder.mDetailItem.setSingleLine(false);
				holder.mDetailItem.setTag(item);
				holder.mDetailItem.setTypeface(urduFont);
				
				holder.mDetailItem.setText(Html.fromHtml(item.getDescription()));
				holder.mDetailItem.setTextSize(TypedValue.COMPLEX_UNIT_PX, getTextSize());
				
				if(item.getType() == MainActivity.TYPE_ARABIC) {
					holder.mDetailItem.setTypeface(arabicFont);
					
				} else if(item.getType() == MainActivity.TYPE_LINK) {
					holder.mDetailItem.setAutoLinkMask(Linkify.WEB_URLS);
					Log.d("Type", "Item link type: " + item.getType());
				} else if(item.getType() == MainActivity.TYPE_EMAIL) {
					holder.mDetailItem.setAutoLinkMask(Linkify.EMAIL_ADDRESSES);
					Log.d("Type", "Item email type: " + item.getType());
				}
			}
		}
		
		return v;

	}

	public List<Detail> getItemList() {
		return itemList;
	}

	public void setItemList(List<Detail> itemList) {
		this.itemList = itemList;
	}

}
