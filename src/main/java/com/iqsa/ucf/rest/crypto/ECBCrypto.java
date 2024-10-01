package com.iqsa.ucf.rest.crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class ECBCrypto {

    public static void encryptWitEcb(InputStream fis, String filenameEnc, byte[] key) throws IOException,
            NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        try (BufferedInputStream in = new BufferedInputStream(fis);
             FileOutputStream out = new FileOutputStream(filenameEnc);
             BufferedOutputStream bos = new BufferedOutputStream(out)) {
            byte[] ibuf = new byte[1024];
            int len;
            while ((len = in.read(ibuf)) != -1) {
                byte[] obuf = cipher.update(ibuf, 0, len);
                if (obuf != null)
                    bos.write(obuf);
            }
            byte[] obuf = cipher.doFinal();
            if (obuf != null)
                bos.write(obuf);
        }
    }

    public static void decryptWithEcb(String filenameEnc, String filenameDec, byte[] key) throws IOException,
            NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {
        try (FileInputStream in = new FileInputStream(filenameEnc);
             FileOutputStream out = new FileOutputStream(filenameDec)) {
            byte[] ibuf = new byte[1024];
            int len;
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            while ((len = in.read(ibuf)) != -1) {
                byte[] obuf = cipher.update(ibuf, 0, len);
                if (obuf != null)
                    out.write(obuf);
            }
            byte[] obuf = cipher.doFinal();
            if (obuf != null)
                out.write(obuf);
        }
    }
}
