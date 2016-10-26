import java.io.*;
import java.security.*;
import java.util.*;
import java.security.spec.*;
import java.math.BigInteger;

public class VerSig {

    public static void main(String[] args) {

        /* Verify a DSA signature */
        String fileName = args[0];
        String sigName = args[1];
        String dataName = args[2];
        String eResult = null;
        String nResult = null;
        //VerSig result = new VerSig();
        try {
            BufferedReader fileIn = new BufferedReader(new FileReader(fileName));

            eResult = fileIn.readLine(); // Reads one line from the file
            nResult = fileIn.readLine(); // Reads one line from the file
            fileIn.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        String sig = readFile(sigName);
        String dataContent = readFile(dataName);


        System.out.println(sigGeneration(sig,eResult,nResult));
        System.out.println(hashString(dataContent));

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

    public static String hashString(String a) {
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
        String result = hexString.toString();
        //int temp = result.length();
        //String result2 = result.substring(0,result.length() - 2);
        System.out.println(result);
        System.out.println(hex2Decimal(result));
        return result;
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

    public static int hex2Decimal(String s) {
        String digits = "0123456789ABCDEF";
        s = s.toUpperCase();
        int val = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int d = digits.indexOf(c);
            val = 16*val + d;
        }
        return val;
    }

    public static BigInteger sigGeneration(String sigName, String e, String n){
        BigInteger sig = new BigInteger(Integer.toString(Integer.parseInt(sigName)));
        BigInteger p = new BigInteger(Integer.toString(Integer.parseInt(e)));
        BigInteger g = new BigInteger(Integer.toString(Integer.parseInt(n)));
        BigInteger result = sig.modPow(p,g);
        return result;
    }

}
