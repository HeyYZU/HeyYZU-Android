package tw.bingluen.heyyzu.model.library;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LibraryUsersBook extends LibraryBookBase {
    @SerializedName("attr")
    @Expose
    private LibraryBookAttribute attr = null;
    public LibraryBookAttribute getAttr() {
        return attr;
    }
    public void setAttr(LibraryBookAttribute attr) {
        this.attr = attr;
    }
}
