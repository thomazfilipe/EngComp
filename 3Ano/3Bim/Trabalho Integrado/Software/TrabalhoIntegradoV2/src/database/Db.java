package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;


public class Db {
	private Connection conn;
	private Statement stmt;
	private ResultSet rs;
	private String usuario;
	private String senha;
	private String database;
	private boolean conectado;
	
	public Db(String database, String usuario, String senha){
		this.database = database;
		this.usuario = usuario;
		this.senha = senha;		
		this.conectado = false;
	}
	
	private void conectar(){
		//se não estiver conectado
		if(!conectado){
			try {
				//pega o driver de conexão com o mysql
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				//passo a string de conexão
		        String url = "jdbc:mysql://localhost:3306/"+database+"?user="+usuario+"&password="+senha;
		        conn = DriverManager.getConnection(url);
		        conectado = true;
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, 
						"Erro tentar conectar com o banco de dados: " + ex.getMessage(),
						"Erro", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	public ResultSet consultar(String consulta){
		
		conectar();
		try {
			//executa um select
			stmt = conn.createStatement();
			if (stmt.execute(consulta)) {
		        rs = stmt.getResultSet();
		    }
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, 
					"Erro tentar consultar o banco de dados: " + e.getMessage(),
					"Erro", JOptionPane.ERROR_MESSAGE);
		}
		
		return rs;//retorno o select
	}
	
	public void disconect(){
		if(conectado){//se estiver conectado no BD
			try {
				conn.close();
				conectado = false;
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, 
						"Erro tentar conectar desconectar do banco de dados: " + e.getMessage(),
						"Erro", JOptionPane.ERROR_MESSAGE);
			}		
		}
	}

	public int executar(String comando) {
		// TODO Auto-generated method stub
		conectar();
		try {
			stmt = conn.createStatement();
			
			return stmt.executeUpdate(comando);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, 
					"Erro tentar executar comando no banco de dados: " + e.getMessage(),
					"Erro", JOptionPane.ERROR_MESSAGE);
			return 0;
		}
	}

}
