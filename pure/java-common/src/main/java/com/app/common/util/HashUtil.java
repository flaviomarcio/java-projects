package com.app.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

public class HashUtil {
    private static final Pattern UUID_REGEX = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    public static String formatStringToMd5(StringBuilder value) {
        return formatStringToMd5(value.toString());
    }


    public static String formatStringToMd5(String valueIn) {
        if (valueIn != null && !valueIn.trim().isEmpty()){
            AtomicReference<String> outValue = new AtomicReference<>(valueIn
                    .trim()
                    .replace("-", "")
                    .replace("{", "")
                    .replace("}", ""));
            if (outValue.get().length() != 32)
                throw new RuntimeException(String.format("Invalid length %s", valueIn));

            return outValue.get().substring(0, 8) + "-" +
                    outValue.get().substring(8, 12) + "-" +
                    outValue.get().substring(12, 16) + "-" +
                    outValue.get().substring(16, 20) + "-" +
                    outValue.get().substring(20);
        }
        throw new RuntimeException(String.format("Invalid value %s", valueIn));
    }

    public static boolean isUuid(String value) {
        if (value != null && !value.trim().isEmpty()) {
            try {
                return UUID_REGEX.matcher(formatStringToMd5(value)).matches();
            } catch (Exception ignore) {}
        }
        return false;
    }

    public static UUID toUuid(String value) {
        try {
            return UUID.fromString(formatStringToMd5(value));
        } catch (Exception ignore) {}
        return null;
    }

    public static UUID toMd5Uuid(InputStream value) {
        try {
            return toMd5Uuid(new String(value.readAllBytes()));
        } catch (IOException ignore) {}
        return null;
    }

    public static UUID toMd5Uuid(Object o) {
        return (o != null)
                ?toMd5Uuid(ObjectUtil.toString(o))
                :null;
    }

    public static UUID toMd5Uuid(String format, Object... args) {
        return toMd5Uuid(String.format(format, args));
    }

    public static UUID toMd5Uuid(String value) {
        if (value != null && !value.isEmpty()) {
            try {
                return UUID.fromString(formatStringToMd5(value));
            } catch (Exception ignore) {}
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
                return UUID.fromString(formatStringToMd5(hexString));
            } catch (NoSuchAlgorithmException ignore) {}
        }
        return null;
    }


    public static String toMd5(String format, Object... args) {
        return HashUtil.toMd5(String.format(format, args));
    }

    public static String toMd5(Object o) {
        return HashUtil.toMd5(ObjectUtil.toString(o));
    }

    public static String toMd5(InputStream value) {
        if(value==null)
            return null;
        UUID uuid = toMd5Uuid(value);
        if (uuid != null){
            return uuid.toString()
                    .replace("{", "")
                    .replace("}", "")
                    .replace("-", "");
        }
        return "";
    }

    public static String toMd5(String value) {
        UUID uuid = HashUtil.toMd5Uuid(value);
        if(uuid != null){
            return
                    uuid.toString()
                            .replace("{", "")
                            .replace("}", "")
                            .replace("-", "");
        }
        return "";
    }

    public static String toBase64(String format, Object... args) {
        return HashUtil.toBase64(String.format(format, args));
    }

    public static String toBase64(String bytes) {
        if (bytes != null && !bytes.isEmpty())
            return Base64.getEncoder().encodeToString(bytes.getBytes());
        return "";
    }

    public static String fromBase64(String bytes) {
        if (bytes != null && !bytes.isEmpty())
            return Arrays.toString(Base64.getDecoder().decode(bytes));
        return "";
    }

    public static String toHex(String format, Object... args) {
        return HashUtil.toHex(String.format(format, args));
    }

    public static String toHex(String value) {
        if (value != null && !value.isEmpty()) {
            StringBuilder hexStringBuilder = new StringBuilder();
            try (Formatter formatter = new Formatter(hexStringBuilder, Locale.US)) {
                for (byte b : value.getBytes())
                    formatter.format("%02x", b);
                return hexStringBuilder.toString();
            }
        }
        return "";
    }

    public static String fromHex(String hexEncoded) {
        if(hexEncoded!=null){
            byte[] decodedBytes = new byte[hexEncoded.length() / 2];
            for (int i = 0; i < decodedBytes.length; i++) {
                decodedBytes[i] = (byte) Integer.parseInt(hexEncoded.substring(i * 2, i * 2 + 2), 16);
            }
            return new String(decodedBytes, StandardCharsets.UTF_8);
        }
        return "";
    }

}
