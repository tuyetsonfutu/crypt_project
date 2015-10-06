/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crytographyelgamal;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashSet;

/**
 *
 * @author Francis
 */
public class GeneratorFactory {

    private int minBits, crtty;
    private SecureRandom srng;
    private BigInteger p, g;
    private static final BigInteger ZERO = BigInteger.ZERO, ONE = BigInteger.ONE,
            TWO = ONE.add(ONE), THREE = TWO.add(ONE);
//Constructors

    public GeneratorFactory(int bits) {
        this(bits, 300);
    }

    public GeneratorFactory(int bits, int crtty) {
        this(bits, crtty, new SecureRandom());
    }

    public GeneratorFactory(int bits, int CRTTY, SecureRandom srng) {
        if (bits < 512) {
            System.err.println("WARNING: Safe primes should be >= 512 bits long");
        }
        this.minBits = bits;
        this.crtty = crtty;
        this.srng = srng;
        System.out.printf("Making a safe prime of at least %d bits...\n", bits);
        long startTm = System.currentTimeMillis(), endTm;
        makeSafePrimeAndGenerator();
        endTm = System.currentTimeMillis();
        System.err.printf("Generating p, g took %d ms\n", endTm - startTm);
        System.out.printf("p = %x (%d bits)\n", p, p.bitLength());
        System.out.printf("g = %x (%d bits)\n", g, g.bitLength());
    }

    public void makeSafePrimeAndGenerator() {
        BigInteger r = BigInteger.valueOf(0x7fffffff),
                t = new BigInteger(minBits, crtty, srng);
//(1) make prime p
        do {
            r = r.add(ONE);
            p = TWO.multiply(r).multiply(t).add(ONE);
        } while (!p.isProbablePrime(crtty));
//(2) obtain prime factorization of p-1 = 2rt
        HashSet<BigInteger> factors = new HashSet<BigInteger>();
        factors.add(t);
        factors.add(TWO);
        if (r.isProbablePrime(crtty)) {
            factors.add(r);
        } else {
            factors.addAll(primeFact(r));
        }
        BigInteger pMinusOne = p.subtract(ONE), z, lnr;
        boolean isGen;
        do {
            isGen = true;
            g = new BigInteger(p.bitLength() - 1, srng); //random, < p
            for (BigInteger f : factors) { //check cond on g for each f
                z = pMinusOne.divide(f);
                lnr = g.modPow(z, p);
                if (lnr.equals(ONE)) {
                    isGen = false;
                    break;
                }
            }
        } while (!isGen);

    } // end of makeSafePrimeAndGenerator() method

    public static HashSet<BigInteger> primeFact(BigInteger n) {
        BigInteger nn = new BigInteger(n.toByteArray()); //clone n
        HashSet<BigInteger> factors = new HashSet<BigInteger>();
        BigInteger dvsr = TWO,
                dvsrSq = dvsr.multiply(dvsr);
        while (dvsrSq.compareTo(nn) <= 0) { //divisor <= sqrt of n
            if (nn.mod(dvsr).equals(ZERO)) { //found a factor (must be prime):
                factors.add(dvsr); //add it to set
                while (nn.mod(dvsr).equals(ZERO)) //divide it out from n completely
                {
                    nn = nn.divide(dvsr); //(ensures later factors are prime)
                }
            }
            dvsr = dvsr.add(ONE); //next possible divisor
            dvsrSq = dvsr.multiply(dvsr);
        }
//if nn's largest prime factor had multiplicity >= 2, nn will now be 1;
//if the multimplicity is only 1, the loop will have been exited leaving
//nn == this prime factor;
        if (nn.compareTo(ONE) > 0) {
            factors.add(nn);
        }
        return factors;
    }

    public BigInteger getP() {
        return p;
    }

    public BigInteger getG() {
        return g;
    }

    public static boolean isGenerator(BigInteger p, BigInteger g, int crtty) {
        System.err.printf("Testing p = %s,\ng = %s\n",
                p.toString(16), g.toString(16));
        if (!p.isProbablePrime(crtty)) {
            System.err.println("p is not prime.");
            return false;
        }
        if (g.mod(p).equals(ZERO)) {
            System.err.println("p divides g.");
            return false;
        }
//See note below on generator test
        BigInteger pMinusOne = p.subtract(ONE), z;
        System.err.println("Finding prime factors of p-1 ...");
        HashSet<BigInteger> factors = primeFact(pMinusOne);
        boolean isGen = true;
        for (BigInteger f : factors) { //check cond on g for each f
            z = pMinusOne.divide(f);
            if (g.modPow(z, p).equals(ONE)) {
                isGen = false;
                System.err.println("g is not a generator mod p.");
                break;
            }
        }
        return isGen;
    }
}
