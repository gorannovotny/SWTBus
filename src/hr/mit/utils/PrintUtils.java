package hr.mit.utils;

import hr.mit.Starter;
import hr.mit.beans.Stavka;
import hr.mit.beans.Vozac;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PrintUtils {
	private static char[] reset = { 27, 64, 13 };
	private static char[] barcode = { 29, 107, 72, 15 };
	private static char[] duplo = { 27, 87, 2 };

	public static void print(Vozac vozac, List<Stavka> stavkaList) {
		if (new File(".print").exists()) {
			try {
			//	FileWriter out = new FileWriter("/dev/ttyS0");
                FileWriter out = new FileWriter(Starter.ComPortPrinter);
				out.write(reset);
				out.write(filter(createString(vozac, stavkaList)));
	
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println(createString(vozac, stavkaList));
	}

	private static String createString(Vozac vozac, List<Stavka> stavkaList) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy. HH:mm");
		StringBuffer sb = new StringBuffer();
		int BrKt = 0;
		for (Stavka stavka : stavkaList) {
			
			if (stavka.getJeZamjenska()) 
				BrKt = 0;
			else
	            BrKt = 1;
	            
			sb.append("AP d.d. Varazdin u stecaju\rGospodarska 56\rOIB: 51631089795\r\rPrijevoznik: AP d.d\r");
			sb.append(stavkaList.get(0).getRelacija() + "\r \r");
			sb.append("Broj karte: " + stavka.getBrojKarte() + "\r");
			sb.append("Vrsta karte       Popust  Cijena\r................................\r");
			if (stavka.getJeZamjenska()) // dodamo opis zamjenska
				sb.append("ZAMJENSKA KARTA" + "\r");
			sb.append(stavka.getDesc()+"\r"); // opis karte
			sb.append("................................\r");
			sb.append(String.format("Osnovica PDV 25%%         %7.2f\r", stavka.getNettoCijena().doubleValue()*BrKt ));
			sb.append(String.format("PDV 25%%                  %7.2f\r", stavka.getIznosPDV().doubleValue()*BrKt ));
			sb.append(String.format("Za platiti               %7.2f\r",  stavka.getProdajnaCijena().doubleValue()*BrKt));
			sb.append("\r");
			sb.append("Vozač: " + vozac.getSifra() + "\r");
			sb.append(sdf.format(new Date()) + "\r");
			sb.append("................................\r");
			if (stavka.getKarta().getStVoznji().equals(2) && (! stavka.getJeZamjenska())) {
				sb.append("\r");
				sb.append("\r");
				sb.append("\r");
				sb.append("................................\r");
				sb.append("Kupon za povratni smjer\r");
				sb.append("AP d.d. Varazdin u stecaju\rGospodarska 56\rOIB: 51631089795\r\rPrijevoznik: AP d.d\r");
				sb.append(stavkaList.get(0).getRelacija() + "\r \r");
				sb.append(sdf.format(new Date()) + "\r");
				sb.append(stavka.getRelacijaKontra() + "\r");
				sb.append("Broj karte:  " + stavka.getBrojKarte() + "\r");
				sb.append(String.format("Cijena kupona: %7.2f\r",  stavka.getCijenaVoznje()));
				sb.append("................................\r");
			}
		}
		sb.append(" \r \r");
		return sb.toString();
	}

	public static char[] filter(String in) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < in.length(); i++) {
			char a = in.charAt(i);
			if (a == 'č')
				a = 'c';
			else if (a == 'Č')
				a = 'C';
			else if (a == 'ć')
				a = 'c';
			else if (a == 'Ć')
				a = 'C';
			else if (a == 'ž')
				a = 'z';
			else if (a == 'Ž')
				a = 'Z';
			else if (a == 'š')
				a = 's';
			else if (a == 'Š')
				a = 'S';
			else if (a == 'đ')
				a = 'd';
			else if (a == 'Đ')
				a = 'D';
			sb.append(a);
		}
		return sb.toString().toCharArray();
	}

}
