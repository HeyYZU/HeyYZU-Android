package tw.bingluen.heyyzu.model.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Curriculum {

    @SerializedName("lesson_id")
    @Expose
    private String lessonId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("timeSlots")
    @Expose
    private List<TimeSlot> timeSlots = null;

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(List<TimeSlot> timeSlots) {
        this.timeSlots = timeSlots;
    }

}