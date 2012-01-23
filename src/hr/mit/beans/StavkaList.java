package hr.mit.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class StavkaList {

	List<Stavka> stavke = new ArrayList<Stavka>();

	public String[] getList() {
		ArrayList<String> x = new ArrayList<String>();
		for (Stavka s : stavke) {
			x.add(s.getDescription());
		}
		return x.toArray(new String[0]);
	}

	public Stavka get(Integer index) {
		return stavke.get(index);
	}

	public void add(Stavka s) {
		stavke.add(s);
	}

	public void remove(Integer index) {
		stavke.clear();
	}

	public void clear() {
		stavke.clear();
	}

	
	public BigDecimal getUkupno() {
		BigDecimal x = BigDecimal.ZERO;
		for (Stavka s : stavke) {
			x = x.add(s.getCijena());
		}
		return x;
	}

}
