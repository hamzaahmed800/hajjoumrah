package com.ziaetaiba.ziaehajjandumrah.models;

public class SubCategory {

	private int id;
	private int categoryId;
	private int parentSubCategoryId;
	private int childCategoryAvailable;
	private String subCategory;
	
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the categoryId
	 */
	public int getCategoryId() {
		return categoryId;
	}
	/**
	 * @param categoryId the categoryId to set
	 */
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	/**
	 * @return the parentSubCategoryId
	 */
	public int getParentSubCategoryId() {
		return parentSubCategoryId;
	}
	/**
	 * @param parentSubCategoryId the parentSubCategoryId to set
	 */
	public void setParentSubCategoryId(int parentSubCategoryId) {
		this.parentSubCategoryId = parentSubCategoryId;
	}

	/**
	 * @return the childCategoryAvailable
	 */
	public int getChildCategoryAvailable() {
		return childCategoryAvailable;
	}
	/**
	 * @param childCategoryAvailable the childCategoryAvailable to set
	 */
	public void setChildCategoryAvailable(int childCategoryAvailable) {
		this.childCategoryAvailable = childCategoryAvailable;
	}

	/**
	 * @return the subCategory
	 */
	public String getSubCategory() {
		return subCategory;
	}
	/**
	 * @param subCategory the subCategory to set
	 */
	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}
	 
	
}
