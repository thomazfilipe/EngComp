package tools;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;


public class NumberDotKeyListener implements KeyListener{
	private JTextField txtField;
	
	public NumberDotKeyListener(JTextField txtField) {
		this.txtField = txtField;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void keyTyped(KeyEvent e) {
		String txt = txtField.getText();
		int countDot = 0;
		for(int i = 0; i < txt.length(); i++){
			if(txt.charAt(i) == '.')
				countDot++;
		}
		
		String caracteres="0987654321.";
		if(!caracteres.contains(e.getKeyChar()+"")){
			e.consume();
		}
		if(e.getKeyChar() == '.'){
			if(countDot == 3)
				e.consume();
		}		
	}

}
