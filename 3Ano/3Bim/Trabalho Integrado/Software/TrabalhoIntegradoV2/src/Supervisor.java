import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import net.server.Servidor;
import tabs.TabConfiguracao;
import tabs.TabMonitor;
import tabs.TabRelatorio1;
import tabs.TabRelatorio2;
import users.Usuarios;
import database.Db;

public class Supervisor {
	
	public static JTabbedPane tabs = new JTabbedPane();
	public static Servidor servidor;
	public static Db conexao = new Db("supervisor","root", "positivo");
	public static Usuarios users = new Usuarios(conexao);

	public static void main(String[] args) {
		///cria conexão do servidor Sockets
		try {
			servidor = new Servidor(conexao);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, 
					"Falha ao criar o servidor: " + e.getMessage(), 
					"Falha", 
					JOptionPane.ERROR_MESSAGE);
		}
		////////iniciando JFrame
		JFrame JApp = new JFrame("Trabalho Integrado - Servidor");
		JApp.setSize(850, 700);
		JApp.setResizable(false);
		JApp.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		JApp.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				///se o servidor iniciou
				if(servidor != null){
					//se o servidor esta rodando
					if (servidor.isRunning()) {
						servidor.stop();
					}
					
					conexao.disconect();
				}
				System.exit(0);
			}
		});
		
		////adiciona o JPanels nas Tabs
		tabs.add("Monitor", new TabMonitor(conexao, users, servidor));
		tabs.add("Relatórios de Fluxo", new TabRelatorio1(conexao));
		tabs.add("Relatorio de Comandos",new TabRelatorio2(conexao));
		tabs.add("Configuracao", new TabConfiguracao(tabs, servidor, users));
		
		tabs.setEnabled(false);//desativa as abas inicialmente
		tabs.setSelectedIndex(3);//determina a aba de configuração como selecionado			
		
		JApp.add(tabs);
		JApp.setLocation(centrarFrame(JApp));
		
		JApp.setVisible(true);
		
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
