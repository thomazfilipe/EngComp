package bolha;

import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class IconPonteiro extends IconList{
	private List<ImageIcon> listaPonteiros = new ArrayList<>();
	private int index = 0;

	public IconPonteiro(){
		try {
			URL resource = Ponteiro.class.getResource("/resources/cursor.png");		
			listaPonteiros.add(new ImageIcon(ImageIO.read(resource)));		
			resource = Ponteiro.class.getResource("/resources/cursor-icon.png");		
			listaPonteiros.add(new ImageIcon(ImageIO.read(resource)));
			resource = Ponteiro.class.getResource("/resources/ponteiro2.png");		
			listaPonteiros.add(new ImageIcon(ImageIO.read(resource)));
			resource = Ponteiro.class.getResource("/resources/ponteiro3.png");		
			listaPonteiros.add(new ImageIcon(ImageIO.read(resource)));
			index = listaPonteiros.size() -1;
		} catch(Exception e) {
			System.out.println("Image can't be loaded.");
		}
	}
	
	public ImageIcon next(){
		if(index < listaPonteiros.size() - 1)
			index++;
		else
			index = 0;

		Image tmp = listaPonteiros.get(index).getImage().getScaledInstance(50, 50, 100);
		return new ImageIcon(tmp);
	}
	
	public ImageIcon previous(){
		if(index > 0)
			index--;
		else
			index = listaPonteiros.size() - 1;

		Image tmp = listaPonteiros.get(index).getImage().getScaledInstance(50, 50, 100);
		return new ImageIcon(tmp);
	}
	
	public Image getSelectedImage(){
		return listaPonteiros.get(index).getImage();
	}
}
