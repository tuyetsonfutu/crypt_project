package idea;
import java.math.BigInteger;;
public class RSA {
	BigInteger public_key[] = new BigInteger[2];
	BigInteger private_Key[] = new BigInteger[2];
	public RSA(BigInteger q, BigInteger p){
		BigInteger n = p.multiply(q);
		System.out.println("n = " + n.toString());
		BigInteger m = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
		System.out.println("m = " +m.toString());
		BigInteger e = new BigInteger("3");;
		while (m.gcd(e).intValue() > 1){
			e = e.add(new BigInteger("2"));
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
		return (new BigInteger(plant.getBytes())).modPow(public_key[1], public_key[0]).toString();
	}
	public  String encrypt(byte c){
		BigInteger v = new BigInteger(Integer.toString((int)c));
		BigInteger bg = v.pow(public_key[1].intValue());
		return bg.mod(public_key[0]).toString();
	}
	public  String decript(String cypher){
		BigInteger v = new BigInteger(cypher);
		BigInteger bg = v.pow(private_Key[1].intValue());
		return bg.mod(public_key[0]).toString();
	}
}