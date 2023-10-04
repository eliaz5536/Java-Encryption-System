package Main;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class eos6_task4B {
    /**
     * Initializes the following procedure
     * of overviewing security of IND CCA for
     * Goldwasser Micali
     */
    public void IND_CCA_security() {
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter the public parameter N: ");
        int N = input.nextInt();
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
        System.out.println("Please enter the encryption exponent y: ");
        Scanner inputY = new Scanner(System.in);
        int y = inputY.nextInt();
        System.out.println(y);
        System.out.println("---------------------------------");

        System.out.println("Please enter the ciphertext c: ");
        String cipher = input.nextLine();
        System.out.println(cipher);

        ArrayList<BigInteger> unicodeInt = extractingCiphertext(cipher, N, y);

        // c' = c * z^2
        int z = randomZ(N);
        System.out.println("The modified ciphertext c' is = ");
        for(int i = 0; i < unicodeInt.size(); i++) {
            BigInteger cipherText, N_BigInt, randomZBigInt, Z_powerofTwo, TWO, result;
            cipherText = unicodeInt.get(i);
            N_BigInt = BigInteger.valueOf(N);
            TWO = new BigInteger("2");
            randomZBigInt = BigInteger.valueOf(z);
            Z_powerofTwo = randomZBigInt.pow(TWO.intValue());
            result = (cipherText.multiply(Z_powerofTwo)).mod(N_BigInt);
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
     * from both selected primes equate to N.
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

    /**
     * Calculates the phi of N.
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
     * Extracts a ciphertext that was encrypted
     * that accepts integers.
     *
     * @param cipher
     * @param N
     * @param e
     * @return
     */
    public static ArrayList<BigInteger> extractingCiphertext(String cipher, int N, int e) {
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

        ArrayList<BigInteger> unicodeInt = new ArrayList<BigInteger>();
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
                System.out.println("Extracting numbers with whitespace and comma");
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

                    int unicode = Integer.parseInt(stringCipherBuilder);
                    unicodeInt.add(BigInteger.valueOf(unicode));

                    i += index_skip;
                }

                return unicodeInt;
            }

        }
        if(isLetter > 0 && isDigit == 0) {
            ArrayList unicodeChars = new ArrayList<String>();
            for(int i = 0; i < cipher.length(); i++) {
                unicodeChars.add(cipher.charAt(i));
            }
            for(int i = 0; i < unicodeChars.size(); i++) {
                int unicode = cipher.charAt(i);
                // unicodeInt.add(unicode);
                unicodeInt.add(BigInteger.valueOf(unicode));
            }
            return unicodeInt;
        }

        return unicodeInt;
    }

    /**
     * Finds random z that is mutually prime
     *
     * @param N
     * @return
     */
    public static int randomZ(int N) {
        Random ran = new Random();
        int z = ran.nextInt(N);
        int random = extendedEucledianAlgorithm(z, N);
        if(random != 1)  {
           randomZ(N);
        }
        return z;
    }
}
