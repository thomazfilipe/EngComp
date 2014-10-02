package bolha;

import java.awt.Image;

import javax.swing.ImageIcon;

public abstract class IconList {
	
	public abstract ImageIcon next();
	public abstract ImageIcon previous();
	public abstract Image getSelectedImage();
}
