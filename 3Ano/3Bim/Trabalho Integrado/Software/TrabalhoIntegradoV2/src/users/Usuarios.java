package users;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import database.Db;

public class Usuarios {
	
	private boolean isAdmin;
	private boolean logged;
	private Db database;
	private int id;
	private String usuario;
	private String senha;
	
	public Usuarios(Db database){
		this.setLogged(false);
		this.isAdmin = false;
		this.database = database;
		this.id = -1;
		this.usuario = "";
	}
	
	public boolean isAdmin(){
		return isAdmin;
	}
	
	public int getID(){
		return id;
	}
	
	public void guardaComm(int idComm, String val){
		String data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		String comando = "INSERT INTO configuracao (codoperador,codregra,valor,datamodificacao) "+
	"VALUES('"+id+"','"+idComm+"','"+val+"','"+data+"')";
		database.executar(comando);
	}
	
	public boolean login(String usuario, String senha){
		///pega dados do sql
		String comando = "SELECT codoperador, login, senha, tipo " + 
						 "FROM operador " +
						 "WHERE login='"+usuario+"' "+
						 "AND senha='"+getHash(senha)+"'";
		ResultSet rs = database.consultar(comando);
		if(rs != null){
			try {
				//contando linhas
				int total = 0;
				while(rs.next())
					total++;
				rs.beforeFirst();				
				
				if(total > 0){
					rs.next();
					id = rs.getInt("codoperador");
					this.usuario = rs.getString("login");
					this.senha = rs.getString("senha");
					if(rs.getInt("tipo") == 1)
						isAdmin = true;
					else
						isAdmin = false;
					
					setLogged(true);
				}
				else
					return false;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else
			return false;
		
		return true;
	}
	
	public String userAdd(String login, String senha, int tipo){
		if(!isAdmin)
			return "Este operador não possui permissão.";
		
		String comando = "SELECT login " + 
				 "FROM operador " +
				 "WHERE login='"+login+"' ";
		
		ResultSet rs = database.consultar(comando);
		if(rs != null){
			try {
				//contando linhas
				int total = 0;
				while(rs.next())
					total++;
				rs.beforeFirst();				
				
				if(total > 0){
					return "O login utilizado já existe!";
				}
			}
			catch(Exception e){
				return "Erro ao consultar o banco de dados: "+ e.getMessage();
			}
		}
		else
		{
			return "Ocorreu um erro ao tentar verificar a existencia do login!";
		}
		
		comando = "INSERT INTO operador (login,senha,tipo) VALUES('"+login+"','"+getHash(senha)+"','"+tipo+"')";
		database.executar(comando);
		return "Operador [ "+ login +"] adicionado com sucesso!";
	}
	
	public boolean userDel(String dados){		
		String comando = "DELETE FROM operador WHERE login='"+dados+"'";
		
		return database.executar(comando) == 1 ? true : false;
	}
	
	public boolean userUpdatePass(String oldpass, String newpass){		
		if(!senha.equals(getHash(oldpass))){
			return false;
		}
		
		String comando = "UPDATE operador "
				+ "SET senha='"+ getHash(newpass)+"' "
				+ "WHERE codoperador="+ id;
		database.executar(comando);
		return true;
	}
	
	public List<String> getUsers(){
		
		List<String> result = new ArrayList<>();
		///pega dados do sql
		String comando = "SELECT codoperador, login " + 
						 "FROM operador";
		ResultSet rs = database.consultar(comando);
		try {
			while(rs.next()){
				result.add(rs.getString("codoperador") +"|"+rs.getString("login"));
			}
		} catch (SQLException e) {
			return null;
		}
		
		return result;
	}
	
	public void logoff(){
		setLogged(false);
		id = -1;
		isAdmin =false;
	}
	
	private String getHash(String password){		 
        MessageDigest md;
        byte byteData[] = null;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes());
			 
	        byteData = md.digest();
		} catch (NoSuchAlgorithmException e) {
			JOptionPane.showMessageDialog(null,
					"Não foi possivel gerar o Hash da senha: " + e.getMessage(),
					"Erro",
					JOptionPane.ERROR_MESSAGE);
		}        
 
        //convert the byte to hex format
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
 
        return sb.toString();
	}

	public String getUser() {
		return usuario;
	}

	public boolean isLogged() {
		return logged;
	}

	public void setLogged(boolean logged) {
		this.logged = logged;
	}
}
