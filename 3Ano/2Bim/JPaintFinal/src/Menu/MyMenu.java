package Menu;
import java.util.ArrayList;
import java.util.List;

import Ferramentas.Keyboard;
import Ferramentas.Mensagem;

public class MyMenu {

	private String titulo = "Menu Principal";
	private String prompt = "Escolha: ";
	private List<Opcao> opcoes = new ArrayList<Opcao>();
	private String erro = "Opcao invalida!";

	public MyMenu(){		
	}

	public MyMenu(String titulo){
		this.titulo = titulo;
	}
	
	public MyMenu(String titulo, String prompt) {
		this.titulo = titulo;
		this.prompt = prompt;
	}
	
	public MyMenu(String titulo, String prompt, String erro){
		this.titulo = titulo;
		this.prompt = prompt;
		this.erro = erro;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public String getErro() {
		return erro;
	}

	public void setErro(String erro) {
		this.erro = erro;
	}
	
	public void add(Opcao opcao){
		opcoes.add(opcao);
		opcao.setId(opcoes.size());
	}
	
	public void add(String descricao, boolean saida){
		Opcao novo = new Opcao(descricao, null, saida);
		add(novo);
	}
	
	public void add(String descricao, ListenerMenu listener){
		Opcao novo = new Opcao(descricao, listener);
		add(novo);
	}
	
	public void add(String descricao, ListenerMenu listener, boolean saida){
		Opcao novo = new Opcao(descricao, listener, saida);
		add(novo);
	}
	
	public void show() {
		while (true) {
			try {
				Mensagem.println(this);
				int op = Keyboard.readInt();
				if (op > 0 && op <= opcoes.size()) {
					Opcao opcao = opcoes.get(op - 1);
					opcao.executar();
					if (opcao.isTipoSair()) {
						break;
					}
				} else {
					Mensagem.println(erro);
				}
			} catch(NumberFormatException e) {
				Mensagem.println(erro);
			}			
		}
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(titulo).append("\n\n");
		for (Opcao op : opcoes) {
			sb.append(op).append("\n");
		}
		sb.append("\n").append(prompt);
		return sb.toString();
	}

	public void update(int pos, Opcao opcao){
		opcoes.set(pos, opcao);
	}
	
	public void update(int pos, String descricao){
		Opcao temp = opcoes.get(pos);
		temp.setDescricao(descricao);
		opcoes.set(pos, temp);
	}
}
