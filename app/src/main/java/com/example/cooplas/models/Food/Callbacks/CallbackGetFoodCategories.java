
package com.example.cooplas.models.Food.Callbacks;

import com.example.cooplas.models.Food.Category;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CallbackGetFoodCategories {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("food_categories")
    @Expose
    private List<Category> food_categories = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Category> getFood_categories() {
        return food_categories;
    }

    public void setFood_categories(List<Category> food_categories) {
        this.food_categories = food_categories;
    }
}
