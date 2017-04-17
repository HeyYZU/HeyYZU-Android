package tw.bingluen.heyyzu.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PublicKey {

    @SerializedName("RSAkey")
    @Expose
    private String rsaKey;
    @SerializedName("Modulus")
    @Expose
    private String modulus;
    @SerializedName("Exponent")
    @Expose
    private String exponent;
    @SerializedName("DeadLine")
    @Expose
    private String deadLine;

    public String getRSAkey() {
        return rsaKey;
    }

    public void setRSAkey(String rsaKey) {
        this.rsaKey = rsaKey;
    }

    public String getModulus() {
        return modulus;
    }

    public void setModulus(String modulus) {
        this.modulus = modulus;
    }

    public String getExponent() {
        return exponent;
    }

    public void setExponent(String exponent) {
        this.exponent = exponent;
    }

    public String getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(String deadLine) {
        this.deadLine = deadLine;
    }

}
