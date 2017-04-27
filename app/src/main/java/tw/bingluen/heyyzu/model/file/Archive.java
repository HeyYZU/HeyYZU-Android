package tw.bingluen.heyyzu.model.file;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by zhuangbinglun on 2017/4/17.
 */

public class Archive extends Attach {
    @SerializedName("datetime")
    @Expose
    private Integer datetime;

    public Integer getDatetime() {
        return datetime;
    }

    public void setDatetime(Integer datetime) {
        this.datetime = datetime;
    }
}
