package tw.bingluen.heyyzu.model.library;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LibraryBook extends LibraryBookBase {

    @SerializedName("publisher")
    @Expose
    private String publisher;
    @SerializedName("year")
    @Expose
    private Integer year;
    @SerializedName("index")
    @Expose
    private String index;
    @SerializedName("isbn")
    @Expose
    private long isbn;
    @SerializedName("cover")
    @Expose
    private String cover;
    @SerializedName("collections")
    @Expose
    private List<LibraryCollection> collections = null;

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public long getIsbn() {
        return isbn;
    }

    public void setIsbn(long isbn) {
        this.isbn = isbn;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public List<LibraryCollection> getCollections() {
        return collections;
    }

    public void setCollections(List<LibraryCollection> collections) {
        this.collections = collections;
    }

}
