package tw.bingluen.heyyzu.model.course;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import tw.bingluen.heyyzu.model.file.Attach;

public class CourseAnnouncement {

    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("datetime")
    @Expose
    private Integer datetime;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("attach")
    @Expose
    private Attach attach;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getDatetime() {
        return datetime;
    }

    public void setDatetime(Integer datetime) {
        this.datetime = datetime;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Attach getAttach() {
        return attach;
    }

    public void setAttach(Attach attach) {
        this.attach = attach;
    }

}
