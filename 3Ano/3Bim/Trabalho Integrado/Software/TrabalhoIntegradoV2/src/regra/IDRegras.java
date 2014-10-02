package regra;

public interface IDRegras {
	//ID das regras
	int PREF_VIA_A  = 1;
	int PREF_VIA_B  = 2;
	int NO_PREF     = 3;
	int VIA_A_ON    = 4;
	int VIA_B_ON    = 5;
	int VIA_A_OFF   = 6;
	int VIA_B_OFF   = 7;
	int STANDBY     = 8;
	int ATIVO       = 9;
	int INATIVO     = 10;
	int ATTRIB_X1   = 11;
	int ATTRIB_Y1   = 12;
	int ADD_OP 	    = 13;
	int SWITCH_OP   = 14;
	int CHANGE_PASS = 15;
	int DEL_OP 	    = 16;
	int GET_USERS   = 17;
	
	//Binarios
	String BIN_PREF_A      = "1001";
	String BIN_PREF_B      = "1010";
	String BIN_NO_PREF     = "1011";
	String BIN_A_ON        = "1100";
	String BIN_B_ON        = "1101";
	String BIN_A_OFF       = "";
	String BIN_B_OFF       = "";
	String BIN_STANDBY     = "1110";
	String BIN_ATIVO       = "1110";
	String BIN_INATIVO     = "0110";
	String BIN_ATTRIB_X1   = "";
	String BIN_ATTRIB_Y1   = "";
	String BIN_ADD_OP      = "";
	String BIN_SWITCH_OP   = "";
	String BIN_CHANGE_PASS = "";
	String BIN_DEL_OP      = "";
	String BIN_GET_USERS   = "";
}
