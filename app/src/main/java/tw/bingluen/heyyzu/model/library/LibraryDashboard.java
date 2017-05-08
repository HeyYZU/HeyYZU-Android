package tw.bingluen.heyyzu.model.library;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LibraryDashboard {

    @SerializedName("reading")
    @Expose
    private LeastTransactionRecord reading;
    @SerializedName("reserving")
    @Expose
    private LeastTransactionRecord reserving;
    @SerializedName("favorite")
    @Expose
    private LeastTransactionRecord favorite;

    public void setFavorite(LeastTransactionRecord favorite) {
        this.favorite = favorite;
    }

    public void setReserving(LeastTransactionRecord reserving) {
        this.reserving = reserving;
    }

    public void setReading(LeastTransactionRecord reading) {
        this.reading = reading;
    }

    public LeastTransactionRecord getFavorite() {
        return favorite;
    }

    public LeastTransactionRecord getReserving() {
        return reserving;
    }

    public LeastTransactionRecord getReading() {
        return reading;
    }

    public class LeastTransactionRecord {
        @SerializedName("count")
        @Expose
        private int total;
        @SerializedName("leastFive")
        @Expose
        private List<LibraryUsersBook> leastFive;

        public void setTotal(int total) {
            this.total = total;
        }

        public void setLeastFive(List<LibraryUsersBook> leastFive) {
            this.leastFive = leastFive;
        }

        public int getTotal() {
            return total;
        }

        public List<LibraryUsersBook> getLeastFive() {
            return leastFive;
        }
    }
}
