package Menu;

import java.util.List;

import Ferramentas.Keyboard;
import Ferramentas.Mensagem;

import paint.model.ModeloGrafico;
import paint.model.Primitiva;

public class ListenerEditar implements ListenerMenu {

	private ModeloGrafico modelo;
	private Controller controller;
	private MyMenu mEditar;

	public ListenerEditar(ModeloGrafico modelo, Controller controller, MyMenu mEditar) {
		this.modelo = modelo;
		this.controller = controller;
		this.mEditar = mEditar;
	}

	public void eventoMenu(int numOp){
		// TODO Auto-generated method stub
		switch (numOp) {
		case 1:
			// Selecionar Primitiva
			List<Primitiva> pL = modelo.getPrimitivas();
			MyMenu mDesenhadas = new MyMenu("Listas de Primitivas Desenhadas: ");
			for (int i = 0; i < pL.size(); i++) {
				mDesenhadas.add(pL.get(i).getNome(), new ListenerSelecionar(controller), true);
			}
			mDesenhadas.add("Voltar", true);
			mDesenhadas.show();
			try {
				mEditar.setTitulo("Menu Editar \n Na Ordem Desenhada - Selecionado: " + controller.getNomePrimitivaSelecionada());
			} catch (ArrayIndexOutOfBoundsException e) {
				// TODO Auto-generated catch block
				mEditar.setTitulo("Menu Editar \n Na Ordem Desenhada - Selecionado: Nenhuma");
			}
			break;
		case 2:
			// Trazer para frente"
			try {
				controller.ordemPrimitivaSel(modelo.getPrimitivas().size() - 1);
			} catch (ArrayIndexOutOfBoundsException  e) {
				Mensagem.println("Selecione primeiro uma primitiva!");
			}
			break;
		case 3:
			// "Enviar para traz"
			try {
				controller.ordemPrimitivaSel(0);
			} catch (ArrayIndexOutOfBoundsException  e) {
				Mensagem.println("Selecione primeiro uma primitiva!");
			}
			break;
		case 4:
			// "Trazer 1 para frente"
			try {
				if (controller.getPrimitivaSelecionada() != modelo.getPrimitivas().size() - 1) {
				controller.ordemPrimitivaSel(controller.getPrimitivaSelecionada() + 1);
				}
			} catch (ArrayIndexOutOfBoundsException  e) {
				Mensagem.println("Selecione primeiro uma primitiva!");
			}
			break;
		case 5:
			// "Enviar 1 para traz"
			try {
				if (controller.getPrimitivaSelecionada() != 0) {
				controller.ordemPrimitivaSel(controller.getPrimitivaSelecionada() - 1);
				}
			} catch (ArrayIndexOutOfBoundsException  e) {
				Mensagem.println("Selecione primeiro uma primitiva!");
			}
			break;
		case 6:
			// "Alterar cor de linha"
			ListenerCorLinha lC = new ListenerCorLinha(controller);
			MyMenu mCorLinha = new MyMenu("Opcoes de Cor de Linha");
			mCorLinha.add(new Opcao("Abrir Paleta de cores", lC, true));
			mCorLinha.add(new Opcao("Remover Cor", lC, true));
			mCorLinha.add(new Opcao("Voltar", null, true));
			mCorLinha.show();
			try {
				controller.setCorLinhaSelecionada();
			} catch (ArrayIndexOutOfBoundsException  e) {
				Mensagem.println("Selecione primeiro uma primitiva!");
			}
			break;
		case 7:
			// "Alterar espessura da linha";
			Mensagem.print("Digite um valor de espessura: ");
			try {
				controller.setEspessuraLinhaSelecionada(Keyboard.readInt());
			} catch (ArrayIndexOutOfBoundsException  e1) {
				Mensagem.println("Selecione primeiro uma primitiva!");
			}			
			break;
		case 8:
			//"Alterar cor de preenchimento"
			ListenerCorPreenchimento lCp = new ListenerCorPreenchimento(controller);
			MyMenu mCorPreenchimento = new MyMenu("Opcoes de Cor de Preenchimento");
			mCorPreenchimento.add(new Opcao("Abrir Paleta de cores", lCp, true));
			mCorPreenchimento.add(new Opcao("Remover Cor", lCp, true));
			mCorPreenchimento.add(new Opcao("Voltar", null, true));
			mCorPreenchimento.show();
			try {
				controller.setCorPreenchimentoSelecionada();
			} catch (ArrayIndexOutOfBoundsException  e) {
				Mensagem.println("Selecione primeiro uma primitiva!");
			}
			break;
		case 9:
			//"Pontos"
			try {
				controller.setPosSelecionada();
			}  catch (ArrayIndexOutOfBoundsException  e) {
				Mensagem.println("Selecione primeiro uma primitiva!");
			}
			break;
		case 10:
			try {
				controller.removerSelecionada();
			}  catch (ArrayIndexOutOfBoundsException  e) {
				Mensagem.println("Selecione primeiro uma primitiva!");
			}
			break;
		}
	}
}
