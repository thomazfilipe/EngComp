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

import tabs.TabRelatorio1;
import database.Db;

public class PesquisarListener implements ActionListener {

	private TabRelatorio1 tab;
	private Db conexao;

	public PesquisarListener(TabRelatorio1 tab) {
		this.tab = tab;
		this.conexao = tab.getConexao();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		//valida entradas
		if(tab.getHoraI().equals("") || tab.getHoraF().equals("") ||
				tab.getMinI().equals("") || tab.getMinF().equals("") ||
				tab.getSecI().equals("") || tab.getSecF().equals("")){
			JOptionPane.showMessageDialog(null, "Os dados fornecidos para a pesquisa são inválidos.", "Dados inválidos", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if(tab.getDataI() == null ||
				tab.getDataF() == null ||
				Integer.parseInt(tab.getHoraI()) > 23 || Integer.parseInt(tab.getHoraI()) < 0 ||
				Integer.parseInt(tab.getHoraF()) > 23 || Integer.parseInt(tab.getHoraF()) < 0 ||
				Integer.parseInt(tab.getMinI()) > 59 || Integer.parseInt(tab.getMinI()) < 0 ||
				Integer.parseInt(tab.getMinF()) > 59 || Integer.parseInt(tab.getMinF()) < 0 ||
				Integer.parseInt(tab.getSecI()) > 59 || Integer.parseInt(tab.getSecI()) < 0 ||
				Integer.parseInt(tab.getSecF()) > 59 || Integer.parseInt(tab.getSecF()) < 0)
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
		
		String comando = "SELECT codfluxo, quantidadea, quantidadeb, dataleitura "
				+ "FROM fluxo "
				+ "WHERE dataleitura >= '" + sdf.format(dataI) + " " + tab.getHoraI() + ":" + tab.getMinI() +":"+ tab.getSecI() +"'"
				+ "AND dataleitura <= '" + sdf.format(dataF) + " " + tab.getHoraF() + ":" + tab.getMinF() +":"+ tab.getSecF() +"'";
		ResultSet rs = conexao.consultar(comando);
		
		//monta o vetor das colunas da tabela
		String[] colunas = new String[] { "ID", "QtddA", "QtddB", "Data Leitura" };
		JTable tabela = new JTable();
		
		if (rs != null) {//se retornou resultado
			try {				
				//cria o modelo de dados
				DefaultTableModel model = new DefaultTableModel();
				model.setColumnIdentifiers(colunas);
				//insere linha por linha
				while (rs.next()) {
					String[] dados = new String[] {rs.getString("codfluxo"), rs.getString("quantidadea"), rs.getString("quantidadeb") ,
						rs.getString("dataleitura").replace(".0", "")};
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
