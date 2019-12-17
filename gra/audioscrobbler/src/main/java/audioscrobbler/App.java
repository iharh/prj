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

        try {
            Call callArtist = musicInterface.getArtistInfo("artist.getinfo", "Cher", "6c8dc87e402c8f96b8369f927ca0c1be", "json");
            Response<ArtistInfo> responseArtistInfo = callArtist.execute();
            System.out.println("got response: " + responseArtistInfo);
            ArtistInfo artistInfo = responseArtistInfo.body();
            System.out.println("got artistInfo: " + artistInfo);
            final Artist artist = artistInfo.getArtist();
            System.out.println("got artist: " + artist);
            final String artistName = artist.getName();
            System.out.println("got artist name: " + artistName);

            Call callDebug = musicInterface.getArtistInfoForDebug("artist.getinfo","Cher","6c8dc87e402c8f96b8369f927ca0c1be", "json");
            Response<String> responseDebug = callDebug.execute();
            System.out.println("got debug response body: " + responseDebug.body().toString());
        } catch (IOException e) {
            System.out.println("caught IOException: " + e);
        }
    }

    public static void main(String[] args) {
        App app = new App();
        System.out.println(app.getGreeting());
        app.getArtist();
    }
}
