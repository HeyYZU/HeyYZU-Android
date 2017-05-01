package tw.bingluen.heyyzu.model.file;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by zhuangbinglun on 2017/4/17.
 */

public class Archive extends Attach {
    @SerializedName("datetime")
    @Expose
    private long datetime;

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }
}
