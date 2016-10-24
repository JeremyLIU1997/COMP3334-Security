import java.io.*;
import java.security.*;
import java.security.spec.*;

public class VerSig {

    public static void main(String[] args) {

        /* Verify a DSA signature */
        int ver = 3;
        if (ver == 3) {
            //VerSig result = new VerSig();
            System.out.println(mod(35,78));
        }
    }

    public static int mod(int a, int b){
        //a is the public, b is the mod
        if(a == 0 || b == 0){
                return 0;
        }
        int b1 = b + 1;
        while(b1%a != 0){
            b1 += b;
        }
        int result = b1/a;
        return result;
    }

}
