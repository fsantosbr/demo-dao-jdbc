package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

	private Connection conn; // This class will have a dependency with the database connections
	
	// A constructor only to make the dependency
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	
	@Override
	public void insert(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE seller.Id = ?"
					);
			
			st.setInt(1, id);
			rs = st.executeQuery();
			
			// The st (ResultSet) will return a table with the information
			// after making the request.. we need to pass the information to classes (because we're working with object oriented)
			
			// testing if came some result. If yes, Instantiate the classes, If not, return null (there's no result for the id).
			if (rs.next()) {				
				Department dep = instantiateDepartment(rs);
				Seller obj = instantiateSeller(rs, dep);				
				return obj;
				
			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
			// we do not close the database connection here because we can use the connection in another method
		}
	}

	
	
	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller obj = new Seller();
		obj.setId(rs.getInt("Id")); // 'rs.getInt("Id")' gets an integer data (id), from the result stored in the rs by the field name "Id".
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setDepartment(dep);
		return obj;
	}


	private Department instantiateDepartment(ResultSet rs) throws SQLException {		
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId")); // 'rs.getInt("DepartmentId")' gets an integer data (id), from the result stored in the rs by the field name "DepartmentId".
		dep.setName(rs.getString("depName")); // 'rs.getString("depName")' gets an String data (Name), from the result stored in the rs by the field name (nickname) "depName".
		return dep;
	}
	
	
	@Override
	public List<Seller> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
