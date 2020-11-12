
package com.example.cooplas.models.Food.Callbacks;

import com.example.cooplas.models.Food.Cart;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CallbackGetCart {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("cart")
    @Expose
    private Cart cart;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

}
