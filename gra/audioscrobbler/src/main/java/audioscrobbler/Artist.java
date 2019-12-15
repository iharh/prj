package audioscrobbler;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Artist {
    @SerializedName("name")
    @Expose
    private String name;

    /*
    @SerializedName("image")
    @Expose
    private List<Image> image = null;
    */


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*
    public List<Image> getImage() {
        return image;
    }

    public void setImage(List<Image> image) {
        this.image = image;
    }
    */
}
