package hr.mit.beans;

import hr.mit.utils.DbUtil;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Karta {
	private static ArrayList<Karta> kartaListMobilna = new ArrayList<Karta>();
	private static ArrayList<Karta> kartaListZamjenska = new ArrayList<Karta>();
	private Integer id;
	private String naziv;
	private Integer stVoznji;
	private Integer nacinDolocanjaCene;
	private Integer tarifniRazredID;
	private BigDecimal fiksnaCena;
	private Integer kmPogoja;
	private BigDecimal popustProcent;
	private Integer tipKarteID;
	private BigDecimal faktorCene;

	public static Integer TARIFNI_DALJINAR = 1;
	public static Integer FIKSNA_CIJENA = 2;
	public static Integer FIKSNI_KILOMETRI = 3;
	public static Integer RUCNI_UNOS = 4;

	static {
		try {
			String sql = "Select a.id,a.kratkiOpis,stVoznji,NacinDolocanjaCene,TarifniRazredID,FiksnaCena,KMPogoja,PopustProcent,TipKarteID,FaktorCene from PTKTVozneKarte a WHERE MobilnaProdaja = '1' ORDER BY sifra";
			ResultSet rs = DbUtil.getConnection().createStatement().executeQuery(sql);
			while (rs.next()) {
				kartaListMobilna.add(new Karta(rs.getInt("ID"), rs.getString("kratkiOpis"), rs.getInt("stVoznji"), rs.getInt("NacinDolocanjaCene"), rs.getInt("TarifniRazredID"), rs.getDouble("FiksnaCena"), rs.getInt("KmPogoja"), rs
						.getDouble("PopustProcent"), rs.getInt("TipKarteID"), rs.getDouble("FaktorCene")));
			}
			rs.close();
			sql = "Select a.id,a.kratkiOpis,stVoznji,NacinDolocanjaCene,TarifniRazredID,FiksnaCena,KMPogoja,PopustProcent,TipKarteID,FaktorCene from PTKTVozneKarte a WHERE ZamjenskaKarta = '1' ORDER BY sifra";

			rs = DbUtil.getConnection().createStatement().executeQuery(sql);
			while (rs.next()) {
				kartaListZamjenska.add(new Karta(rs.getInt("ID"), rs.getString("kratkiOpis"), rs.getInt("stVoznji"), rs.getInt("NacinDolocanjaCene"), rs.getInt("TarifniRazredID"), rs.getDouble("FiksnaCena"), rs.getInt("KmPogoja"), rs
						.getDouble("PopustProcent"), rs.getInt("TipKarteID"), rs.getDouble("FaktorCene")));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// kartaList.add(new
		// Karta(Karta.ZAMJENSKA_KARTA,"Zamjenska karta",1,0,0,0,0,0.0,0));
	}

	public Karta(int id, String naziv, int stVoznji, int nacinDolocanjaCene, int tarifniRazredID, double FiksnaCena, int kmPogoja, double popustProcent, int tip, double faktorCene) {
		this.id = id;
		this.naziv = naziv;
		this.stVoznji = stVoznji;
		this.nacinDolocanjaCene = nacinDolocanjaCene;
		this.tarifniRazredID = tarifniRazredID;
		this.fiksnaCena = new BigDecimal(FiksnaCena);
		this.kmPogoja = kmPogoja;
		this.popustProcent = new BigDecimal(popustProcent);
		this.tipKarteID = tip;
		this.faktorCene = new BigDecimal(faktorCene);
	}

//	public static String[] getListMobilna() {
//		String[] l = new String[kartaList.size()];
//		int x = 0;
//		for (Karta v : kartaList) {
//			l[x] = v.getNaziv();
//			x++;
//		}
//		return l;
//	}
//	
//	public static String[] getListZamjenska() {
//		String[] l = new String[kartaList.size()];
//		int x = 0;
//		for (Karta v : kartaList) {
//			l[x] = v.getNaziv();
//			x++;
//		}
//		return l;
//	}
	

	public static Karta getByID(int id) {
		for (Karta v : kartaListMobilna) {
			if (v.getId().equals(id))
				return v;
		}
		for (Karta v : kartaListZamjenska) {
			if (v.getId().equals(id))
				return v;
		}
		return null;
	}

	public static Karta getMobilna(int index) {
		return kartaListMobilna.get(index);
	}
	public static Karta getZamjenska(int index) {
		return kartaListZamjenska.get(index);
	}

	public Integer getId() {
		return id;
	}

	public String getNaziv() {
		return naziv;
	}

	public Integer getNacinDolocanjaCene() {
		if (nacinDolocanjaCene == null)
			return Karta.TARIFNI_DALJINAR;
		else
			return nacinDolocanjaCene;
	}

	public Integer getTarifniRazredID() {
		return tarifniRazredID;
	}

	public BigDecimal getFiksnaCena() {
		return fiksnaCena;
	}

	public Integer getKmPogoja() {
		return kmPogoja;
	}

	public Integer getStVoznji() {
		return stVoznji;
	}

	public BigDecimal getPopustProcent() {
		return popustProcent;
	}

	public Integer getTipKarteID() {
		return tipKarteID;
	}

	public BigDecimal getFaktorCene() {
		return faktorCene;
	}

	public static List<String> getArrayListMobilna() {
		List<String> l = new ArrayList<String>();
		for (Karta v : kartaListMobilna) {
			l.add(v.getNaziv());
		}
		return l;
	}

	public static List<String> getArrayListZamjenska() {
		List<String> l = new ArrayList<String>();
		for (Karta v : kartaListZamjenska) {
			l.add(v.getNaziv());
		}
		return l;
	}

}
