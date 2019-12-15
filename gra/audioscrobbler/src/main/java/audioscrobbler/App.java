package audioscrobbler;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App {
    public static final String BASE_URL = "http://ws.audioscrobbler.com";

    public static Retrofit retrofit;

    public static Retrofit getRetrofitClient(){
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        }
        return retrofit;
    }
    
    // used by UT
    public String getGreeting() {
        return "Hello world.";
    }

    public static void main(String[] args) {
        System.out.println(new App().getGreeting());
    }
}
