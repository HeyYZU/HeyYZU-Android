package tw.bingluen.heyyzu.tool;

import android.util.Base64;

import javax.crypto.Cipher;

public class RSACipher {
    private Cipher cipher;
    protected RSACipher(Cipher cipher) {
        this.cipher = cipher;
    }

    public String getEncryptTextOnBase64(String plain) throws Exception {
        return new String(Base64.encode(cipher.doFinal(plain.getBytes("UTF-8")), Base64.NO_WRAP));
    }

    public static RSACipher getInstance(Cipher cipher) {
        return new RSACipher(cipher);
    }
}
