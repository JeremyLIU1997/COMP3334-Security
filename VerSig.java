import java.io.*;
import java.security.*;
import java.security.spec.*;

class VerSig {

    public static void main() {

        /* Verify a DSA signature */
        int ver = 3;
        if (ver == 3) {
            System.out.println(mod(5,5963));
        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }
    }

    public int mod(int a, int b){
        //a is the public, b is the mod
        if(!a || !b){
                return;
        }
        int b1 = b + 1;
        while(b%a != 0){
            b1 += b;
        }

        result result = b1/a;
    }

}
