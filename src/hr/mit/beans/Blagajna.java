package hr.mit.beans;

import java.math.BigDecimal;

public class Blagajna {
	private BigDecimal saldo = BigDecimal.ZERO;
	
	public BigDecimal getSaldo() {
		return saldo;
	}
	
	public void add(BigDecimal iznos){
		saldo = saldo.add(iznos);
	}
}
