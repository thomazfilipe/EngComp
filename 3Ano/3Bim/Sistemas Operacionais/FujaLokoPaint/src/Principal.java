import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import bolha.ModeloGrafico;
import bolha.Screen;

public class Principal {

	private ModeloGrafico modelo;
	private JFrame fApp;

	public Principal(ModeloGrafico modelo) {
		this.modelo = modelo;
		// /criando janela
		fApp = new Screen(modelo, 60);
		fApp.setTitle("Fuja Loko - Bolhas Malditas");
		fApp.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		fApp.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				Principal.this.modelo.stopThreads();
				Principal.this.modelo.stopMonitor();
				System.exit(0);
			}
		});
		fApp.pack();
		fApp.setResizable(false);
		fApp.setVisible(true);
		// deixa cursor do mouse invisivel
		Image cursorImage = Toolkit.getDefaultToolkit().getImage("xparent.gif");
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
				cursorImage, new Point(0, 0), "");
		fApp.setCursor(blankCursor);

	}
	
	public static Point centrarFrame(JFrame frame) {
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension tamanhoTela = kit.getScreenSize();

		int width = tamanhoTela.width;
		int height = tamanhoTela.height;

		return new Point((width / 2) - (frame.getSize().width / 2), (height / 2)
				- (frame.getSize().height / 2));
	}

}
