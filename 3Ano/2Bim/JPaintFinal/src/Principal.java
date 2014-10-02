import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import paint.Paint;
import paint.model.ModeloGrafico;
import Ferramentas.Keyboard;
import Ferramentas.Mensagem;
import Menu.Controller;
import Menu.ListenerCorFundo;
import Menu.ListenerMenu;
import Menu.ListenerOpPrimitivas;
import Menu.MyMenu;
import Menu.Opcao;

public class Principal {

	private static ModeloGrafico modelo;
	private static JFrame fApp;
	private static String nomeArquivo;

	public static void recriaJanela(final ModeloGrafico modelo) {
		Principal.modelo = modelo;
		if (fApp != null) {
			fApp.setVisible(false);
		}
		fApp = new Paint(modelo);
		fApp.setTitle("JPaint [Arquivo: "+nomeArquivo+"]");
		fApp.setResizable(false);		
		fApp.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		fApp.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				if (!modelo.isSave()) {
					int sel = JOptionPane
							.showConfirmDialog(null,
									"O Modelo ainda nao foi salvo desde a ultima alteracao. Deseja salvar?");
					if (sel == JOptionPane.OK_OPTION) {
						salvar();
						System.exit(0);
					} else if (sel == JOptionPane.NO_OPTION) {
						System.exit(0);
					}
					else{
						return;
					}
				}
				System.exit(0);
			}
		});
		fApp.pack();
		fApp.setVisible(true);
		// apenas para trazer para frente, não achei método melhor
		fApp.setAlwaysOnTop(true);
		fApp.setAlwaysOnTop(false);
	}

	public static void main(String[] args) throws Exception {
		modelo = new ModeloGrafico();
		fApp = new Paint(modelo);
		fApp.setTitle("JPaint [Arquivo: ainda não foi criado]");
		fApp.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		fApp.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				if (!modelo.isSave()) {
					int sel = JOptionPane
							.showConfirmDialog(null,
									"O Modelo ainda nao foi salvo desde a ultima alteracao. Deseja salvar?");
					if (sel == JOptionPane.OK_OPTION) {
						salvar();
						System.exit(0);
					} else if (sel == JOptionPane.NO_OPTION) {
						System.exit(0);
					}
					else{
						return;
					}					
				}
				System.exit(0);
			}
		});
		fApp.pack();
		fApp.setResizable(false);
		fApp.setVisible(true);

		ListenerPrincipal pL = new ListenerPrincipal();
		MyMenu m = new MyMenu("Menu Principal");
		m.add("Criar novo", pL);
		m.add("Alterar Cor de Fundo", pL);// ok
		m.add("Primitivas", pL);// ok
		m.add("Exportar", pL);
		m.add("Salvar", pL);
		m.add("Salvar Como...", pL);
		m.add("Abrir", pL);
		m.show();

	}

	public static class ListenerPrincipal implements ListenerMenu {
		Controller controller;

		public ListenerPrincipal() {
			controller = new Controller(modelo);
		}

		public void eventoMenu(int numOp) {

			switch (numOp) {
			case 1:
				if (!modelo.isSave()) {
					int sel = JOptionPane
							.showConfirmDialog(null,
									"O Modelo ainda nao foi salvo desde a ultima alteracao. Deseja salvar?");
					if (sel == JOptionPane.OK_OPTION) {
						salvar(false);
					} else if (sel == JOptionPane.NO_OPTION) {
					}
					else{
						break;
					}
				}
				modelo.newModeloGrafico();
				break;
			case 2:
				ListenerCorFundo lC = new ListenerCorFundo(controller);
				MyMenu mCorFundo = new MyMenu("Opcoes de Cor de Fundo");
				mCorFundo.add(new Opcao("Abrir Paleta de cores", lC, true));
				mCorFundo.add(new Opcao("Remover Cor", lC, true));
				mCorFundo.add(new Opcao("Voltar", null, true));
				mCorFundo.show();
				break;
			case 3:
				ListenerOpPrimitivas lOp = new ListenerOpPrimitivas(modelo,
						controller);
				MyMenu mOpPrimitivas = new MyMenu("Opcoes das Primitivas");
				mOpPrimitivas.add(new Opcao("Criar Primitiva", lOp));
				mOpPrimitivas.add(new Opcao("Editar Primitiva", lOp));
				mOpPrimitivas
						.add(new Opcao("Alterar Cor da Linha Padrao", lOp));
				mOpPrimitivas.add(new Opcao(
						"Alterar Cor de Preenchimento Padrao", lOp));
				mOpPrimitivas.add(new Opcao(
						"Alterar Espessura da linha Padrao", lOp));
				mOpPrimitivas.add(new Opcao("Voltar", null, true));
				mOpPrimitivas.show();
				break;
			case 4:
				exportar();
				break;
			case 5:
				salvar(false);
				break;
			case 6:
				salvar(true);
				break;
			case 7:
				abrir();
				break;
			}
		}

		private void exportar() {
			boolean ok = false;
			while (!ok) {
				Mensagem.print("Digite o local de destino (ex: C:\\documentos\\imagem.png) : ");
				String nomeArq = Keyboard.readString();

				if (nomeArq.isEmpty()
						|| nomeArq.charAt(nomeArq.length() - 1) == '\\') {
					nomeArq += "Untitled.png";
				}
				if (!nomeArq.contains(".png")) {
					nomeArq += ".png";
				}
				try {
					modelo.exportToPNG(nomeArq);
					ok = true;
				} catch (Exception e) {
					Mensagem.println("O caminho é invalido ou nao acessivel");
					continue;
				}

				String caminho = nomeArq.contains("\\") ? nomeArq : System
						.getProperty("user.dir") + "\\" + nomeArq;
				Mensagem.println("Arquivo exportado com sucesso em: " + caminho);
			}
			;
		}

		private void salvar(boolean novo) {
			try {
				if (nomeArquivo == "" || novo) {
					Mensagem.print("Digite o caminho aonde deseja salvar (ex: 'C:\\Documentos\\teste.ser' ou 'teste'): ");
					String nomeArq = Keyboard.readString();
					if (nomeArq.isEmpty()
							|| nomeArq.charAt(nomeArq.length() - 1) == '\\') {
						nomeArq += "Untitled.ser";
					}
					if(!nomeArq.contains(".ser"))
						nomeArq += ".ser";
					nomeArquivo = nomeArq;
				}
				modelo.salvar(nomeArquivo);
				fApp.setTitle("JPaint [Arquivo: "+nomeArquivo+"]");
				Mensagem.println("Salvo com sucesso em: " + nomeArquivo);

			} catch (Exception e) {
				Mensagem.println("O caminho é invalido ou nao acessivel!");
			}
		}

		private void abrir() {
			try {
				Mensagem.print("Digite o caminho aonde encontra-se o arquivo (ex: 'C:\\Documentos\\teste.ser' ou 'teste'): ");
				String nomeArq = Keyboard.readString();
				fApp.setVisible(false);
				modelo = ModeloGrafico.carregar(nomeArq);
				controller.setModeloGrafico(modelo);
				nomeArquivo = nomeArq;
				recriaJanela(modelo);
				fApp.setVisible(true);
				Mensagem.println("Arquivo " + nomeArq + " aberto com sucesso!");				
			} catch (Exception e) {
				Mensagem.println("Falha ao tentar abrir!");
			}
		}
	}

	public static void salvar() {
		try {
			String nomeArq = null;
			if(nomeArquivo == ""){
				nomeArq = "Untitled.ser";
				nomeArquivo = nomeArq;
			}
			else
				nomeArq = nomeArquivo;

			modelo.salvar(nomeArq);
			Mensagem.println("Salvo com sucesso em: " + nomeArq);
		} catch (Exception e) {
			Mensagem.println("O caminho é invalido ou nao acessivel!");
		}
	}

}
