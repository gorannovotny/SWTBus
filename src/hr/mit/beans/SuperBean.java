package hr.mit.beans;

import hr.mit.utils.DbUtil;

import java.sql.Connection;
import java.util.ArrayList;

public class SuperBean {
	protected ArrayList<String> nazivList = new ArrayList<String>();
	protected ArrayList<Integer> idList = new ArrayList<Integer>();

	protected Connection con = DbUtil.getConnection();
	
	public String[] getList() {
		return nazivList.toArray(new String[0]);
	}

	public Integer getID(int selectionIndex) {
		if (selectionIndex >= 0)
			return idList.get(selectionIndex);
		else
			return new Integer(-1);
	}
}
