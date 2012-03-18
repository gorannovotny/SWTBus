package hr.mit.utils;

import hr.mit.beans.Stavka;
import hr.mit.beans.Vozac;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PrintUtils {
	private static char[] reset = { 27, 64, 13 };
	
	public static void print(Vozac vozac,List<Stavka> stavkaList) {
		try {
			FileWriter out = new FileWriter("/dev/ttyS0");
			out.write(reset);
			out.write(createString(vozac,stavkaList));
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		System.out.println(createString(vozac,stavkaList));
	}

	private static String createString(Vozac vozac,List<Stavka> stavkaList) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy. hh:mm");
		StringBuffer sb = new StringBuffer();
		sb.append("AP d.d. Varazdin\rGospodarska 56\rOIB: 4434343433\r\rBroj racuna: XXXXXXXXX\rPrijevoznik: AP d.d\r");
		sb.append("Relacija : " + stavkaList.get(0).getRelacija() + "\r\r");
		sb.append("Vrsta karte       Popust  Cijena\r--------------------------------\r");
		for (Stavka stavka : stavkaList) {
			sb.append(stavka.getDesc() + "\r");	
		}
		sb.append("--------------------------------\r");
		sb.append(String.format("Osnovica PDV 25%%         %7.2f\r", Stavka.getUkupno().multiply(new BigDecimal(0.75))));
		sb.append(String.format("PDV 25%%                  %7.2f\r", Stavka.getUkupno().multiply(new BigDecimal(0.25))));
		sb.append(String.format("Za platiti               %7.2f\r", Stavka.getUkupno()));
		sb.append("\r");
		sb.append("Vozač : " + vozac.getNaziv() + "\n" );
		sb.append(sdf.format(new Date()));
		sb.append("--------------------------------\r");

		
		return sb.toString();
	}
	
}
