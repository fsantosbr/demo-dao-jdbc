package model.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO seller "
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			// prepareStatement(sql_code, Statement.RETURN_GENERATED_KEYS) is an overload of the same method.
			// Statement.RETURN_GENERATED_KEYS = Will return the new key created
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new Date(obj.getBirthDate().getTime()));
				// st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
				// works in both ways. Just need to import "import java.sql.Date"
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			}
			else {
				// If we try to make an update and no rows are affected, it means something went wrong
				throw new DbException("Unexpected error! No rows affected!");
			}	
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	
	
	@Override
	public void update(Seller obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE seller "
					+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
					+ "WHERE Id = ?");
				
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new Date(obj.getBirthDate().getTime())); // st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime())); - works in both ways. Just need to import "import java.sql.Date"
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			st.setInt(6, obj.getId());
			
			st.executeUpdate();
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
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
		// In order to understand why we use each cod here or there, check the findByDepartment() method. There's all explanations there.	
		
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "ORDER BY Name");
			
			rs = st.executeQuery();
				
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			
			while (rs.next()) {				
				Department dep = map.get(rs.getInt("DepartmentId"));
				if (dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
					// .put(hey, value) will add a value in the Map
				}
				
				Seller obj = instantiateSeller(rs, dep);				
				list.add(obj);
			}
			return list;
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


	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE DepartmentId = ? "
					+ "ORDER BY Name");					
					
			
			st.setInt(1, department.getId());
				// st.setInt(index, value) = we replace with position of the "?" and the real value expected in the .prepareStatement() method
			
			rs = st.executeQuery();
				// The st (ResultSet) will return a table with the information
			
			// after making the request.. we need to pass the information to classes (because we're working with object oriented)
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
				// we're using Map to avoid repetitions for Department classes. Remember, Map doesn't allow repetition values
				// The key will be the department id. And the value will be the Department class
			
			while (rs.next()) {				
				Department dep = map.get(rs.getInt("DepartmentId"));
				// "rs.getInt("DepartmentId")" will bring an int value from the ResultSet as an arg to the .get() method for map
				// As there's nothing in the map yet, the dep variable will be null (won't receive a value).
				
				if (dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
					// .put(hey, value) will add a value in the Map
				}
				
				Seller obj = instantiateSeller(rs, dep);				
				list.add(obj);
				
			}
			return list;
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

	
}
