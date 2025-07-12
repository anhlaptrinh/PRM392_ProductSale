package com.example.productsaleprm.model.response;

import com.example.productsaleprm.model.WishList;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WishListResponse {
    private int totalItems;
    private boolean isLast;
    private int totalPages;
    private int page;
    @SerializedName("wishListItem")
    private List<WishList> wishListItem;

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<WishList> getWishListItem() {
        return wishListItem;
    }

    public void setWishListItem(List<WishList> wishListItem) {
        this.wishListItem = wishListItem;
    }
}
