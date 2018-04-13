package com.ziaetaiba.ziaehajjandumrah;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.ziaetaiba.hajjandumrah.R;
import com.ziaetaiba.ziaehajjandumrah.dao.DBHelper;
import com.ziaetaiba.ziaehajjandumrah.dao.QueryHandler;
import com.ziaetaiba.ziaehajjandumrah.models.SubCategory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SubCategoryActivity extends Activity {

	private SQLiteDatabase db = null;

	private ListView subCategoryListView = null;
	private SubCategoryAdapter adptSubCategory;

	private ScaleGestureDetector scaleGestureDetector;

	private TextView headingText;
	private Typeface headingFont = null;
	List<SubCategory> subCategoryList,NewList;
	int categoryId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sub_category);

		headingText = (TextView)findViewById(R.id.heading);

		headingFont = Typeface.createFromAsset(this.getAssets(), "Aslam.ttf");
		headingText.setTypeface(headingFont);
		subCategoryListView = (ListView) findViewById(R.id.subCategoryList);

		if(MainActivity.newList != null){
			setSubCategoryList();
			adptSubCategory.setItemList(MainActivity.newList);
		}



		try {
			db = new DBHelper(this).getWritableDatabase(); //
		} catch (IOException e) {
			e.printStackTrace();
		}

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			categoryId = extras.getInt("categoryId");
		    Log.e("categoryId",String.valueOf(categoryId));
		    int subCategoryId = extras.getInt("subCategoryId");
			Log.e("SubcategoryId",String.valueOf(subCategoryId));
		    if(categoryId > 0) {
		    	 Log.d("CLICKED", "CLICKED! Populate categoryId " + categoryId);
		    	 setSubCategoryList();
		    	 populateSubCategories(categoryId);
		    } else if(subCategoryId > 0) {
		    	Log.d("CLICKED", "CLICKED! Populate subCategoryId " + subCategoryId);
		    	String subCategory = extras.getString("subCategory");
		    	setSubCategoryList();
		    	populateChildSubCategories(subCategoryId, subCategory);
		    }

		}

		scaleGestureDetector = new ScaleGestureDetector(this,
				new simpleOnScaleGestureListener());

		ActionBar actionBar = getActionBar();
        //for color

		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(MainActivity.TOP_BAR_COLOR)));
        actionBar.setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		getMenuInflater().inflate(R.menu.main,menu);
		getMenuInflater().inflate(R.menu.search, menu);
		MenuItem item = (menu).findItem(R.id.search_item);
		SearchView searchView = (SearchView) item.getActionView();
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String s) {
				if(s.equals("")){
					populateSubCategories(categoryId);
				}else{

					SearchItem(s);
				}
				return false;
			}

			@Override
			public boolean onQueryTextChange(String s) {
				Log.e("URDU STRING",s);
				if(s.equals("")){
					populateSubCategories(categoryId);
				}
				return false;
			}
		});
		return true;
	}
	//Searching in Subcatogory
	private void SearchItem(String s) {
		NewList = new ArrayList<SubCategory>();
		for(int i=0;i<subCategoryList.size();i++){
			if(subCategoryList.get(i).getSubCategory().contains(s)){
				NewList.add(subCategoryList.get(i));
			}
		}
		adptSubCategory.setItemList(NewList);
		adptSubCategory.notifyDataSetChanged();
	}

	@Override
	public void onBackPressed() {

		Log.d("Back", "Phone back pressed.");
		super.onBackPressed();
		SubCategoryActivity.this.finish();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menu) {

		switch (menu.getItemId()) {
		case android.R.id.home:
			Log.d("OPTION_CLICKED", "Top app icon clicked to go back");
			SubCategoryActivity.this.finish();
			return true;
			case R.id.action_settings:
				Intent  intent = new Intent(Intent.ACTION_VIEW,
						Uri.parse("https://play.google.com/store/apps/dev?id=7848049246569116556"));
				startActivity(intent);
			return true;


			default:
			return super.onOptionsItemSelected(menu);
		}
	}



