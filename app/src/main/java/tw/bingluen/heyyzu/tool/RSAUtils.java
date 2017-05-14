package tw.bingluen.heyyzu.tool;

import android.support.annotation.NonNull;
import android.util.Base64;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;


public class RSAUtils {
    public static RSACipher genEncryptorFromPublicKey(@NonNull tw.bingluen.heyyzu.model.PublicKey publicKey) throws Exception {
        RSAPublicKeySpec spec = new RSAPublicKeySpec(
                new BigInteger(1, Base64.decode(publicKey.getModulus(), Base64.DEFAULT)),
                new BigInteger(1, Base64.decode(publicKey.getExponent(), Base64.DEFAULT))
        );

        KeyFactory factory = KeyFactory.getInstance("RSA");
        PublicKey pubKey = factory.generatePublic(spec);

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return RSACipher.getInstance(cipher);
    }

    public static RSACipher genEncryptorFromPublicKey(@NonNull String publicKey) throws Exception {
        KeyFactory factory = KeyFactory.getInstance("RSA");
        PublicKey pubKey = factory.generatePublic(
                new X509EncodedKeySpec(
                        Base64.decode(publicKey, Base64.DEFAULT)
                )
        );

        Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return RSACipher.getInstance(cipher);
    }
}
