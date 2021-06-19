package tech.fedorov.fedstock.fedquotes;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import tech.fedorov.fedstock.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    TextView tv;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotes);

        // List symbols for RecyclerView
        ArrayList<tech.fedorov.fedstock.fedquotes.Symbol> symbolsList = new ArrayList<>();

        // Create FinnHub listener
        Retrofit finnhubListener = new Retrofit.Builder()
                .baseUrl("https://finnhub.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // Create retrofit service
        FinnhubService service = finnhubListener.create(FinnhubService.class);
        // Create callback command
        Call<List<tech.fedorov.fedstock.fedquotes.Symbol>> symbolsListCall = service.listSymbols(getString(R.string.FinnhubToken), "US");

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.SymbolsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SymbolsListAdapter adapter = new SymbolsListAdapter(this, symbolsList, service);
        recyclerView.setAdapter(adapter);
        // Request to get the list of symbols
        symbolsListCall.enqueue(new Callback<List<tech.fedorov.fedstock.fedquotes.Symbol>>() {
            @Override
            public void onResponse(Call<List<tech.fedorov.fedstock.fedquotes.Symbol>> call,
                                   Response<List<tech.fedorov.fedstock.fedquotes.Symbol>> response) {
                // Getting responded symbols
                List<tech.fedorov.fedstock.fedquotes.Symbol> responseSymbolsList = response.body();

                // Adding responded symbols to RecyclerView symbols list
                for (tech.fedorov.fedstock.fedquotes.Symbol s: responseSymbolsList) {
                    symbolsList.add(new tech.fedorov.fedstock.fedquotes.Symbol(s.getDescription(), s.getSymbol()));
                }

                // Update adapter's information
                adapter.updateStocks(symbolsList);
            }

            @Override
            public void onFailure(Call<List<tech.fedorov.fedstock.fedquotes.Symbol>> call, Throwable t) {
                Log.d("SymbolsListCallDebug", t.getLocalizedMessage());
            }
        });
    }

    /*
        The following methods are needed to save symbolsList to a file in a private memory section.
        It will be implemented in the future.
    */

    private boolean findInArray(String str, String[] strs) {
        for (int i = 0; i < strs.length; i++) {
            if (str.equals(strs[i])) {
                return true;
            }
        }
        return false;
    }

    private void writeObjectToPrivateFile(String filename, Object obj) {
        FileOutputStream fileOut = null;
        try {
            fileOut = getApplicationContext().openFileOutput(filename, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ObjectOutputStream objectOut = null;
        try {
            objectOut = new ObjectOutputStream(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            objectOut.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            objectOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Object readObjectFromPrivateFile(String filename) {
        FileInputStream flis = null;
        try {
            flis = getApplicationContext().openFileInput(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ObjectInputStream objis = null;
        try {
            objis = new ObjectInputStream(flis);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Object object = null;
        try {
            object = objis.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return object;
    }
}