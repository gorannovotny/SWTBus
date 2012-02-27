package hr.mit.beans;

import hr.mit.utils.DbUtil;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Karta {
	private static ArrayList<Karta> kartaList = new ArrayList<Karta>();
	private Integer id;
	private String naziv;
	private Integer stVoznji;
	private Integer nacinDolocanjaCene;
	private Integer tarifniRazredID;
	private BigDecimal fiksnaCena;
	private Integer kmPogoja;
	private BigDecimal popustProcent;
	private Integer tipKarteID;

	
	public static Integer TARIFNI_DALJINAR = 1;
	public static Integer FIKSNA_CIJENA = 2;
	public static Integer FIKSNI_KILOMETRI = 3;
	public static Integer ZAMJENSKA_KARTA = 99;

	static {
		try {
			String sql = "Select a.id,b.Opis,stVoznji,NacinDolocanjaCene,TarifniRazredID,FiksnaCena,KMPogoja,PopustProcent,TipKarteID from PTKTVozneKarte a,PTKTTipiKarti b WHERE a.MobilnaProdaja = 1 AND a.TipKarteID = b.ID";
			ResultSet rs = DbUtil.getConnection().createStatement().executeQuery(sql);
			while (rs.next()) {
				kartaList.add(new Karta(rs.getInt("ID"),rs.getString("Opis"),rs.getInt("stVoznji"),rs.getInt("NacinDolocanjaCene"),rs.getInt("TarifniRazredID"),rs.getDouble("FiksnaCena"),rs.getInt("KmPogoja"),rs.getDouble("PopustProcent"),rs.getInt("TipKarteID")));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		kartaList.add(new Karta(Karta.ZAMJENSKA_KARTA,"Zamjenska karta",1,0,0,0,0,0.0,0));	
	}

	public Karta(int id, String naziv,int stVoznji, int nacinDolocanjaCene, int tarifniRazredID, double FiksnaCena, int kmPogoja,double popustProcent,int tip) {
		this.id = id;
		this.naziv = naziv;
		this.stVoznji = stVoznji;
		this.nacinDolocanjaCene = nacinDolocanjaCene;
		this.tarifniRazredID = tarifniRazredID;
		this.fiksnaCena = new BigDecimal(FiksnaCena);
		this.kmPogoja = kmPogoja;
		this.popustProcent = new BigDecimal(popustProcent);
		this.tipKarteID = tip;
	}

	public static String[] getList() {
		String[] l = new String[kartaList.size()];
		int x = 0;
		for (Karta v : kartaList) {
			l[x] = v.getNaziv();
			x++;
		}
		return l;
	}
	
	public static Karta get(int index) {
		return kartaList.get(index);
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

	public Integer getTipKarteID(){
		return tipKarteID;
	}
}
