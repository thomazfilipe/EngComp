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
	
	//inicia conexão segura com o servidor
	public SecureTCPClient(String address, int port) throws Exception {
		super(address, port);
		//solicita troca de chaves
		handshake(this);
	}
	
	//cria conexão segura
	public SecureTCPClient(Socket socket) throws Exception {
		super(socket);
	}

	//método de troca de chaves
	private void handshake(SecureTCPClient client) throws Exception {
		//gera as chave simetrica
		KeyGenerator generator = KeyGenerator.getInstance("AES");
		SecretKey secretKey = generator.generateKey();
		//envia pedido da chave publica
		client.send("REQUEST_PUBLIC_KEY".getBytes());
		//aguarda a chave publica até o TimeOut
		byte[] encPublicKey = client.receive(true);
		//armazena a chave publica do servidor
		PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(encPublicKey));
		
		//cria uma criptografia RSA com a chave publica recebida
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		//criptografa a chave simetrica com a chave publica recebida e converte em bytes
		byte[] cipherEncSecretKey = cipher.doFinal(secretKey.getEncoded());

		client.send(cipherEncSecretKey);//envia a chave simetrica ao servidor
		client.setSecretKey(secretKey);//salva a chave simetrica gerada para este cliente
	}
	
	//salva a chave fornecida no objeto
	public void setSecretKey(SecretKey secretKey) {
		this.secretKey = secretKey;
	}
	
	//encripta o conteudo
	private byte[] encrypt(byte[] plainMessage) throws Exception {
		if (secretKey == null) return plainMessage;
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		byte[] cipherMessage = cipher.doFinal(plainMessage);
		return cipherMessage;		
	}
	
	//decripta o conteudo
	public byte[] decrypt(byte[] cipherMessage) throws Exception {
		if (secretKey == null) return cipherMessage;
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		byte[] plainMessage = cipher.doFinal(cipherMessage);
		return plainMessage;		
	}
	
	//envia conteudo criptografado
	public void send(byte[] message) throws Exception {
		byte[] cipherMessage = encrypt(message);
		//System.out.println(new String(cipherMessage));
		super.send(cipherMessage);
	}
	
	//recebe conteudo e descritografa
	protected byte[] receive(boolean wait) throws Exception {
		byte[] cipherMessage = super.receive(wait); 
		//System.out.println(new String(cipherMessage));
		return decrypt(cipherMessage);
	}
} 
