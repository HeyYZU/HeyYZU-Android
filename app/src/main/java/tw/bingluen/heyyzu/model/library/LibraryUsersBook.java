package tw.bingluen.heyyzu.model.library;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LibraryUsersBook extends LibraryBookBase {
    @SerializedName("attr")
    @Expose
    private List<LibraryBookAttribute> attr = null;
    public List<LibraryBookAttribute> getAttr() {
        return attr;
    }
    public void setAttr(List<LibraryBookAttribute> attr) {
        this.attr = attr;
    }
}
