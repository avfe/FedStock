package tech.fedorov.fedstock.fedquotes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Price {
    // FinnHub returns just "brilliant" JSON
    @SerializedName("c")
    @Expose
    public Float c;
    @SerializedName("h")
    @Expose
    public Float h;
    @SerializedName("l")
    @Expose
    public Float l;
    @SerializedName("o")
    @Expose
    public Float o;
    @SerializedName("pc")
    @Expose
    public Float pc;
    @SerializedName("t")
    @Expose
    public Integer t;

    public Float getCurrent() {
        return c;
    }

    public Float getHigher() {
        return h;
    }

    public Float getLower() {
        return l;
    }

    public Float getOpenOfDay() {
        return o;
    }

    public Float getPreviousClose() {
        return pc;
    }

    public Integer getT() {
        return t;
    }
}