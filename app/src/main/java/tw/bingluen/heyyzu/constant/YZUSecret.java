package tw.bingluen.heyyzu.constant;

public class YZUSecret {
    static {
        System.loadLibrary("YZUSecret");
    }

    public static native String getUsername();
    public static native String getPassword();
    public static native String getAppId();
}
