package regra;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;

import net.server.Servidor;
import database.Db;
import tabs.TabMonitor;
import tools.DataConverter;
import tools.Imprimir;
import tools.PopUpComandos;
import users.Usuarios;

public class Regras implements Observer {

	private int x1;
	private int y1;
	private boolean auto;
	private Db database;
	private Usuarios user;
	private Date dataAtual;
	private int idAtual;
	private int idRegra;
	private Servidor servidor;
	private boolean iniciar;

	public Regras(Db database, Usuarios user, Servidor servidor) {
		// pega o gerenciador de usuario, a conexão com o DB, e o servidor
		// criado
		this.database = database;
		this.user = user;
		this.servidor = servidor;

		servidor.addObserver(this);
		auto = false;// modo automatico

		// variavel que determina o envio imediato de uma regra após um comando
		// inicial
		iniciar = false;

		x1 = 10;// default X1 10%
		y1 = 10;// default Y1 10%
		idAtual = -1;// indica que esta com o sistema desligado
		idRegra = IDRegras.INATIVO;// sem regras
	}

	// /recebe a regra
	public String setRegra(TabMonitor tab) {
		String regra = tab.getComando();
		// valida comandos digitados
		String status = "Comando desconhecido";

		// /se o comando for 'setar X1=NN%' seta x1
		if (regra.startsWith("setar X1=") && regra.endsWith("%")) {
			// pega o valor setado
			String val = regra.substring(regra.indexOf('=') + 1,
					regra.lastIndexOf('%'));
			// verifica se é numero e altera
			try {
				int valInt = Integer.parseInt(val);
				// se o valor não estiver entre 0 e 2,147,483,647
				if (valInt < 0)
					throw new Exception();

				x1 = valInt;
				status = "X1 foi setado para: " + x1 + "%";
				user.guardaComm(IDRegras.ATTRIB_X1, new String(
						IDRegras.BIN_ATTRIB_X1 + x1));
			} catch (Exception e) {
				// se deu erro não faz nada
				status = "O valor informado a X1 e invalido";
			}
		}
		// /se o comando for 'setar Y1=NN%' seta y1
		else if (regra.startsWith("setar Y1=") && regra.endsWith("%")) {
			// pega o valor setado
			String val = regra.substring(regra.indexOf('=') + 1,
					regra.lastIndexOf('%'));
			// verifica se é numero e altera
			try {
				int valInt = Integer.parseInt(val);
				// se o valor não estiver entre 0 e 100
				if (valInt < 0)
					throw new Exception();

				y1 = valInt;
				status = "Y1 foi setado para: " + y1 + "%";
				user.guardaComm(IDRegras.ATTRIB_Y1, new String(
						IDRegras.BIN_ATTRIB_Y1 + y1));
			} catch (Exception e) {
				// se deu erro não faz nada
				status = "O valor informado a Y1 e invalido";
			}
		}
		// /se for comando para setar fluxo ON na via A ou B
		else if (regra.equals("VIA A=ON")) {
			// verifica se o sistema está ativo
			if (idRegra == IDRegras.INATIVO || idRegra == IDRegras.STANDBY) {
				status = "O sistema ainda não foi ligado";
			} else if (idRegra == IDRegras.VIA_B_ON) {
				status = "Coloque a VIA B em off antes de ativar A";
			}
			// se a regra já esta ativa
			else if (idRegra == IDRegras.VIA_A_ON) {
				status = "VIA A já esta aberta";
			} else {
				auto = false;
				servidor.sendMessege(IDRegras.BIN_A_ON);
				user.guardaComm(IDRegras.VIA_A_ON, IDRegras.BIN_A_ON);
				idRegra = IDRegras.VIA_A_ON;
				status = "VIA A sempre aberta";
			}

		} else if (regra.equals("VIA B=ON")) {
			if (idRegra == IDRegras.INATIVO || idRegra == IDRegras.STANDBY) {
				status = "O sistema ainda não foi ligado";
			} else if (idRegra == IDRegras.VIA_A_ON) {
				status = "Coloque a VIA A em off antes de ativar B";
			} else if (idRegra == IDRegras.VIA_B_ON) {
				status = "VIA B já esta aberta";
			} else {
				auto = false;
				servidor.sendMessege(IDRegras.BIN_B_ON);
				status = "VIA B sempre aberta";
				user.guardaComm(IDRegras.VIA_B_ON, IDRegras.BIN_B_ON);
				idRegra = IDRegras.VIA_B_ON;
			}
		}
		// /se for comando para setar OFF na via A ou B
		else if (regra.equals("VIA A=OFF")) {
			if (idRegra == IDRegras.INATIVO || idRegra == IDRegras.STANDBY) {
				status = "O sistema ainda não foi ligado";
			} else if (idRegra != IDRegras.VIA_A_ON) {
				status = "A Via A não está ativa";
			} else if (idRegra == IDRegras.VIA_A_OFF) {
				status = "O modo automatico já está ativo";
			} else {
				auto = true;
				status = "Modo automatico ativo";
				servidor.sendMessege(IDRegras.BIN_A_OFF);
				user.guardaComm(IDRegras.VIA_A_OFF, IDRegras.BIN_A_OFF);
			}
		} else if (regra.equals("VIA B=OFF")) {
			if (idRegra == IDRegras.INATIVO || idRegra == IDRegras.STANDBY) {
				status = "O sistema ainda não foi ligado";
			} else if (idRegra != IDRegras.VIA_B_ON) {
				status = "A Via B não está ativa";
			} else if (idRegra == IDRegras.VIA_B_OFF) {
				status = "O modo automatico já está ativo";
			} else {
				auto = true;
				status = "Modo automatico ativo";
				servidor.sendMessege(IDRegras.BIN_B_OFF);
				user.guardaComm(IDRegras.VIA_B_OFF, IDRegras.BIN_B_OFF);
			}
		}
		// /se for comando sistema ON regra inicial
		else if (regra.equals("sistema on")) {
			if (idRegra == IDRegras.ATIVO) {
				status = "O sistema já esta ativo";
			} else if (idRegra != IDRegras.STANDBY
					&& idRegra != IDRegras.INATIVO) {
				status = "O sistema já está ativo";
			} else {
				status = "Sistema supervisor ativo";
				servidor.sendMessege(IDRegras.BIN_ATIVO);
				user.guardaComm(IDRegras.ATIVO, IDRegras.BIN_ATIVO);
				idRegra = IDRegras.ATIVO;
				auto = true;
				iniciar = true;
			}
		}
		// /se for comando sistema OFF regra standby
		else if (regra.equals("sistema off")) {
			if (idRegra == IDRegras.STANDBY) {
				status = "O sistema já em stand-by";
			} else if (idRegra != IDRegras.STANDBY
					|| idRegra != IDRegras.INATIVO) {
				servidor.sendMessege(IDRegras.BIN_STANDBY);
				status = "Sistema supervisor em standby";
				auto = false;
				user.guardaComm(IDRegras.STANDBY, IDRegras.BIN_STANDBY);
				idRegra = IDRegras.STANDBY;
			} else {
				status = "O Sistema não esta ativo";
			}
		} else {
			// /se for comando de usuario
			String[] dados = regra.split(" ");
			// verificando sintaxe 'user usuario pass senha'
			if (dados[0].equals("user") && dados[2].equals("pass")
					&& (dados.length == 4 || dados.length == 5)) {
				if (dados[1].length() > 10 || dados[3].length() > 64) {
					status = "O login deve ter no maximo 10 caracteres, e a senha deve ter no maximo 64 caracteres";
				}
				// /se foi passado um atributo de tipo de usuario
				else if (dados.length == 5) {
					// /valida se o atributo é valido
					if (dados[4].equals("0") || dados[4].equals("1")) {
						// /se estiver tentando criar um administrador
						if (dados[4].equals("1")) {
							// /verifica se a conta atual é administrador
							if (user.isAdmin()) {
								status = user.userAdd(dados[1], dados[3], 1);
								user.guardaComm(IDRegras.ADD_OP, dados[1]);
							} else {
								status = "Você não possui permissão para criar um administrador.";
							}
						} else {// senão cria um usuario normal
							status = user.userAdd(dados[1], dados[3], 0);
							user.guardaComm(IDRegras.ADD_OP, dados[1]);
						}
					} else {
						// propriedade inválida
						status = "O tipo de Operador definido é invalido.";
					}
				} else {
					// adiciona usuario normal
					status = user.userAdd(dados[1], dados[3], 0);
					user.guardaComm(IDRegras.ADD_OP, dados[1]);
				}
			} else if (dados[0].equals("changeuser") && dados.length == 3) {
				// tenta trocar de usuario
				user.guardaComm(IDRegras.SWITCH_OP, dados[1]);
				if (user.login(dados[1], dados[2])) {
					status = "Você está agora como Operador [ " + dados[1]
							+ " ]";
					// seta o label na Tab do monitor o usuario atual
					tab.setLaberUser("Você está logado como [ " + dados[1]
							+ " ]");
				} else
					status = "Não foi possivel trocar de Operador";
			} else if (dados[0].equals("changepass") && dados.length == 3) {
				// tenta trocar a senha
				if (user.userUpdatePass(dados[1], dados[2])) {
					status = "Senha alterada com sucesso.";
					user.guardaComm(IDRegras.CHANGE_PASS,
							IDRegras.BIN_CHANGE_PASS);
				} else
					status = "Não foi possivel trocar a senha, senha atual incorreta.";
			} else if (dados[0].equals("deluser") && dados.length == 2) {
				// /verifica se a conta atual é administrador
				if (user.isAdmin()) {
					if (user.getUser().equals(dados[1])) {
						status = "Você não pode deletar um operador logado.";
					} else if (user.userDel(dados[1])) {
						user.guardaComm(IDRegras.DEL_OP, dados[1]);
						status = "Operador [ " + dados[1] + " ] foi deletado";
					} else
						status = "Operador invalido";
				} else {
					status = "Você não possui permissão para usar este comando.";
				}
				// /////////pega lista de Operadores
			} else if (dados[0].equals("getusers") && dados.length == 1) {
				// /verifica se a conta atual é administrador
				if (user.isAdmin()) {
					user.guardaComm(IDRegras.GET_USERS, IDRegras.BIN_GET_USERS);
					PopUpComandos cmdUsers = new PopUpComandos();
					cmdUsers.setSize(300, 200);
					java.util.List<String> lstOp = user.getUsers();

					for (int i = 0; i < lstOp.size(); i++) {
						cmdUsers.addLabels("", lstOp.get(i));
					}

					cmdUsers.ativar();

					status = " ";
				} else {
					status = "Você não possui permissão para usar este comando.";
				}
			}
		}

		return status;
	}

