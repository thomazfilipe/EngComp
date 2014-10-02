package tabs.listeners;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import tabs.TabRelatorio2;
import database.Db;

public class Pesquisar2Listener implements ActionListener {

	private TabRelatorio2 tab;
	private Db conexao;

	public Pesquisar2Listener(TabRelatorio2 tab) {
		this.tab = tab;
		this.conexao = tab.getConexao();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		//valida entradas
		if(tab.getDataI() == null ||
				tab.getDataF() == null)
		{
			JOptionPane.showMessageDialog(null, "Os dados fornecidos para a pesquisa são inválidos.", "Dados inválidos", JOptionPane.ERROR_MESSAGE);
			return;
		}
		//////////////////////////////////////////////////////////////
		//limpa a pesquisa atual
		tab.gePanelTabela().removeAll();
		//pega os dados inseridos
		Date dataI = tab.getDataI();
		Date dataF =  tab.getDataF();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		String comando = "SELECT nome, login, binario, valor, datamodificacao "
				+ "FROM operador op, regra re, configuracao conf "
				+ "WHERE conf.codoperador = op.codoperador "
				+ "AND conf.codregra = re.codregra "
				+ (!tab.getSelectedUser().equals("Todos") ? "AND login = '"+ tab.getSelectedUser() +"' " : "")
				+ "AND datamodificacao >= '" + sdf.format(dataI) + " " + tab.getHoraI() + ":" + tab.getMinI() +":"+ tab.getSecI() +"'"
				+ "AND datamodificacao <= '" + sdf.format(dataF) + " " + tab.getHoraF() + ":" + tab.getMinF() +":"+ tab.getSecF() +"'"
				+ "ORDER BY datamodificacao";
		ResultSet rs = conexao.consultar(comando);
		
		//monta o vetor das colunas da tabela
		String[] colunas = new String[] { "Comando", "Login", "Binário", "Valor" ,"Data do Comando" };
		JTable tabela = new JTable();
		
		if (rs != null) {//se retornou resultado
			try {				
				//cria o modelo de dados
				DefaultTableModel model = new DefaultTableModel();
				model.setColumnIdentifiers(colunas);
				sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				//insere linha por linha
				while (rs.next()) {
					String[] dados = new String[] {rs.getString("nome"), rs.getString("login"), rs.getString("binario") ,
						rs.getString("valor"), rs.getString("datamodificacao").replace(".0", "")};
					model.addRow(dados);					
				}				
				//insere na tabela
				tabela.setModel(model);
				JScrollPane scroll = new JScrollPane(tabela);
				scroll.setViewportView(tabela);
				scroll.setPreferredSize(new Dimension(640, 300));
				//adiciona a resposta na tab e atualiza
				tab.gePanelTabela().add(scroll);
				tab.gePanelTabela().validate();
				tab.validate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "Falha da conexão: " + e.getMessage(), "Conexão", JOptionPane.ERROR_MESSAGE);
			} 
		}
		
	}

}
