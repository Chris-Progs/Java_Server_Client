package servera;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.MessageDigest;
import java.util.*;

public class User {

    private static HashMap<String, String> pw = new HashMap<>();
    private static HashMap<String, String> saltM = new HashMap<>();
    
    // Add new user to HashMaps
    public static void newUser(String usern, String passw) throws NoSuchAlgorithmException {

        byte[] newSalt = getSalt();
        byte[] newHash = generateHash(passw, newSalt);
        pw.put(usern, bytesToStringHex(newHash));
        saltM.put(usern, bytesToStringHex(newSalt));
    }
    // Generate new salt
    public static byte[] getSalt() {

        SecureRandom sRand = new SecureRandom();
        byte[] salt = new byte[16];
        sRand.nextBytes(salt);
        return salt;
    }
    // Returns a byte array after randomizing
    public static byte[] generateHash(String passw, byte[] salt) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(salt);
        byte[] hash = md.digest(passw.getBytes());
        md.reset();
        return hash;
    }
    // Check if the username is a key within the pw HashMap
    public static boolean checkUsername(String username) {
        return pw.containsKey(username);
    }
    // 
    public static boolean checkUser(String usern, String passw) throws NoSuchAlgorithmException {

        String salt = saltM.get(usern);
        byte[] byteSalt = stringHextoBytes(salt);
        String saltedPwHash = bytesToStringHex(generateHash(passw, byteSalt));
        if (saltedPwHash.equals(pw.get(usern))) {
            return true;
        } else {
            return false;
        }
    }
    // Returns a hexidecimal number in string format
    public static String bytesToStringHex(byte[] bytes) {

        StringBuilder stringB = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            stringB.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return stringB.toString();
    }
    // Returns a hexidecimal number in a byte array format
    public static byte[] stringHextoBytes(String hex) {

        byte[] hexBytes = new byte[hex.length() / 2];
        for (int i = 0; i < hexBytes.length; i++) {
            hexBytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return hexBytes;
    }
}
