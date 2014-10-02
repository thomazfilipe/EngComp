package tools;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class OnlyNumberKeyListener implements KeyListener{

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void keyTyped(KeyEvent e) {
		String caracteres="0987654321";
		if(!caracteres.contains(e.getKeyChar()+"")){
			e.consume();
		}
	}

}
