package model.dao;

import java.util.List;

import model.entities.Seller;

public interface SellerDao {

	void insert(Seller obj); // Operation responsible for inserting an obj (of Seller) in the database
	void update(Seller obj); // Operation responsible for updating an obj (of Seller) in the database
	void deleteById(Integer id); // Operation responsible for deleting something in the database by an id	
	Seller findById(Integer id); // Operation responsible for searching in the database with this id and if it exist, return a Seller obj, if not exist, return null
	List<Seller> findAll(); // Operation responsible for returning all sellers in the database
	
}
