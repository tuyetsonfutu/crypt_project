/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crytographyelgamal;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.ArrayList;


/**
 *
 * @author Francis
 */
public class MyElgamal {

    private BigInteger p, g, a, r, pMinus2;
    private SecureRandom srng;
    private static final int CRTTY = 300;
    private static final String configPath = "Key.txt";
    private static final BigInteger ZERO = BigInteger.ZERO, ONE = BigInteger.ONE,
            TWO = ONE.add(ONE), THREE = TWO.add(ONE);

    public MyElgamal(int kSz) {
//Random system with at least (kSz) bits in p
        srng = new SecureRandom();
        GeneratorFactory fact = new GeneratorFactory(kSz, CRTTY, srng);
        p = fact.getP();
        pMinus2 = p.subtract(TWO);
        g = fact.getG();
        //a should be a random integer in range 1 < a < p-1
        BigInteger pmt = p.subtract(THREE);
        a = (new BigInteger(p.bitLength(), srng)).mod(pmt).add(TWO);
        r = g.modPow(a, p);
        saveConfig();
    }

    public MyElgamal() {
        srng = new SecureRandom();
        try {
            BufferedReader in = new BufferedReader(new FileReader(configPath));
            p = new BigInteger(in.readLine(), 16);
            g = new BigInteger(in.readLine(), 16);
            a = new BigInteger(in.readLine(), 16);
            in.close();
        } catch (NumberFormatException ex) {
            System.err.println("Invalid data in config file - " + ex);
            System.exit(1);
        } catch (EOFException ex) {
            System.err.println("Unexpected end of config file");
            System.exit(1);
        } catch (IOException ex) {
            System.err.println("Trouble reading config file");
            System.exit(1);
        } catch (NullPointerException ex) {
            System.err.println("Trouble reading string from config file - " + ex);
            System.exit(1);
        }
        if (g.mod(p).equals(ZERO)) {
            System.err.println(p.toString(16) + " divides " + g.toString(16)
                    + ". Terminating.");
            System.exit(1);
        }
        pMinus2 = p.subtract(TWO);
        r = g.modPow(a, p);
    }

    public void saveConfig() {
        try {
            PrintWriter out = new PrintWriter(new FileWriter(configPath));
            out.println(p.toString(16));
            out.println(g.toString(16));
            out.println(a.toString(16));
            out.close();
        } catch (IOException ex) {
            System.err.println("Could not save the config");
        }
    }

    public byte[] readFile(String fileName) throws IOException {
        Path path = Paths.get(fileName);
        byte[] bytes = Files.readAllBytes(path);
        return bytes;
    }
    
    public BigInteger getG() {
        return g;
    }

    public BigInteger getR() {
        return r;
    }

    public BigInteger getP() {
        return p;
    }

    public BigInteger[] encrypt(BigInteger m) {
        BigInteger k = new BigInteger(p.bitLength(), srng);
        k = k.mod(pMinus2).add(ONE);
        BigInteger[] cipher = new BigInteger[2];
        cipher[0] = g.modPow(k, p);
        cipher[1] = r.modPow(k, p).multiply(m).mod(p);
        return cipher;
    }

    public void encrypt(String filename) throws FileNotFoundException {
        ArrayList<BlockCiphers> blz = new ArrayList<>();
        FileOutputStream fos = new FileOutputStream("cipher.txt");
        PrintWriter pw = new PrintWriter(fos);
        try {
            byte[] plaintext = readFile(filename);
            blz = blockSize(plaintext);
        } catch (IOException ex) {
            System.err.println("Can't not open file");
        }
        for (int i = 0; i < blz.size(); i++) {
            if (blz.get(i).getBlock().length != 8) {
                BigInteger[] cipher = new BigInteger[2];
                blz.get(i).setBlock(padDing(blz.get(i).getBlock()));
                BigInteger plain = new BigInteger(blz.get(i).bt);
                cipher = encrypt(plain);
                pw.println(cipher[0].toString());
                pw.println(cipher[1].toString());
                break;
            } else {
                BigInteger[] cipher = new BigInteger[2];
                BigInteger plain = new BigInteger(blz.get(i).bt);
                cipher = encrypt(plain);
                pw.println(cipher[0].toString());
                pw.println(cipher[1].toString());
            }
        }
        pw.close();
    }

   
    public byte[] padDing(byte[] bytes) {
        if (bytes.length < 8) {
            int n = bytes.length;
            int indexByte = 7;
            //fill byte to last index
            while (n < 0) {
                bytes[indexByte] = bytes[n - 1];
                bytes[n - 1] = 0;
                n--;
                indexByte--;
            }
        }
        return bytes;
    }


    public BigInteger decrypt(BigInteger c0, BigInteger c1) {
        BigInteger c = c0.modPow(a, p).modInverse(p); //c0^-a mod p
        return c.multiply(c1).mod(p);
    }

    public byte[] getBytes(BigInteger bg) {
        byte[] bts = bg.toByteArray();
        if (bg.bitLength() % 8 != 0) {
            return bts;
        } else {
            byte[] sbts = new byte[bg.bitLength() / 8];
            System.arraycopy(bts, 1, sbts, 0, sbts.length);
            return sbts;
        }
    }

    public ArrayList<BlockCiphers> blockSize(byte[] bt) {
        byte[] bytes = null;
        ArrayList<BlockCiphers> bl = new ArrayList<>();
        BlockCiphers blkc;
        for (int i = 0; i < bt.length - (bt.length % 8); i++) {
            if (i % 8 == 0) {
                bytes = new byte[8];
                bytes[i % 8] = bt[i];
            } else {
                if (i % 8 == 7) {
                    bytes[i % 8] = bt[i];
                    blkc = new BlockCiphers(bytes, 8);
                    bl.add(blkc);
                } else {
                    bytes[i % 8] = bt[i];
                }
            }
        }

        if (bt.length % 8 != 0) {
            bytes = new byte[bt.length % 8];
            for (int i = bt.length - (bt.length % 8); i < bt.length; i++) {
                bytes[i % 8] = bt[i];
            }
            blkc = new BlockCiphers(bytes, (bt.length % 8));
            bl.add(blkc);
        }
        return bl;
    }

    public void decrypt(String filename) throws FileNotFoundException, IOException {
        File file = new File(filename);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        FileOutputStream fos = new FileOutputStream("plain.pdf");
        //FileWriter fs = new FileWriter("out.pdf");
       // BufferedWriter out = new BufferedWriter(fs); 
        BigInteger c0;
        BigInteger c1;
        BigInteger plain;
        String line;

        while ((line = br.readLine()) != null) {
            c0 = new BigInteger(line);
            c1 = new BigInteger(br.readLine());
            plain = decrypt(c0, c1);
            fos.write(plain.toByteArray());
            //for(Byte b : plain.toByteArray()){
              //  out.write(b);
            //}
        }
       // out.close();
        br.close();
        fr.close();
        fos.close();
    }
    

}

class BlockCiphers {

    byte[] bt;

    public BlockCiphers(byte[] b, int blkSize) {
        bt = new byte[blkSize];
        this.bt = b;
    }

    public void setBlock(byte[] bt) {
        this.bt = bt;
    }

    public byte[] getBlock() {
        return bt;
    }
}