//	public OnClickListener mOnTitleClickListener = new OnClickListener() {
//        @Override
//        public void onClick(View v) {
////            final int position = itemList.getPositionForView((View) v.getParent());
//        	if(v.getTag() != null) {
//    			SubCategory objSubCategory = (SubCategory)v.getTag();
//    			
//    			if(objSubCategory.getChildCategoryAvailable() > 0) {
//    				Log.d("Load Child Sub Category item: ", "" + objSubCategory.getSubCategory());
//    				populateChildSubCategories(objSubCategory.getId());
//    			} else {
//    				Log.d("Load Sub Category Detail: ", "" + objSubCategory.getSubCategory());
//    				startDetailActivity(objSubCategory.getId());
//    			}	
//    		}
//            Log.d("Click Event", "Title clicked, row " + v.getTag());
//        }
//    };

	private void populateSubCategories(int categoryId) {

		 subCategoryList = QueryHandler.getSubCategoriesByCategoryId(this.db, categoryId);

		// setContentView(R.layout.sub_category_item);

		headingText.setText(MainActivity.headings.get(categoryId));

		adptSubCategory.setItemList(subCategoryList);
		//adptSubCategory.notifyDataSetChanged();

		Log.d("SubCategory List", "SubCategory List Size: " + subCategoryList.size());

	}

	private void populateChildSubCategories(int subCategoryId, String subCategory) {

		List<SubCategory> subCategoryList = QueryHandler.getChildSubCategories(
				this.db, subCategoryId);

		// setContentView(R.layout.sub_category_item);

		headingText.setText(subCategory);

		adptSubCategory.setItemList(subCategoryList);



		Log.d("SubCategory List", "Child SubCategory List Size: " + subCategoryList.size());

	}

	private void setSubCategoryList() {

//    	setContentView(R.layout.sub_category_item);

    	//subCategoryListView = (ListView) findViewById(R.id.subCategoryList);
//    	List<SubCategory> list = QueryHandler.getSubCategoriesByCategoryId(this.db,categoryId);
//    	for(int i=0;i<list.size();i++){
//    		Log.e("Index"+i+,list.get(i)/)
//		}
		adptSubCategory = new SubCategoryAdapter(new ArrayList<SubCategory>(), this);
		Log.d("adptSubCategory", "adptSubCategory :::::::::::::::::::::: " + adptSubCategory + " : " + subCategoryListView);
		subCategoryListView.setAdapter(adptSubCategory);

		subCategoryListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				Log.d("List Item position", "" + position);
				SubCategory objSubCategory = SubCategoryActivity.this.adptSubCategory.getItem(position);
				Log.d("List Sub Category item:", "" + objSubCategory.getSubCategory());

				if(objSubCategory.getChildCategoryAvailable() > 0) {
					Log.d("Child Sub Category:", "" + objSubCategory.getSubCategory());
					//populateChildSubCategories(objSubCategory.getId());
					startSubCategoryActivity(objSubCategory.getId(), objSubCategory.getSubCategory());
				} else {
					Log.d("Sub Category Detail: ", "" + objSubCategory.getSubCategory());
					startDetailActivity(objSubCategory.getId(), objSubCategory.getSubCategory());
//					loadDetailPage(objSubCategory.getId());
				}

			}

		});


		subCategoryListView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				int pointers = MotionEventCompat.getPointerCount(event);
				Log.d("TOUCH", "UPPER ON TOUCH CALLED:::" + pointers);
				if (pointers > 0) {
					scaleGestureDetector.onTouchEvent(event);
				}
				return false;
			}
		});

	}

	// Start activity itself for child of sub category.
	private void startSubCategoryActivity(int subCategoryId, String subCategory) {
		Intent intent = new Intent(this, SubCategoryActivity.class);
		intent.putExtra("subCategoryId", subCategoryId);
		intent.putExtra("subCategory", subCategory);

		startActivity(intent);
	}

	private void startDetailActivity(int subCategoryId, String subCategory) {
    	 Intent intent = new Intent(this, DetailActivity.class);
    	 intent.putExtra("subCategoryId", subCategoryId);
    	 intent.putExtra("subCategory", subCategory);
         startActivity(intent);
    }

	private class simpleOnScaleGestureListener extends
	SimpleOnScaleGestureListener {
		float size;
		float product;
		float factor;
//		final static float MIN_PRODUCT_VALUE = 30.0f;
//		final static float MAX_PRODUCT_VALUE = 80.0f;

		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			// TODO Auto-generated method stub
			// float size = scaleGesture.getTextSize();
			// float size = left.getTextSize();

			// float size = scaleGesture.getTextSize();
			//
			size = ((TextView) ((RelativeLayout) ((RelativeLayout) (subCategoryListView
					.getChildAt(0))).getChildAt(0)).getChildAt(0))
					.getTextSize();
			Log.d("TextSizeStart", "TextSize! size start: " + String.valueOf(size));
			//
			factor = detector.getScaleFactor();
			Log.d("Factor", "TextSize! Factor" + String.valueOf(factor));
			//
			product = size * factor;
			adptSubCategory.setTextSize(product);
			// scaleAyaat(product);
			Log.d("TextSize", String.valueOf(product * 10));
		//	if (product <= MAX_PRODUCT_VALUE && product >= MIN_PRODUCT_VALUE) {
		//		adptDetail.notifyDataSetChanged();
		//	}

			adptSubCategory.notifyDataSetChanged();

			size = ((TextView) ((RelativeLayout) ((RelativeLayout) (subCategoryListView
					.getChildAt(0))).getChildAt(0)).getChildAt(0))
					.getTextSize();
			Log.d("TextSizeEnd", "TextSize! size end: " + String.valueOf(size));

			return true;
		}

	}


}
