package bolha;

import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class IconBolha extends IconList {
	private List<ImageIcon> listaBolhas = new ArrayList<>();
	private int index = 0;

	public IconBolha(){
		try {
			URL resource = Ponteiro.class.getResource("/resources/buble.png");		
			listaBolhas.add(new ImageIcon(ImageIO.read(resource)));	
			resource = Ponteiro.class.getResource("/resources/bubble2.png");		
			listaBolhas.add(new ImageIcon(ImageIO.read(resource)));	
			resource = Ponteiro.class.getResource("/resources/bubble3.png");		
			listaBolhas.add(new ImageIcon(ImageIO.read(resource)));	
			resource = Ponteiro.class.getResource("/resources/bubble4.png");		
			listaBolhas.add(new ImageIcon(ImageIO.read(resource)));	
			resource = Ponteiro.class.getResource("/resources/bubble5.png");		
			listaBolhas.add(new ImageIcon(ImageIO.read(resource)));	
			resource = Ponteiro.class.getResource("/resources/bubble6.png");		
			listaBolhas.add(new ImageIcon(ImageIO.read(resource)));	
			resource = Ponteiro.class.getResource("/resources/bubble7.png");		
			listaBolhas.add(new ImageIcon(ImageIO.read(resource)));	
			resource = Ponteiro.class.getResource("/resources/bubble8.png");		
			listaBolhas.add(new ImageIcon(ImageIO.read(resource)));	
			resource = Ponteiro.class.getResource("/resources/bubble9.png");		
			listaBolhas.add(new ImageIcon(ImageIO.read(resource)));	
			resource = Ponteiro.class.getResource("/resources/bubble10.png");		
			listaBolhas.add(new ImageIcon(ImageIO.read(resource)));	
			index = listaBolhas.size() -1;
		} catch(Exception e) {
			System.out.println("Image can't be loaded.");
		}
	}
	
	public ImageIcon next(){
		if(index < listaBolhas.size() - 1)
			index++;
		else
			index = 0;
		Image tmp = listaBolhas.get(index).getImage().getScaledInstance(50, 50, 100);
		return new ImageIcon(tmp);
	}
	
	public ImageIcon previous(){
		if(index > 0)
			index--;
		else
			index = listaBolhas.size() - 1;

		Image tmp = listaBolhas.get(index).getImage().getScaledInstance(50, 50, 100);
		return new ImageIcon(tmp);
	}
	
	public Image getSelectedImage(){
		return listaBolhas.get(index).getImage();
	}

}
