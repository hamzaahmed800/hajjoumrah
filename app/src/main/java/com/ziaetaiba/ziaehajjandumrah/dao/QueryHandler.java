package com.ziaetaiba.ziaehajjandumrah.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ziaetaiba.ziaehajjandumrah.models.Book;
import com.ziaetaiba.ziaehajjandumrah.models.Detail;
import com.ziaetaiba.ziaehajjandumrah.models.SubCategory;

import java.util.ArrayList;

public class QueryHandler {

	public static ArrayList<SubCategory> getSubCategoriesByCategoryId(SQLiteDatabase db, int categoryId) {
		
		ArrayList<SubCategory> subCategoryList = new ArrayList<SubCategory>();

		Cursor res = db.rawQuery("select * from sub_category where category_id=" + categoryId, null);
		res.moveToFirst();
		while (res.isAfterLast() == false) {
			
			SubCategory subCategory = new SubCategory();
			subCategory.setId(res.getInt(res.getColumnIndex("id")));
			subCategory.setCategoryId(res.getInt(res.getColumnIndex("category_id")));
			subCategory.setSubCategory(res.getString(res.getColumnIndex("sub_category")));
			subCategory.setParentSubCategoryId(res.getInt(res.getColumnIndex("parent_sub_category_id")));
			subCategory.setChildCategoryAvailable(res.getInt(res.getColumnIndex("child_category_available")));
			
			subCategoryList.add(subCategory);
			
			res.moveToNext();
		}
		
		return subCategoryList;
	}
	
	public static ArrayList<SubCategory> getChildSubCategories(SQLiteDatabase db, int subCategoryId) {
		
		ArrayList<SubCategory> subCategoryList = new ArrayList<SubCategory>();

		Cursor res = db.rawQuery("select * from sub_category where parent_sub_category_id=" + subCategoryId, null);
		res.moveToFirst();
		while (res.isAfterLast() == false) {
			
			SubCategory subCategory = new SubCategory();
			subCategory.setId(res.getInt(res.getColumnIndex("id")));
			subCategory.setCategoryId(res.getInt(res.getColumnIndex("category_id")));
			subCategory.setSubCategory(res.getString(res.getColumnIndex("sub_category")));
			subCategory.setParentSubCategoryId(res.getInt(res.getColumnIndex("parent_sub_category_id")));
			subCategory.setChildCategoryAvailable(res.getInt(res.getColumnIndex("child_category_available")));
			
			subCategoryList.add(subCategory);
			
			res.moveToNext();
		}
		
		return subCategoryList;
	}
	
	public static ArrayList<Detail> getSubCategoryDetail(SQLiteDatabase db, int subCategoryId) {
		
		ArrayList<Detail> detailList = new ArrayList<Detail>();

		Cursor res = db.rawQuery("select * from details where sub_category_id=" + subCategoryId + " order by id asc", null);
		res.moveToFirst();
		while (res.isAfterLast() == false) {
			
			Detail detail = new Detail();
			detail.setId(res.getInt(res.getColumnIndex("id")));
			detail.setDescription(res.getString(res.getColumnIndex("description")));
			detail.setSubCategoryId(res.getInt(res.getColumnIndex("sub_category_id")));
			detail.setType(res.getInt(res.getColumnIndex("type")));
			
			detailList.add(detail);
			
			res.moveToNext();
		}
		
		return detailList;
	}

	public static Book getBookDetails(SQLiteDatabase db, int detailCategorID){

		Book book = new Book();
		Cursor res = db.rawQuery("select * from book_download_link where book_detailcategory_id=" + detailCategorID + " order by id asc", null);
		res.moveToFirst();
		while (res.isAfterLast() == false) {

			book.setId(res.getInt(res.getColumnIndex("id")));
			book.setBook_detailcategory_id(res.getInt(res.getColumnIndex("book_detailcategory_id")));
			book.setBook_name(res.getString(res.getColumnIndex("book_name")));
			book.setBook_imageLink(res.getString(res.getColumnIndex("book_imageLink")));
			book.setBook_downloadLink(res.getString(res.getColumnIndex("book_downloadLink")));

			res.moveToNext();
		}

		return book;
	}
}
