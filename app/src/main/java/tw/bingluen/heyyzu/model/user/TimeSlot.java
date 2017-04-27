package tw.bingluen.heyyzu.model.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimeSlot {

    @SerializedName("day")
    @Expose
    private Integer day;
    @SerializedName("slot")
    @Expose
    private Integer slot;
    @SerializedName("room")
    @Expose
    private String room;

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getSlot() {
        return slot;
    }

    public void setSlot(Integer slot) {
        this.slot = slot;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

}