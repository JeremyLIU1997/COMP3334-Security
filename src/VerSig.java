import java.io.*;
import java.security.*;
import java.util.*;
import java.security.spec.*;
import java.math.BigInteger;

public class VerSig {
    private static final int INIT_A = 0x67452301;
    private static final int INIT_B = (int)0xEFCDAB89L;
    private static final int INIT_C = (int)0x98BADCFEL;
    private static final int INIT_D = 0x10325476;

    private static final int[] SHIFT_AMTS = {
        7, 12, 17, 22,
        5,  9, 14, 20,
        4, 11, 16, 23,
        6, 10, 15, 21
      };

    private static final int[] TABLE_T = new int[64];
      static
      {
        for (int i = 0; i < 64; i++)
          TABLE_T[i] = (int)(long)((1L << 32) * Math.abs(Math.sin(i + 1)));
      }

    public static void main(String[] args) {

        /* Verify a RSA signature */
        String fileName = args[0];
        String sigName = args[1];
        String dataName = args[2];
        String eResult = null;
        String nResult = null;

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

        BigInteger sigResult = sigGeneration(sig,eResult,nResult);
        System.out.println("Signature is: " + sigResult);
        String dataResult = computeMD5(dataContent);
        System.out.println("data is: " + dataResult);
        BigInteger bigInt = new BigInteger(dataResult, 16);
        //BigInteger decResult = hex2Decimal(dataResult);
        //System.out.println("data result is : " + decResult);
        System.out.println("data2 result is : " + bigInt);
        if(sigResult.equals(bigInt)){
            System.out.println("True");
        }else{
            System.out.println("False");
        }

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

    public static String computeMD5(String mInput){
        //StringBuffer hexString = new StringBuffer();
        byte[] message = null;
        try {
            message = mInput.getBytes("UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        int messageLenBytes = message.length;
        int numBlocks = ((messageLenBytes + 8) >>> 6) + 1;
        int totalLen = numBlocks << 6;
        byte[] paddingBytes = new byte[totalLen - messageLenBytes];
        paddingBytes[0] = (byte)0x80;

        long messageLenBits = (long)messageLenBytes << 3;
        for (int i = 0; i < 8; i++)
        {
            paddingBytes[paddingBytes.length - 8 + i] = (byte)messageLenBits;
            messageLenBits >>>= 8;
        }

        int a = INIT_A;
        int b = INIT_B;
        int c = INIT_C;
        int d = INIT_D;
        int[] buffer = new int[16];
        for (int i = 0; i < numBlocks; i ++)
        {
            int index = i << 6;
            for (int j = 0; j < 64; j++, index++)
            buffer[j >>> 2] = ((int)((index < messageLenBytes) ? message[index] : paddingBytes[index - messageLenBytes]) << 24) | (buffer[j >>> 2] >>> 8);
            int originalA = a;
            int originalB = b;
            int originalC = c;
            int originalD = d;
            for (int j = 0; j < 64; j++)
            {
                int div16 = j >>> 4;
                int f = 0;
                int bufferIndex = j;
                switch (div16)
                {
                    case 0:
                    f = (b & c) | (~b & d);
                    break;

                    case 1:
                    f = (b & d) | (c & ~d);
                    bufferIndex = (bufferIndex * 5 + 1) & 0x0F;
                    break;

                    case 2:
                    f = b ^ c ^ d;
                    bufferIndex = (bufferIndex * 3 + 5) & 0x0F;
                    break;

                    case 3:
                    f = c ^ (b | ~d);
                    bufferIndex = (bufferIndex * 7) & 0x0F;
                    break;
                }
                int temp = b + Integer.rotateLeft(a + f + buffer[bufferIndex] + TABLE_T[j], SHIFT_AMTS[(div16 << 2) | (j & 3)]);
                a = d;
                d = c;
                c = b;
                b = temp;
            }

            a += originalA;
            b += originalB;
            c += originalC;
            d += originalD;
        }

        byte[] md5 = new byte[16];
        int count = 0;
        for (int i = 0; i < 4; i++)
        {
            int n = (i == 0) ? a : ((i == 1) ? b : ((i == 2) ? c : d));
            for (int j = 0; j < 4; j++)
            {
                md5[count++] = (byte)n;
                n >>>= 8;
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < md5.length; i++)
        {
            sb.append(String.format("%02X", md5[i] & 0xFF));
        }
        return sb.toString();
    }
    /*
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
        //System.out.println(result);
        //System.out.println(hex2Decimal(result));
        return result;
    }*/

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

    public static BigInteger hex2Decimal(String s) {
        String digits = "0123456789ABCDEF";
        s = s.toUpperCase();
        BigInteger val = new BigInteger("0");
        BigInteger hex = new BigInteger("16");
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            BigInteger d = new BigInteger(Integer.toString(digits.indexOf(c)));
            val = val.multiply(hex);
            val = val.add(d);
        }
        return val;
    }

    public static BigInteger sigGeneration(String sigName, String e, String n){
        BigInteger sig = new BigInteger(sigName);
        BigInteger p = new BigInteger(e);
        BigInteger g = new BigInteger(n);
        BigInteger result = sig.modPow(p,g);
        return result;
    }

}
