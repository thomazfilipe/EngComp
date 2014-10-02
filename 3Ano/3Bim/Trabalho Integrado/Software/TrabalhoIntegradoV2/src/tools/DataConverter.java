package tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataConverter extends SimpleDateFormat {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static Date StringToDate(String data){
		Date nova = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			nova = sdf.parse(data);
			return nova;
		} catch (ParseException e) {
			return null;
		}
	}

}
