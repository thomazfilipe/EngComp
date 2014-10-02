package Ferramentas;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;

public abstract class AbstractScreen extends JFrame implements KeyListener, MouseMotionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public abstract void keyPressed(KeyEvent e);

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public abstract void mouseMoved(MouseEvent e);
}
