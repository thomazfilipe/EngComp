package Menu;

public class Opcao {
	private String descricao;
	private boolean tipoSair;
	private ListenerMenu listener;
	private int id;
	
	public Opcao(String descricao, ListenerMenu listener){
		this.descricao = descricao;
		tipoSair = false;
		this.listener = listener;
	}
	
	public Opcao(String descricao, ListenerMenu listener, boolean saida){
		this.descricao = descricao;		
		this.listener = listener;
		tipoSair = saida;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void executar(){
		if(listener != null){
			listener.eventoMenu(id);
		}
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public boolean isTipoSair() {
		return tipoSair;
	}

	public void setTipoSair(boolean tipoSair) {
		this.tipoSair = tipoSair;
	}

	public ListenerMenu getListener() {
		return listener;
	}

	public void setListener(ListenerMenu listener) {
		this.listener = listener;
	}
	
	public String toString() {
		return String.format("%-2s", id) + "- " + descricao;
	}
}
