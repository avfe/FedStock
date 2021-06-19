package tech.fedorov.fedstock.fedquotes;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Symbol {

    @SerializedName("currency")
    @Expose
    public String currency;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("displaySymbol")
    @Expose
    public String displaySymbol;
    @SerializedName("figi")
    @Expose
    public String figi;
    @SerializedName("mic")
    @Expose
    public String mic;
    @SerializedName("symbol")
    @Expose
    public String symbol;
    @SerializedName("type")
    @Expose
    public String type;

    public Symbol(String description, String symbol) {
        this.description = description;
        this.symbol = symbol;
    }

    public String getDescription() {
        return description;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public float price = -1f;

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Symbol{" +
                "currency='" + currency + '\'' +
                ", description='" + description + '\'' +
                ", displaySymbol='" + displaySymbol + '\'' +
                ", figi='" + figi + '\'' +
                ", mic='" + mic + '\'' +
                ", symbol='" + symbol + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}