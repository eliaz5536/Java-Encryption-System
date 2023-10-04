package Main;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class eos6_task4A {

    /**
     * Initializes Task 4A that overviews the
     * 'insecurity' of IND CCA of Naive RSA Encryption
     * System
     */
    public void IND_CCA_security() {
        // * Completed
        Scanner inputN = new Scanner(System.in);
        System.out.println("Please enter the public parameter N: ");
        int N = inputN.nextInt();
        System.out.println(N);

        ArrayList<Integer> factorsOfN = new ArrayList<>();
        factorsOfN = factors(N, factorsOfN);

        int[] twoPrimeNumbers = findTwoPrimeNumbers(N, factorsOfN);

        for(int i = 0; i < twoPrimeNumbers.length; i++) {
            System.out.println("Two Prime Numbers: " + twoPrimeNumbers[i]);
        }

        int p = twoPrimeNumbers[0];
        int q = twoPrimeNumbers[1];

        int M = phiOfN(N, p, q);

        int e = encryption(N, M);

        System.out.println("---------------------------------");
        Scanner inputCipher = new Scanner(System.in);
        System.out.println("Please enter the ciphertext c: ");
        // String inputC = inputCipher.nextLine();
        String cipher = inputCipher.nextLine();
        System.out.println(cipher);
        System.out.println("---------------------------------");

        ArrayList<Integer> unicodeInt = extractingCiphertext(cipher, N, e);

        // c' = 2^e * c (mod N)
        BigInteger TWO;
        ArrayList<BigInteger> modifiedCiphertext = new ArrayList<BigInteger>();
        BigInteger encryption = BigInteger.valueOf(e);
        TWO = new BigInteger("2");
        for(int i = 0; i < unicodeInt.size(); i++) {
            BigInteger cipherText  = BigInteger.valueOf(unicodeInt.get(i));
            BigInteger twoPower = TWO.pow(encryption.intValue());
            BigInteger modifiedCipher =  twoPower.multiply(cipherText);
            modifiedCiphertext.add(modifiedCipher);
        }

        System.out.println("--------------------------------");
        System.out.println("The modified ciphertext c' is = ");
        for(int i = 0; i < modifiedCiphertext.size(); i++) {
            System.out.println(modifiedCiphertext.get(i));
        }

        System.out.println(" ");
        // Inverse calculation:
        BigInteger N_BigInteger = BigInteger.valueOf(N);
        BigInteger twoInverse = TWO.modInverse(N_BigInteger);
        System.out.println("The inverse of 2 mod " + N + " is = " + twoInverse.intValue());
        System.out.println("--------------------------------");

        System.out.println("Please decrypt the modified ciphertext c' using your program from Task 1.");
        Scanner pdScanner = new Scanner(System.in);
        System.out.println("Please input the plaintext m' decrypted from c': ");
        String decryptedPlaintext = pdScanner.nextLine();

        ArrayList<Integer> decryptedPlaintextInt = extractingCiphertext(decryptedPlaintext, N, e);

        // Perform the reverse to find that plain text
        System.out.println("The original plaintext message m computed from m' is: ");
        for(int i = 0; i < decryptedPlaintextInt.size(); i++) {
            BigInteger plaintext, mod, result;
            BigInteger exponent = BigInteger.valueOf(e);
            plaintext = BigInteger.valueOf(unicodeInt.get(i));
            mod = BigInteger.valueOf(N);
            result = plaintext.modPow(exponent, mod);
            System.out.print(result);
        }
    }

    /**
     * Searches all divisors from a provided
     * security parameter.
     *
     * @param v
     * @param factors
     * @return
     */
    public ArrayList<Integer> factors(int v, ArrayList<Integer> factors) {
        // * Completed - New Prime Factorization Method
        int k = (int) Math.floor(Math.sqrt(v));
        // System.out.println("Searching for divisors between 2 and " + k); // --> think about this one too

        for(int i = 2; i <= v; i++) {
            while(v % i == 0) {
                factors.add(i);
                v /= i;
            }
        }

        return factors;
    }

    /**
     * Searches for the two prime numbers from
     * array if the following mutliplication
     * from both selected primes equate to N
     *
     * @param N
     * @param factorsOfN
     * @return
     */
    public static int[] findTwoPrimeNumbers(int N, ArrayList<Integer> factorsOfN) {
        int p = 0;
        int q = 0;
        int resultOfN = 0;
        boolean foundTwoPrimes = false;

        for(int i = 0; i < factorsOfN.size(); i++) {
            for(int j = 0; j < factorsOfN.size(); i++) {
                resultOfN = factorsOfN.get(i) * factorsOfN.get(j);
                if(resultOfN == N) {
                    p = factorsOfN.get(i);
                    q = factorsOfN.get(j);
                    foundTwoPrimes = true;
                    break;
                }
            }
            if(foundTwoPrimes) {
               break;
            }
        }

       return new int[] { p ,q };
    }

    // Not sure about this one
    public int findPrimeNumbers(ArrayList<Integer> factors) {
        // * Completed - Have a review before moving to next task
        Random ran = new Random();;;;
        int randomPrime = ran.nextInt(factors.size());
        int prime = factors.get(randomPrime);
        return prime;
    }

    /**
     * Returns the calculation of the
     * phi of N.
     *
     * @param N
     * @param p
     * @param q
     * @return
     */
    public int phiOfN(int N, int p, int q) {
        int total = (p - 1) * (q - 1);
        return total;
    }

    /**
     * Extended Eucledian Algorithm that
     * returns if the following value
     * is mutually prime.
     *
     * @param e
     * @param M
     * @return
     */
    public static int extendedEucledianAlgorithm(int e, int M) {
        if(e == 0) {
            if(M == 1)  {
                return M;
            }
            return e;
        }

        return extendedEucledianAlgorithm(M % e, e);
    }

    /**
     * Encrypts the specified value of e
     * if if it is mutually prime.
     *
     * @param N
     * @param M
     * @return
     */
    public static int encryption(int N, int M) {
        System.out.println("Please enter the encryption exponent e: ");
        Scanner inputE = new Scanner(System.in);
        int e = inputE.nextInt();
        if(e > N) {
            System.out.println("Your encryption e is above N");
            System.out.println("Please enter the encryption exponent under the set of N");
            encryption(N, M);
        }

        while(true) {
            int gcd = extendedEucledianAlgorithm(e, M);
            if(gcd == 1) {
                break;
            }
            else {
               throw new RuntimeException("Encryption is not mutually prime. Pick another encryption");
            }
        }

        return e;
    }

    /**
     * Extracts a ciphertext that was encrypted
     * that accepts integers.
     *
     * @param cipher
     * @param N
     * @param e
     * @return
     */
    public static ArrayList<Integer> extractingCiphertext(String cipher, int N, int e) {
        char[] cipherCheck = cipher.toCharArray();
        int isLetter = 0;
        int isDigit = 0;
        int unknown = 0;
        for(int i = 0; i < cipherCheck.length; i++){
            if(Character.isDigit(cipherCheck[i])) {
                isDigit++;
            }
            if(Character.isLetter(cipherCheck[i])) {
                isLetter++;
            }
            if(!Character.isLetterOrDigit(cipherCheck[i])) {
                unknown++;
            }
        }

        ArrayList<Integer> unicodeInt = new ArrayList<Integer>();
        if((isLetter > 0 && isDigit > 0) || (unknown > 0)) {
            int whitespace = 0;
            int comma = 0;
            for(int i = 0; i < cipherCheck.length; i++) {
                if(Character.isWhitespace(cipherCheck[i])) {
                    whitespace++;
                }
                String cipherCheckString = String.valueOf(cipherCheck[i]);
                if(cipherCheckString.contains(",")) {
                    comma++;
                }
            }
            if(whitespace > 0 || comma > 0) {
                for(int i = 0; i < cipherCheck.length; i++) {
                    String cipherCheckString = String.valueOf(cipherCheck[i]);
                    if(Character.isWhitespace(cipherCheck[i]) || cipherCheckString.contains(",")) {
                        break;
                    }

                    ArrayList<String> cipherArrayNumber = new ArrayList<String>();
                    cipherArrayNumber.add(String.valueOf(cipherCheck[i]));

                    String stringCipherBuilder = new String();
                    int index_skip = 0;
                    for(int l = i; l < cipherCheck.length; l++) {
                        String SecondCipherCheckString = String.valueOf(cipherCheck[i]);
                        if(Character.isWhitespace(cipherCheck[l]) || SecondCipherCheckString.contains(",")) {
                            break;
                        }
                        stringCipherBuilder += cipherCheck[l];
                        index_skip++;
                    }

                    BigInteger stringCipherBigInteger = new BigInteger(stringCipherBuilder);
                    if(stringCipherBigInteger.intValue() > N) {
                        System.out.println(" ");
                        System.out.println("Pick another ASCII value that does not exceed the value of the security parameter.");
                        System.out.println(" ");
                        encryption(N, e);
                        break;
                    }

                    int unicode = Integer.parseInt(stringCipherBuilder);
                    unicodeInt.add(unicode);

                    i += index_skip;
                }

                return unicodeInt;
            }

        }
        if(isLetter > 0 && isDigit == 0) {
            for(int i = 0; i < cipherCheck.length; i++) {
                if(cipherCheck[i] > N) {
                    System.out.println("Pick ASCII values that do not exceed the security parameter.");
                    encryption(N, e);
                    break;
                }
            }

            ArrayList unicodeChars = new ArrayList<String>();
            for(int i = 0; i < cipher.length(); i++) {
                unicodeChars.add(cipher.charAt(i));
            }

            for(int i = 0; i < unicodeChars.size(); i++) {
                int unicode = cipher.charAt(i);
                unicodeInt.add(unicode);
            }

            return unicodeInt;
        }

        return unicodeInt;
    }

}
