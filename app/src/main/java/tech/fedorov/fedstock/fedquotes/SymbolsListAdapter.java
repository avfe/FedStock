package tech.fedorov.fedstock.fedquotes;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import tech.fedorov.fedstock.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SymbolsListAdapter extends RecyclerView.Adapter<SymbolsListAdapter.ViewHolder>{

    private ArrayList<tech.fedorov.fedstock.fedquotes.Symbol> symbolsList;

    private LayoutInflater mInflater;

    private tech.fedorov.fedstock.fedquotes.FinnhubService service;

    // data is passed into the constructor
    SymbolsListAdapter(Context context, ArrayList<tech.fedorov.fedstock.fedquotes.Symbol> data,
                       tech.fedorov.fedstock.fedquotes.FinnhubService service) {
        this.mInflater = LayoutInflater.from(context);
        this.symbolsList = data;
        this.service = service;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.symbol_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        tech.fedorov.fedstock.fedquotes.Symbol currentSymbol = symbolsList.get(position);
        String currentSymbolTicket = currentSymbol.getSymbol();
        String currentSymbolDescription = currentSymbol.getDescription();
        holder.symbol.setText(currentSymbolTicket);
        holder.description.setText(currentSymbolDescription);

        if (symbolsList.get(position).price < 0) {
            // Display query
            holder.price.setText("...");
            // Create callback command to get price of symbol
            Call<tech.fedorov.fedstock.fedquotes.Price> priceCall = service.getPrice(currentSymbol.getSymbol(),"c1gdjqn48v6p69n8t8og");
            // Request to get price of symbol
            priceCall.enqueue(new Callback<tech.fedorov.fedstock.fedquotes.Price>() {
                @Override
                public void onResponse(Call<tech.fedorov.fedstock.fedquotes.Price> call,
                                       Response<tech.fedorov.fedstock.fedquotes.Price> response) {
                    // Getting a price
                    tech.fedorov.fedstock.fedquotes.Price price =  response.body();
                    if (price != null) {
                        symbolsList.get(position).setPrice(price.getCurrent());
                        holder.price.setText(price.getCurrent() + " $");
                    } else {
                        Log.d("Price", "NullPrice");
                        holder.price.setText("Failed");
                    }
                }

                @Override
                public void onFailure(Call<tech.fedorov.fedstock.fedquotes.Price> call, Throwable t) {
                    Log.d("PriceCallDebug", t.getLocalizedMessage());
                    holder.price.setText("Failed");
                }

            });
        }
    }

    public void updateStocks(ArrayList<tech.fedorov.fedstock.fedquotes.Symbol> symbolsList) {
        this.symbolsList = symbolsList;
        notifyDataSetChanged();
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return symbolsList.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView symbol;
        TextView description;
        TextView price;

        ViewHolder(View itemView) {
            super(itemView);
            symbol = itemView.findViewById(R.id.symbol);
            description = itemView.findViewById(R.id.description);
            price = itemView.findViewById(R.id.price);
        }
    }
}