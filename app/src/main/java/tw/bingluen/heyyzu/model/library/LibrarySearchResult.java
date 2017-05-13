package tw.bingluen.heyyzu.model.library;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LibrarySearchResult implements SearchSuggestion {
    @SerializedName("bibliosno")
    @Expose
    private Long id;
    @SerializedName("bktitle")
    @Expose
    private String title;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("callno")
    @Expose
    private String index;
    @SerializedName("Cover")
    @Expose
    private String cover;

    public LibrarySearchResult(Parcel source) {
        this.title = source.readString();
    }

    public Long getId() {
        return id;
    }

    public void setId(String id) {
        this.id = Long.parseLong(id);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String bktitle) {
        this.title = bktitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    @Override
    public String getBody() {
        return getTitle();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
    }

    public static final Creator<LibrarySearchResult> CREATOR = new Creator<LibrarySearchResult>() {
        @Override
        public LibrarySearchResult createFromParcel(Parcel in) {
            return new LibrarySearchResult(in);
        }

        @Override
        public LibrarySearchResult[] newArray(int size) {
            return new LibrarySearchResult[size];
        }
    };
}
