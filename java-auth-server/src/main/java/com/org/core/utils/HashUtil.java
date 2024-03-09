package com.org.core.utils;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;
import java.util.regex.Pattern;

public class HashUtil {
    private static final Pattern UUID_REGEX = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    public static boolean isUuid(String value) {
        if (value == null || value.trim().isEmpty())
            return false;
        return UUID_REGEX.matcher(value).matches();
    }

    public static UUID toUuid(String value) {
        try {
            return UUID.fromString(value);
        } catch (Exception e) {
            return null;
        }
    }

    public static UUID toMd5Uuid(String format, Object... args) {
        return toMd5Uuid(String.format(format, args));
    }

    public static UUID toMd5Uuid(String value) {
        if (value == null || value.isEmpty())
            return null;

        try {
            return UUID.fromString(value);
        } catch (Exception e) {

            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] messageDigest = md.digest(value.getBytes());

                // Convert the byte array to a hexadecimal representation
                StringBuilder hexString = new StringBuilder();
                for (byte b : messageDigest) {
                    String hex = Integer.toHexString(0xFF & b);
                    if (hex.length() == 1)
                        hexString.append('0');

                    hexString.append(hex);
                }
                String uuidString = hexString.substring(0, 8) + "-" +
                        hexString.substring(8, 12) + "-" +
                        hexString.substring(12, 16) + "-" +
                        hexString.substring(16, 20) + "-" +
                        hexString.substring(20);
                return UUID.fromString(uuidString);
            } catch (NoSuchAlgorithmException ex) {
                return null;
            }
        }
    }


    public static String toMd5(String format, Object... args) {
        return toMd5(String.format(format, args));
    }

    public static String toMd5(String value) {
        var uuid = toMd5Uuid(value);
        return uuid == null
                ? ""
                : uuid.toString()
                .replace("{", "")
                .replace("}", "")
                .replace("-", "");
    }

    public static String toBase64(String format, Object... args) {
        return toBase64(String.format(format, args));
    }

    public static String toBase64(String bytes) {
        if (bytes == null || bytes.isEmpty())
            return "";
        return Base64.getEncoder().encodeToString(bytes.getBytes());
    }

    public static String fromBase64(String bytes) {
        if (bytes == null || bytes.isEmpty())
            return "";
        return Arrays.toString(Base64.getDecoder().decode(bytes));
    }

    public static String toHex(String format, Object... args) {
        return toHex(String.format(format, args));
    }

    public static String toHex(String bytes) {
        if (bytes == null || bytes.isEmpty())
            return "";
        return Hex.encodeHexString(bytes.getBytes());
    }

    public static String fromHex(String hexValue) {
        if (hexValue == null || hexValue.isEmpty())
            return "";
        try {
            return new String(Hex.decodeHex(hexValue));
        } catch (DecoderException e) {
            return "";
        }
    }

}
