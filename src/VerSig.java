import java.io.*;
import java.security.*;
import java.util.*;
import java.security.spec.*;

public class VerSig {

    public static void main(String[] args) {

        /* Verify a DSA signature */
        String fileName = args[0];
        String sigName = args[1];
        String dataName = args[2];
        String en = null;
        String n = null;
        //VerSig result = new VerSig();
        try {
            BufferedReader fileIn = new BufferedReader(new FileReader(fileName));

            en = fileIn.readLine(); // Reads one line from the file
            n = fileIn.readLine(); // Reads one line from the file
            fileIn.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        String sig = readFile(sigName);
        String dataContent = readFile(dataName);
        System.out.println(en);
        System.out.println(n);
        int en1 = Integer.parseInt(en);
        int en2 = Integer.parseInt(n);
        System.out.println(mod(en1, en2));
        hashString(dataContent);
        System.out.println(29);
    }

    public static int mod(int a, int b) {
        //a is the public, b is the mod
        if (a == 0 || b == 0) {
            return 0;
        }
        int b1 = b + 1;
        while (b1 % a != 0) {
            b1 += b;
        }
        int result = b1 / a;
        return result;
    }

    public static void hashString(String a) {
        StringBuffer hexString = new StringBuffer();
        byte[] bytesOfMessage = null;
        try {
            bytesOfMessage = a.getBytes("UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.err.println("I'm sorry, but MD5 is not a valid message digest algorithm");
        }

        byte[] thedigest = md.digest(bytesOfMessage);
        //int temp = thedigest.length;
        for (int i = 0; i < thedigest.length; i++) {
            if ((0xff & thedigest[i]) < 0x10) {
                hexString.append("0"
                        + Integer.toHexString((0xFF & thedigest[i])));
            } else {
                hexString.append(Integer.toHexString(0xFF & thedigest[i]));
            }
        }
        System.out.printf(hexString.toString());
    }

    public static String readFile(String file_path){
        try {
            BufferedReader fileIn = new BufferedReader(new FileReader(file_path));

            String result = fileIn.readLine(); // Reads one line from the file
            fileIn.close();
            return result;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
