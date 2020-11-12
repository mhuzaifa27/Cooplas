
package com.example.cooplas.models.Food.Callbacks;

import com.example.cooplas.models.Food.ForYou;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CallbackGetFoodForYou {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("for_you")
    @Expose
    private List<ForYou> for_you = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<ForYou> getFor_you() {
        return for_you;
    }

    public void setFor_you(List<ForYou> for_you) {
        this.for_you = for_you;
    }
}
