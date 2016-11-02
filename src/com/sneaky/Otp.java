package com.sneaky;

import org.apache.commons.codec.binary.Base32;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.UndeclaredThrowableException;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.util.Date;

/**
 * Created by thomas on 11/2/16.
 */
public class Otp {

    private String account;
    private byte[] key;
    private String crypto;

    Otp(String secret, String account) {
        Base32 base32 = new Base32();
        this.key = base32.decode(secret.replaceAll("\\s+", "").toUpperCase().getBytes());
        this.crypto = "HmacSHA1";
        this.account = account;
    }

    private static long getEpoch() {
        Date current = new Date();
        return current.getTime() / 1000;
    }

    private static byte[] hmacSha(String crypto, byte[] keyBytes, byte[] text) {
        try {
            Mac hmac;
            hmac = Mac.getInstance(crypto);
            SecretKeySpec macKey = new SecretKeySpec(keyBytes, "RAW");
            hmac.init(macKey);
            return hmac.doFinal(text);
        } catch (GeneralSecurityException gse) {
            throw new UndeclaredThrowableException(gse);
        }
    }

    private int generateTOTP() {
        long time = getEpoch() / 30;

        byte[] msg = ByteBuffer.allocate(8).putLong(time).array();
        byte[] hash = hmacSha(crypto, key, msg);
        int offset = hash[hash.length - 1] & 0xf;
        int binary = ((hash[offset] & 0x7f) << 24) | ((hash[offset + 1] & 0xff) << 16) | ((hash[offset + 2] & 0xff) << 8) | (hash[offset + 3] & 0xff);
        int otp = binary % 1000000;

        return otp;
    }

    int getCode() {
        return generateTOTP();
    }

    public long getSecondsLeft() {
        return (30 - (getEpoch() % 30));
    }

    public String getAccount() {
        return account;
    }
}
