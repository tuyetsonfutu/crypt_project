package rsa;
import java.math.BigInteger;
import java.util.Random;
public class RSA {
	BigInteger public_key[] = new BigInteger[2];
	BigInteger private_Key[] = new BigInteger[2];
	public RSA(BigInteger pb, BigInteger pr ,BigInteger nf ){
		this.private_Key[0] = nf;
		this.private_Key[1] = pr;
		this.public_key[0] = nf;
		this.public_key[1] = pb;
	}
	public RSA(BigInteger q, BigInteger p, int bitlength){
		Random r =new Random();
		BigInteger n = p.multiply(q);
		System.out.println("n = " + n.toString());
		BigInteger m = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
		System.out.println("m = " + m.toString());
		System.out.println("bit_lenght = " + bitlength);
		BigInteger e = BigInteger.probablePrime(bitlength, r);
		while (m.gcd(e).compareTo(BigInteger.ONE) > 0 && e.compareTo(m) < 0 ) {
			e.add(BigInteger.ONE);
		}
		System.out.println("e = " + e.toString());
		BigInteger d = e.modInverse(m);
		System.out.println("d = "+ d.toString());
	
		this.private_Key[0] = n;
		this.private_Key[1] = d;
		this.public_key[0] = n;
		this.public_key[1] = e;
	}
	public BigInteger [] getPrivateKey(){
		return private_Key;
	}
	public BigInteger [] getPublicKey(){
		return public_key;
	}
	public  String encrypt(String plant){
		//System.out.println(plant);
		BigInteger mp = new BigInteger(plant,2);
		//System.out.println(mp);
		return (mp.modPow(public_key[1], public_key[0])).toString();
	}
	public  String decript(String cypher){
		BigInteger v = new BigInteger(cypher);
		//System.out.println(private_Key[1] +"  "+ public_key[1]);
		BigInteger bg = v.pow(private_Key[1].intValue());
		//System.out.println(cypher +"  "+ (bg.mod(public_key[0])).toString());
		return (bg.mod(public_key[0])).toString();
	}
}