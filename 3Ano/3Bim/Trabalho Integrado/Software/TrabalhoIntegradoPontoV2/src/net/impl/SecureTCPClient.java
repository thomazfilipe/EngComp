package net.impl;

import java.net.Socket;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class SecureTCPClient extends DefaultTCPClient {
	private SecretKey secretKey;
	
	public SecureTCPClient(String address, int port) throws Exception {
		super(address, port);
		handshake(this);
	}
	
	public SecureTCPClient(Socket socket) throws Exception {
		super(socket);
	}

	private void handshake(SecureTCPClient client) throws Exception {
		KeyGenerator generator = KeyGenerator.getInstance("AES");
		SecretKey secretKey = generator.generateKey();
		
		client.send("REQUEST_PUBLIC_KEY".getBytes());
		byte[] encPublicKey = client.receive(true);
		PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(encPublicKey));
			
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] cipherEncSecretKey = cipher.doFinal(secretKey.getEncoded());

		client.send(cipherEncSecretKey);
		client.setSecretKey(secretKey);
	}
	
	public void setSecretKey(SecretKey secretKey) {
		this.secretKey = secretKey;
	}
	
	private byte[] encrypt(byte[] plainMessage) throws Exception {
		if (secretKey == null) return plainMessage;
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		byte[] cipherMessage = cipher.doFinal(plainMessage);
		return cipherMessage;		
	}
	
	public byte[] decrypt(byte[] cipherMessage) throws Exception {
		if (secretKey == null) return cipherMessage;
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		byte[] plainMessage = cipher.doFinal(cipherMessage);
		return plainMessage;		
	}
	
	@Override
	public void send(byte[] message) throws Exception {
		byte[] cipherMessage = encrypt(message);
		//System.out.println(new String(cipherMessage));
		super.send(cipherMessage);
	}
	
	@Override
	protected byte[] receive(boolean wait) throws Exception {
		byte[] cipherMessage = super.receive(wait); 
		//System.out.println(new String(cipherMessage));
		return decrypt(cipherMessage);
	}
} 
