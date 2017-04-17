package tw.bingluen.heyyzu.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccessToken {
    @SerializedName("Token")
    @Expose
    private String token;
    @SerializedName("DeadLine")
    @Expose
    private long expired;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpired() {
        return expired;
    }

    public void setExpired(String expired) {
        this.expired = Long.parseLong(expired, 10);
    }
}