	public void update(Observable arg0, Object arg1) {
		boolean calcFluxo = false;
		// /pega o ultimo dado do sql na tabela fluxo
		String comando = "SELECT codfluxo, dataleitura "
				+ "FROM fluxo ORDER BY codfluxo DESC LIMIT 1";
		ResultSet rs = database.consultar(comando);
		try {
			if (rs != null) {// garante que tem dados a serem processados

				rs.next();

				if (rs.getDate("dataleitura") != null) {
					if (idAtual == -1) {
						idAtual = rs.getInt("codfluxo");
						dataAtual = DataConverter.StringToDate(rs
								.getString("dataleitura"));
					}
					// se o tempo entre da data de controle ea data do
					// ultimo dado inserido for maior que 60 segundos
					else if (DataConverter.StringToDate(
							rs.getString("dataleitura")).getTime()
							- dataAtual.getTime() >= 60000) {
						calcFluxo = true;
					}

				}
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,
					"Ocorreu um erro ao tentar consultar o banco de dados: "
							+ e.getMessage(), "Erro de consulta",
					JOptionPane.ERROR_MESSAGE);
		}

		// e se é para calcula a média de fluxo
		if (calcFluxo || iniciar) {
			iniciar = false;

			double fA = 0.0;
			double fB = 0.0;

			comando = "SELECT codfluxo, quantidadea, quantidadeb, dataleitura "
					+ "FROM fluxo " + "WHERE codfluxo >= " + idAtual;
			rs = database.consultar(comando);

			if (rs != null) {
				try {
					while (rs.next()) {
						// se for soma a quantidade do intervalo de TA e
						// TB
						fA += Double.parseDouble(rs.getString("quantidadea"));
						fB += Double.parseDouble(rs.getString("quantidadeb"));
						// se é o ultimo registro
						if (rs.isLast()) {
							idAtual = rs.getInt("codfluxo");
							dataAtual = DataConverter.StringToDate(rs
									.getString("dataleitura"));
						}
					}
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null,
							"Ocorreu um erro ao tentar consultar o banco de dados: "
									+ e.getMessage(), "Erro de consulta",
							JOptionPane.ERROR_MESSAGE);
				}
			}
			// /se o modo automatico estiver ativo
			if (auto) {
				// se fluxo A é x1% maior que fluxo B prioridade para A
				// regra TA
				if (fA > fB + (fB*(x1 /100))) {
					// envia a regra TA
					Imprimir.linha("DEFINIDO REGRA TA");
					servidor.sendMessege(IDRegras.BIN_PREF_A);
				}
				// se fluxo B é y1% maior que fluxo A prioridade para B
				// regra TB
				else if (fB > fA + (fA*(y1 /100))) {
					// envia a regra TB
					Imprimir.linha("DEFINIDO REGRA TB");
					servidor.sendMessege(IDRegras.BIN_PREF_B);
				}
				// senão prioridade igual em A e B regra TC
				else {
					// /envia regra TC
					servidor.sendMessege(IDRegras.BIN_NO_PREF);
					Imprimir.linha("DEFINIDO REGRA TC");
				}

				
				
				Imprimir.linha("X1=" + x1);
				Imprimir.linha("Y1=" + y1);
				Imprimir.linha("A: " + fA + " B: " + fB);
				Imprimir.linha("diferença entre A e B: " + fB +" > " +(fA + (fA*(y1 /100))));
				// desativa o calculo da média do fluxo
				calcFluxo = false;
			}

		}

	}

}
