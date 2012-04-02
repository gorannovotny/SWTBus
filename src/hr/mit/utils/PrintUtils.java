package hr.mit.utils;

import hr.mit.beans.Stavka;
import hr.mit.beans.Vozac;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PrintUtils {
	private static char[] reset = { 27, 64, 13 };
	private static char[] barcode = { 29,107,72,15 };

	public static void print(Vozac vozac,List<Stavka> stavkaList) {
		try {
			FileWriter out = new FileWriter("/dev/ttyS0");
			out.write(reset);
			out.write(filter(createString(vozac,stavkaList)));
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
		sb.append("AP d.d. Varazdin\nGospodarska 56\nOIB: 4434343433\n\nBroj racuna: 734234429\nPrijevoznik: AP d.d\n");
		sb.append("Relacija : " + stavkaList.get(0).getRelacija() + "\n\n");
		sb.append("Vrsta karte       Popust  Cijena\n--------------------------------\n");
		for (Stavka stavka : stavkaList) {
			sb.append(stavka.getDesc() + "\n");	
		}
		sb.append("--------------------------------\n");
		double osnovica = Stavka.getUkupno().doubleValue();
		
		sb.append(String.format("Osnovica PDV 25%%         %7.2f\n",osnovica*.75));
		sb.append(String.format("PDV 25%%                  %7.2f\n", osnovica*0.25));
		sb.append(String.format("Za platiti               %7.2f\n", osnovica));
		sb.append("\n");
		sb.append("Vozač : " + vozac.getNaziv() + "\n" );
		sb.append(sdf.format(new Date())+"\n");
		sb.append("--------------------------------\n");
		for (Stavka stavka : stavkaList) {
			if (stavka.getKarta().getStVoznji().equals(1)) {
				sb.append("Talon: "+ stavka.getRelacija()+"\n");
				sb.append("Vrsta karte: "+ stavka.getKarta().getNaziv() + "\n");
				sb.append("Broj karte: " + stavka.getBrojKarte() + "\n");
				sb.append("--------------------------------\n");
			}
			if (stavka.getKarta().getStVoznji().equals(2)) {
				sb.append("Talon: "+ stavka.getRelacija()+"\n");
				sb.append("Vrsta karte: "+ stavka.getKarta().getNaziv() + "\n");
				sb.append("Broj karte: " + stavka.getBrojKarte() + "\n");
				sb.append("--------------------------------\n");
				sb.append("Talon: "+ stavka.getRelacijaKontra()+"\n");
				sb.append("Vrsta karte: "+ stavka.getKarta().getNaziv() + "\n");
				sb.append("Broj karte: " + stavka.getBrojKarte() + "\n");
				sb.append("--------------------------------\n");
			}
		}
		sb.append(" \n \n");	
		return sb.toString();
	}
	
	private static String filter(String in){
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < in.length(); i++) {
			char a = in.charAt(i);
			if (a == 'č')  a = 'c';
			else if (a == 'Č') a = 'C';
			else if (a == 'ć') a = 'c';
			else if (a == 'Ć') a = 'C';
			else if (a == 'ž') a = 'z';
			else if (a == 'Ž') a = 'Z';
			else if (a == 'š') a = 's';
			else if (a == 'Š') a = 'S';
			else if (a == 'đ') a = 'd';
			else if (a == 'Đ') a = 'D';
			sb.append(a);
		}
		return sb.toString();
	}
	
}
