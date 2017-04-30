package tw.bingluen.heyyzu.model.course;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import tw.bingluen.heyyzu.model.file.Archive;
import tw.bingluen.heyyzu.model.file.Attach;

public class CourseHomework {

    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("datetime")
    @Expose
    private long datetime;
    @SerializedName("deadline")
    @Expose
    private long deadline;
    @SerializedName("group")
    @Expose
    private Boolean group;
    @SerializedName("optional")
    @Expose
    private Boolean optional;
    @SerializedName("attach")
    @Expose
    private Attach attach;
    @SerializedName("grades")
    @Expose
    private Integer grades;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("archive")
    @Expose
    private List<Archive> archive = null;

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

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    public long getDeadline() {
        return deadline;
    }

    public void setDeadline(long deadline) {
        this.deadline = deadline;
    }

    public Boolean getGroup() {
        return group;
    }

    public void setGroup(Boolean group) {
        this.group = group;
    }

    public Boolean getOptional() {
        return optional;
    }

    public void setOptional(Boolean optional) {
        this.optional = optional;
    }

    public Attach getAttach() {
        return attach;
    }

    public void setAttach(Attach attach) {
        this.attach = attach;
    }

    public Integer getGrades() {
        return grades;
    }

    public void setGrades(Integer grades) {
        this.grades = grades;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<Archive> getArchive() {
        return archive;
    }

    public void setArchive(List<Archive> archive) {
        this.archive = archive;
    }

}