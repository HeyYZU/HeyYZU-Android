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
    private int reservingCount;

    @SerializedName("return")
    @Expose
    private long returnTime;

    @SerializedName("attr")
    private LibraryBookAttribute attribute;

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

    public int getReservingCount() {
        return reservingCount;
    }

    public void setReservingCount(int reservingCount) {
        this.reservingCount = reservingCount;
    }

    public LibraryBookAttribute getAttribute() {
        return attribute;
    }

    public void setAttribute(LibraryBookAttribute attribute) {
        this.attribute = attribute;
    }

    public long getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(long returnTime) {
        this.returnTime = returnTime;
    }
}