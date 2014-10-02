package paint.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class Primitiva implements Serializable {
	protected List<Point> pontos = new ArrayList<>();
	protected int espessuraLinha = 5;
	protected Color corLinha = Color.BLACK;
	protected Color corPreenchimento = null;

	public void add(Point p) {
		if (pontos.size() == getQtddPontos()) {
			throw new IllegalArgumentException(
					"qtdd de pontos ja eh suficiente");
		}
		pontos.add(p);
	}
	
	public void removeAllPoints(){
		pontos.clear();
	}

	public void setCorLinha(Color corLinha) {
		this.corLinha = corLinha;
	}

	public void setCorPreenchimento(Color corPreenchimento) {
		this.corPreenchimento = corPreenchimento;
	}

	public void setEspessuraLinha(int espessuraLinha) {
		this.espessuraLinha = espessuraLinha;
	}

	public String toXML() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("<primitiva class=\"").append(this.getClass().getName())
				.append("\" espessuraLinha=\"").append(espessuraLinha)
				.append("\" corLinha=\"").append(corLinha.getRGB())
				.append("\" corPreenchimento=\"")
				.append(corPreenchimento.getRGB()).append("\" ");

		sb.append(">\n");
		for (int i = 0; i < pontos.size(); i++) {
			sb.append("<List class=\"java.util.ArrayList\">")
					.append("\n<Point method=\"add\">")
					.append("\n<Point class=\"java.awt.Point\"><Point property=\"x\">")
					.append("\n<int>" + pontos.get(i).x + "</int>")
					.append("\n</Point>")
					.append("\n<Point property=\"y\">")
					.append("\n<int>" + pontos.get(i).y + "</int>")
					.append("\n</Ponit>")
					.append("\n</Point>")
					.append("\n</Point>")
					.append("\n</List>\n");
		}
		sb.append("</primitiva>\n");
		return sb.toString();
	}

	public abstract void desenhar(Graphics g);

	public abstract int getQtddPontos();

	public abstract String getNome();

	public abstract String getDescricao();
}
