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
	private static char[] rotate = { 28, 73, 2 };

	public static void print(Vozac vozac, List<Stavka> stavkaList) {
		if (new File(".print").exists()) {
			try {
				// FileWriter out = new FileWriter("/dev/ttyS0");
				FileWriter out = new FileWriter(Starter.ComPortPrinter);
				out.write(reset);
				if ((Starter.PrintRotate != null) && (Starter.PrintRotate.equals("1")))
					out.write(filter(reversePrint(createString(vozac, stavkaList))));
				else
					out.write(filter(createString(vozac, stavkaList)));
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println(reversePrint(createString(vozac, stavkaList)));
	}

	public static void printPripremaPapira() {
		if (new File(".print").exists()) {
			try {
				FileWriter out = new FileWriter(Starter.ComPortPrinter);
				out.write(reset);
				if ((Starter.PrintRotate != null) && (Starter.PrintRotate.equals("1")))
					out.write(filter(reversePrint(pripremiPapir())));
				else
					out.write(filter(pripremiPapir()));
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println(pripremiPapir());
	}
	
    private static String pripremiPapir(){
		StringBuffer sb = new StringBuffer();
		sb.append("********************************\r");
		sb.append("AP d.d. Varazdin\r");
		sb.append("Gospodarska 56\r");
		sb.append("OIB: 51631089795\r");
		sb.append("\r");
		sb.append("Prijevoznik: AP d.d\r");
		sb.append("Vrijeme: " + (new SimpleDateFormat("dd.MM.yy\nHH:mm:ss").format(new Date())) + "\r");
		sb.append("--------------------------------\r");
		sb.append("--------------------------------\r");
		sb.append(" P R I P R E M A    P A P I R A \r");
		sb.append("--------------------------------\r");
		sb.append("--------------------------------\r");
		sb.append("********************************\r");
		sb.append(" \r \r \r");
		return sb.toString();
    } 	

	private static String createString(Vozac vozac, List<Stavka> stavkaList) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy. HH:mm");
		StringBuffer sb = new StringBuffer();
		int BrKt = 0;
		int listIndex = 0;
		double gotovinaKn = 0;

		double NettoCijena;
		double IznosPDV;
		double ZaPlatiti;

		boolean lhasnext;
		String ss;
		for (Stavka stavka : stavkaList) {
			if (stavka.getJePrelazna()) { // *** pocetni razmak
				sb.append("\r");
				sb.append("\r");
			}

			ZaPlatiti = stavka.getProdajnaCijena().doubleValue();
			IznosPDV = stavka.getIznosPDV().doubleValue();
			NettoCijena = ZaPlatiti - IznosPDV;

			if (stavka.getJeZamjenska() || stavka.getJePrelazna())
				BrKt = 0;
			else
				BrKt = 1;

			if (!stavka.getJeZamjenska())
				gotovinaKn = gotovinaKn + stavka.getProdajnaCijena().doubleValue();

			if (listIndex == 0)
				sb.append("AP d.d. Varazdin u stecaju\rGospodarska 56\rOIB: 51631089795\r\rPrijevoznik: AP d.d\r");

			// ss = stavkaList.get(0).getRelacija(); // relacija karte
			if (!stavka.getJePrelazna())
				ss = stavkaList.get(0).getRelacija();
			else
				ss = stavka.getRelacija();

			// **** za prijelaznu kartu moramo dodati opis i od slijedeceg prijelaza ****
			lhasnext = (listIndex < stavkaList.size() - 1);
			if (lhasnext) {
				if (stavkaList.get(listIndex + 1).getJePrelazna()) {
					ss = ss + stavkaList.get(listIndex + 1).getDodInfoRelacije();
					// za prijaleznu pribrojimo iznos osnovnoj
					ZaPlatiti = ZaPlatiti + stavkaList.get(listIndex + 1).getProdajnaCijena().doubleValue();
					IznosPDV = IznosPDV + stavkaList.get(listIndex + 1).getIznosPDV().doubleValue();
					NettoCijena = NettoCijena + (stavkaList.get(listIndex + 1).getProdajnaCijena().doubleValue() - stavkaList.get(listIndex + 1).getIznosPDV().doubleValue());
				}
			}
			sb.append(ss + "\r");
			/*
			 * sb.append(stavkaList.get(0).getRelacija()+ stavkaList.get(1).getDodInfoRelacije()+"\r");
			 */
			// if (stavka.jePrelazna())
			// sb.append(stavka.getDodInfoRelacije()+"\r");
			// sb.append("\r");
			sb.append("Broj karte: " + stavka.getBrojKarte() + "\r");
			sb.append("Vrsta karte       Popust  Cijena\r................................\r");
			/*
			 * if (stavka.getJeZamjenska()) // dodamo opis zamjenska sb.append("ZAMJENSKA KARTA" + "\r"); if (stavka.getJePrelazna()) // dodamo opis zamjenska sb.append("PRIJELAZNA KARTA" + "\r");
			 */
			if (stavka.getDodOpisKarte() != "")
				sb.append(stavka.getDodOpisKarte() + "\r");
			sb.append(stavka.getDesc() + "\r"); // opis karte
			sb.append("................................\r");
			sb.append(String.format("Osnovica PDV 25%%         %7.2f\r", NettoCijena * BrKt));
			sb.append(String.format("PDV 25%%                  %7.2f\r", IznosPDV * BrKt));
			sb.append(String.format("Za platiti               %7.2f\r", ZaPlatiti * BrKt));
			sb.append("\r");
			sb.append("Vozač: " + vozac.getSifra() + "\r");
			sb.append(sdf.format(new Date()) + "\r");
			sb.append("................................\r");
			if (stavka.getKarta().getStVoznji().equals(2) && (!stavka.getJeZamjenska())) {
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
				sb.append(String.format("Cijena kupona: %7.2f\r", stavka.getCijenaVoznje()));
				sb.append("................................\r");
			}
			listIndex++;
		}
		if (gotovinaKn != 0) {
			sb.append(String.format("PLAĆENO GOTOVINOM KN:    %7.2f\r", gotovinaKn));
		}
		sb.append(" \r \r \r");
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

	public static String reversePrint(String in) {
		StringBuffer out = new StringBuffer();
		String[] lines = in.split("\r");
		for (int i = lines.length - 1; i >= 0; i--) {
		 if ((i < lines.length - 3) || (lines[i].length() > 2)) 
			  out = out.append(lines[i] + "\r");
		}
		out = out.append(" \r \r \r");
		return out.toString();
	}

}
