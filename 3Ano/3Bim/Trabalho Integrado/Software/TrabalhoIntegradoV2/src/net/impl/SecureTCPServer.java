package net.impl;

import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import net.TCPClient;

public class SecureTCPServer extends DefaultTCPServer {
	private KeyPair keyPair;//guarda a chave publica e privada aqui
	
	public SecureTCPServer(int port, int bits) throws Exception {
		super(port);//cria servidor socket
		//gera chave RSA publica e privada de bits informados
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(bits);
		keyPair = generator.generateKeyPair();
	}
	
	//cria uma conexão segura com o cliente
	protected TCPClient createTCPClient(Socket socket) throws Exception {
		SecureTCPClient client = new SecureTCPClient(socket);
		//efetua troca de chaves
		handshake(client);
		return client;
	}
	
	private void handshake(SecureTCPClient client) throws Exception {
		//aguarda pedido de 'REQUEST_PUBLIC_KEY' do cliente
		byte[] request = client.receive(true);
		//se recebeu o pedido
		if ("REQUEST_PUBLIC_KEY".equals(new String(request))) {
			//envia a chave publica para o cliente
			client.send(keyPair.getPublic().getEncoded());
			//aguarda a chave simetrica
			byte[] cipherEncSecretKey = client.receive(true);
			
			//cria uma descriptografia RSA com a chave privada
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
			//descriptografa a chave simetrica recebida 
			byte[] encSecretKey = cipher.doFinal(cipherEncSecretKey);
			//cria e guarda a chave recebida dentro do cliente da conexão
			SecretKey secretKey = new SecretKeySpec(encSecretKey, "AES");
			client.setSecretKey(secretKey);
		}
	}
}







