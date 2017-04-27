package tw.bingluen.heyyzu.constant;

public class HeyYZUSecret {
    static {
        System.loadLibrary("HeyYZUSecret");
    }

    public static native String getCipher();
    public static native String getSecret();
}
