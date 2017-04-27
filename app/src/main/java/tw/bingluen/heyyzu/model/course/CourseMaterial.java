package tw.bingluen.heyyzu.model.course;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import tw.bingluen.heyyzu.model.file.Attach;

public class CourseMaterial {

    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("datetime")
    @Expose
    private Integer datetime;
    @SerializedName("attach")
    @Expose
    private Attach attach;
    @SerializedName("website")
    @Expose
    private String website;
    @SerializedName("video")
    @Expose
    private String video;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getDatetime() {
        return datetime;
    }

    public void setDatetime(Integer datetime) {
        this.datetime = datetime;
    }

    public Attach getAttach() {
        return attach;
    }

    public void setAttach(Attach attach) {
        this.attach = attach;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

}
