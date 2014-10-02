package Ferramentas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.awt.Color;

import Erros.CorInvalidaException;

public class Keyboard {
	private static BufferedReader in = new BufferedReader(
			new InputStreamReader(System.in));

	public static String readString() {
		String str = null;
		try {
			str = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}
	public static int readInt() {
		return Integer.parseInt(readString());
	}

	public static Color readColor() throws IllegalArgumentException, CorInvalidaException{
		Color c = null;
		String corString = readString();
		if(corString.isEmpty()){
			return c;
		}
		if(corString.length() == 6){
			corString = "0x"+corString;
			c = Color.decode(corString);
		}
		else
		{
			String[] rgb = corString.split(" ");
			if(rgb.length != 3){
				throw new CorInvalidaException();
			}			
			c = new Color(num(rgb[0]), num(rgb[1]), num(rgb[2]));
		}		
		return c;
	}

	private static int num(String s) {
		return Integer.parseInt(s);
	}
}