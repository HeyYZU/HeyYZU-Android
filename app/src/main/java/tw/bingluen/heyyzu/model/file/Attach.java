package tw.bingluen.heyyzu.model.file;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Attach {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("filename")
    @Expose
    private String filename;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

}