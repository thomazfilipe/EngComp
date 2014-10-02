package Menu;

import java.io.BufferedReader;
import java.io.FileReader;

import paint.model.ModeloGrafico;
import paint.model.Primitiva;
import Ferramentas.Keyboard;
import Ferramentas.Mensagem;

public class ListenerOpPrimitivas implements ListenerMenu {

	private Controller controller;
	private ModeloGrafico modelo;

	public ListenerOpPrimitivas(ModeloGrafico modelo, Controller controller) {
		this.controller = controller;
		this.modelo = modelo;
	}

	public void eventoMenu(int numOp) {
		switch (numOp) {
		case 1:
			// Criar Primitiva
			try {
				criaMenuPrimitivas();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Mensagem.println("Primitiva nao encontrada!");
			}
			break;
		case 2:
			// editar
			MyMenu mEditar = null;
			try {
				mEditar = new MyMenu(
						"Menu Editar \n Na Ordem Desenhada - Selecionado: "
								+ controller.getNomePrimitivaSelecionada());
			} catch (ArrayIndexOutOfBoundsException  e) {
				mEditar = new MyMenu("Menu Editar \n Na Ordem Desenhada - Selecionado: Nenhuma");
			}
			ListenerEditar lE = new ListenerEditar(modelo, controller, mEditar);
			mEditar.add("Selecionar Primitiva", lE);
			mEditar.add("Trazer para frente", lE);
			mEditar.add("Enviar para traz", lE);
			mEditar.add("Trazer 1 para frente", lE);
			mEditar.add("Enviar 1 para traz", lE);
			mEditar.add("Alterar cor de linha", lE);
			mEditar.add("Alterar espessura da linha", lE);
			mEditar.add("Alterar cor de preenchimento", lE);
			mEditar.add("Alterar Pontos", lE);
			mEditar.add("Remover",lE);
			mEditar.add("Voltar", true);
			mEditar.show();
			break;
		case 3:
			// alterar Cor Linha
			ListenerCorLinha lC = new ListenerCorLinha(controller);
			MyMenu mCorFundo = new MyMenu("Opcoes de Cor da Linha");
			mCorFundo.add(new Opcao("Abrir Paleta de cores", lC, true));
			mCorFundo.add(new Opcao("Remover Cor", lC, true));
			mCorFundo.add(new Opcao("Voltar", null, true));
			mCorFundo.show();
			break;
		case 4:
			// alterar Cor Preenchimento
			ListenerCorPreenchimento lCp = new ListenerCorPreenchimento(
					controller);
			MyMenu mCorPreenchimento = new MyMenu(
					"Opcoes de Cor de Preenchimento");
			mCorPreenchimento
					.add(new Opcao("Abrir Paleta de cores", lCp, true));
			mCorPreenchimento.add(new Opcao("Remover Cor", lCp, true));
			mCorPreenchimento.add(new Opcao("Voltar", null, true));
			mCorPreenchimento.show();
			break;
		case 5:
			// alterar Espessura linha
			Mensagem.print("Digite um valor de espessura: ");
			controller.setEspessuraLinha(Keyboard.readInt());
			break;
		}

	}

	private void criaMenuPrimitivas() throws Exception {
		BufferedReader fin = new BufferedReader(
				new FileReader("primitivas.cfg"));

		MyMenu menuPrimitivas = new MyMenu("Menu de Primitivas");
		while (true) {
			String line = fin.readLine();

			if (line == null) {
				break;
			}

			@SuppressWarnings("unchecked")
			Class<Primitiva> clazz = (Class<Primitiva>) Class.forName(line);
			Primitiva p = clazz.newInstance();
			menuPrimitivas.add(p.getNome(), new ListenerPrimitivas(controller,
					clazz));
		}
		fin.close();

		menuPrimitivas.add("Voltar", null, true);
		menuPrimitivas.show();

	}

}
