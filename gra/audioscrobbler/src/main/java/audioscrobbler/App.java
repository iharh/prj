package audioscrobbler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.IOException;

public class App {
    public static final String BASE_URL = "http://ws.audioscrobbler.com";

    public static Retrofit retrofit;

    public static Retrofit getRetrofitClient(){
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        }
        return retrofit;
    }
    
    // used by UT
    public String getGreeting() {
        return "Hello world.";
    }

    private void getArtist() {
        Retrofit retrofit = getRetrofitClient();
        MusicInterface musicInterface = retrofit.create(MusicInterface.class);

        Call call = musicInterface.getArtistString("artist.getinfo","Cher","6c8dc87e402c8f96b8369f927ca0c1be", "json");

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println("Response: " + response.body().toString());
                //Toast.makeText()
                /*
                if (response.isSuccessful()){
                    if (response.body() != null){
                        Log.i("onSuccess", response.body().toString());
                    }else{
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
                */
            }
            @Override
            public void onFailure(Call call, Throwable t) {
                System.out.println("onFailure !!!");
            }
        });
    }

    public static void main(String[] args) {
        App app = new App();
        System.out.println(app.getGreeting());
        app.getArtist();
    }
}
