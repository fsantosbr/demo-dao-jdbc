package model.dao;

import java.util.List;

import model.entities.Department;

public interface DepartmentDao {

	void insert(Department obj); // Operation responsible for inserting an obj (of Department) in the database
	void update(Department obj); // Operation responsible for updating an obj (of Department) in the database
	void deleteById(Integer id); // Operation responsible for deleting something in the database by an id	
	Department findById(Integer id); // Operation responsible for searching in the database with this id and if it exist, return a Department obj, if not exist, return null
	List<Department> findAll(); // Operation responsible for returning all departments in the database
	
	
	
}
