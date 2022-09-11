package com.luv2code.springdemo.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.luv2code.springdemo.entity.Customer;

@Repository
public class CustomerDAOImpl implements CustomerDAO {

	// Need to inject the SessionFactory so that the DAO can communicate with the Database.
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
//	@Transactional
	public List<Customer> getCustomers() {
		
		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
//		// create a query
//		Query<Customer> theQuery = currentSession.createQuery("from Customer", Customer.class);
		
		// for a query that sorts by name
		Query<Customer> theQuery = currentSession.createQuery("from Customer order by lastName", Customer.class);
		
		// execute query and get the ResultList
		List<Customer> customers = theQuery.getResultList();
		
		// return results
		return customers;
	}

	@Override
	public void saveCustomer(Customer theCustomer) {
		
		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// save the customer
		currentSession.saveOrUpdate(theCustomer);
	}

	@Override
	public Customer getCustomer(int theId) {

		// get current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// now retrieve/read data from database using the primary key
		Customer theCustomerToRead = currentSession.get(Customer.class, theId);
		
		return theCustomerToRead;
	}

	@Override
	public void deleteCustomer(int theId) {
		
		// get the current hibernate session
		Session currentSession = sessionFactory.getCurrentSession();
		
		// delete the object with primary key
		Query theQuery = currentSession.createQuery("delete from Customer where id =:customerId");
		theQuery.setParameter("customerId", theId);
		
		theQuery.executeUpdate();
		
	}

	@Override
	public List<Customer> searchCustomers(String theSearchName) {
		
		// get the current session
		Session currentSession = sessionFactory.getCurrentSession();
		
		Query theQuery = null;
		
		
		// only search if search field is not empty
		if(theSearchName != null && theSearchName.trim().length()>0) {
			
			// search for the firstName or lastName
			theQuery = currentSession.createQuery("from Customer where lower(firstName) like :theName or lower(lastName) like :theName", Customer.class);
			theQuery.setParameter("theName", "%" + theSearchName.toLowerCase() + "%");
		}
			// else nothing to search
		else {
			theQuery = currentSession.createQuery("from Customer", Customer.class);
		}
		
		List<Customer> customers = theQuery.getResultList();
		
		
		return customers;
	}
	
}
