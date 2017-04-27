package tw.bingluen.heyyzu.model.library;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LibraryCollection {

    @SerializedName("branch")
    @Expose
    private String branch;
    @SerializedName("collection")
    @Expose
    private String collection;
    @SerializedName("reservingCount")
    @Expose
    private Integer reservingCount;

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public Integer getReservingCount() {
        return reservingCount;
    }

    public void setReservingCount(Integer reservingCount) {
        this.reservingCount = reservingCount;
    }

}