package tw.bingluen.heyyzu.model.library;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LibraryBookAttribute {

    @SerializedName("dueDate")
    @Expose
    private Integer dueDate;
    @SerializedName("fine")
    @Expose
    private Integer fine;
    @SerializedName("renewable")
    @Expose
    private Boolean renewable;
    @SerializedName("reserved")
    @Expose
    private Boolean reserved;
    @SerializedName("index")
    @Expose
    private String index;
    @SerializedName("reservedCount")
    @Expose
    private Integer reservedCount;
    @SerializedName("order")
    @Expose
    private Integer order;
    @SerializedName("reservedBefore")
    @Expose
    private long reservedBefore;

    @SerializedName("type")
    @Expose
    private String type;

    public Integer getDueDate() {
        return dueDate;
    }

    public void setDueDate(Integer dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getFine() {
        return fine;
    }

    public void setFine(Integer fine) {
        this.fine = fine;
    }

    public Boolean getRenewable() {
        return renewable;
    }

    public void setRenewable(Boolean renewable) {
        this.renewable = renewable;
    }

    public Boolean getReserved() {
        return reserved;
    }

    public void setReserved(Boolean reserved) {
        this.reserved = reserved;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public Integer getReservedCount() {
        return reservedCount;
    }

    public void setReservedCount(Integer reservedCount) {
        this.reservedCount = reservedCount;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public long getReservedBefore() {
        return reservedBefore;
    }

    public void setReservedBefore(long reservedBefore) {
        this.reservedBefore = reservedBefore;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}