package paint.model;

import java.awt.Dimension;
import java.util.*;
import java.io.*;
import java.awt.image.*;
import java.awt.Graphics;
import java.awt.Color;
import javax.imageio.*;

// MODEL

public class ModeloGrafico extends Observable implements Serializable {
	private static final long serialVersionUID = 1L;

	private int width = 500;
	private int height = 500;
	private Color bg = null;
	private boolean saved = true;

	private List<Primitiva> primitivas;
	
	public ModeloGrafico() {
		primitivas = new ArrayList<>();
	}
	
	public void newModeloGrafico(){
		width = 500;
		height = 500;
		bg = null;
		primitivas = new ArrayList<>();
		setChanged();
		notifyObservers();
		saved = true;
	}
	
	public boolean isSave(){
		return saved;
	}
	
	public void setColorBG(Color c) {
		bg = c;
		setChanged();
		notifyObservers();
		saved = false;
	}

	public Color getColorBG() {
		return bg;
	}
	
	public void add(Primitiva p) {
		primitivas.add(p);
		
		setChanged();
		notifyObservers();
		saved = false;
	}
	
	public void remove(int id) {
		primitivas.remove(id);
		
		setChanged();
		notifyObservers();
		saved = false;
	}
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public Dimension getDimension() {
		return new Dimension(width, height);
	}
	
	public List<Primitiva> getPrimitivas() {
		return primitivas;
	}
	
	public void ordemPrimitivas(int atual, int nova) {
		int diferenca = atual - nova;
		Primitiva pSel = primitivas.get(atual);
		if (diferenca == 1 || diferenca == -1) {
			primitivas.set(atual, primitivas.get(nova));
			primitivas.set(nova, pSel);
		} else if (diferenca < -1) {// se for para enviar para frente
			int tamanhoList = primitivas.size() - 1;
			for (int i = atual; i < tamanhoList; i++) {
				primitivas.set(i, primitivas.get(i + 1));
				if ((i + 1) == nova) {
					primitivas.set( i + 1, pSel);
					break;
				}
			}
		} else if (diferenca > 1) {// se for para enviar para traz
			int finalLista = primitivas.size() - 1;
			for (int i = finalLista; i > 0; i--) {
				primitivas.set(i, primitivas.get(i - 1));
				if ((i - 1) == nova) {
					primitivas.set(i - 1, pSel);
					break;
				}
			}
		}
		setChanged();
		notifyObservers();
		saved = false;
	}
	
	public void salvar(String arquivo) throws Exception {
		saved = true;
		String nomeCompleto = arquivo;
		if(!arquivo.contains(".ser")){
			nomeCompleto = arquivo + ".ser";
		}
		FileOutputStream fout = new FileOutputStream(nomeCompleto);
		ObjectOutputStream oout = new ObjectOutputStream(fout);
		oout.writeObject(this);
		oout.close();
		fout.close();		
	}
	
	public static ModeloGrafico carregar(String arquivo) throws Exception {
		String nomeCompleto = arquivo;
		if(!arquivo.contains(".ser")){
			nomeCompleto = arquivo + ".ser";
		}
		FileInputStream fin = new FileInputStream(nomeCompleto);
		ObjectInputStream oin = new ObjectInputStream(fin);
		Object obj = oin.readObject();
		oin.close();
		fin.close();
		
		return (ModeloGrafico) obj;
	}
	
	public String toXML() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("<?xml version=\"1.0\"?>\n\n");
		
		sb.append("<modeloGrafico width=\"")
		  .append(width)
		  .append("\" height=\"")
		  .append(height)
		  .append("\">\n");
		  
		for (Primitiva p : primitivas) {
			sb.append(p.toXML());
		}
		  
		sb.append("</modeloGrafico>\n");
		
		return sb.toString();
	}
	
	public void salvarToXML(String arquivo) throws Exception {
		String nomeCompleto = arquivo;
		if(!arquivo.contains(".xml")){
			nomeCompleto = arquivo + ".xml";
		}
		PrintStream fout = new PrintStream(nomeCompleto);
		fout.println(toXML());
		fout.close();
		saved = true;
	}
	
	private BufferedImage createBufferedImage() {
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
			
		Graphics g = bi.getGraphics();	
		if(bg != null)
		{
			g.setColor(bg);
			g.fillRect(0, 0, width, height);		
		}
		for (Primitiva p : primitivas) {
			p.desenhar(g);
		}		
		g.dispose();
		
		return bi;
	}
		
	// jpg, bmp, jpeg, wbmp, png, gif
	public void export(String arquivo, String formato) throws Exception {
		BufferedImage bi = createBufferedImage();
		String nomeCompleto = arquivo;
		if(!arquivo.contains(formato)){
			nomeCompleto = arquivo + formato;
		}
		ImageIO.write(bi, formato, new File(nomeCompleto));
	}
	
	public void exportToJPEG(String arquivo) throws Exception {
		BufferedImage bi = createBufferedImage();
		String nomeCompleto = arquivo;
		if(!arquivo.contains(".jpg")){
			nomeCompleto = arquivo + ".jpg";
		}
		ImageIO.write(bi, "jpeg", new File(nomeCompleto));
	}

	public void exportToPNG(String arquivo) throws Exception {
		BufferedImage bi = createBufferedImage();
		String nomeCompleto = arquivo;
		if(!arquivo.contains(".png")){
			nomeCompleto = arquivo + ".png";
		}
		ImageIO.write(bi, "png", new File(nomeCompleto));
	}

	public void setPrimitiva(Primitiva p, int idP) {
		primitivas.set(idP, p);
		setChanged();
		notifyObservers();
	}
	
}




























