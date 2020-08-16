package model.dao;

import model.dao.impl.SellerDaoJDBC;

public class DaoFactory {

	// class responsible for initiated the Daos
	
	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC();
		
		// This will initiate a SellerDaoJDBC that implements the interface SellerDao. Returning a SellerDao
		// We'll be calling the this static method like that in the main program: SellerDao sellerDao = DaoFactory.createSellerDao();
	}
}
