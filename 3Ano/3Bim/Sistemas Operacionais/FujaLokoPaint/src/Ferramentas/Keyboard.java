package Ferramentas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Keyboard {
	private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	
	public static String readString() {
		String str = null;
		try {
			str = in.readLine();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return str;
	}
	
	public static int readInt() {
		return Integer.parseInt(readString());
	}
}