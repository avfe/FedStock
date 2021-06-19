package tech.fedorov.fedstock.fedquotes;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FinnhubService {
    @GET("api/v1/stock/symbol")
    Call<List<tech.fedorov.fedstock.fedquotes.Symbol>> listSymbols(
            @Query("token") String token,
            @Query("exchange") String exchange);

    @GET("api/v1/quote")
    Call<tech.fedorov.fedstock.fedquotes.Price> getPrice(
            @Query("symbol") String symbol,
            @Query("token") String token);
}