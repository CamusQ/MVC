package com.atguigu.mvcapp.test;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.atguigu.mvcapp.dao.CriteriaCustomer;
import com.atguigu.mvcapp.dao.CustomerDAO;
import com.atguigu.mvcapp.dao.impl.CustomerDAOJdbcImpl;
import com.atguigu.mvcapp.domain.Customer;

class CustomerDAOJdbcImplTest {

	private CustomerDAO customerDao = new CustomerDAOJdbcImpl();
	
	@Test
	public void testGetForListWithCriteriaCustomer() {
		CriteriaCustomer cc = new CriteriaCustomer("k", null, null);
		List<Customer> customers = customerDao.getForListWithCriteriaCustomer(cc);
		System.out.println(customers);
	}

	@Test
	void testGetAll() {
		List<Customer> customers = customerDao.getAll();
		System.out.println(customers);
	}

	@Test
	void testSave() {
		Customer customer = new Customer();
		customer.setAddress("shanghao");
		customer.setName("Jerry");
		customer.setPhone("1372098654");
		
		customerDao.save(customer);
	}

	@Test
	void testGetInteger() {
		Customer cust = customerDao.get(1);
		System.out.println(cust);
	}

	@Test
	void testDelete() {
		customerDao.delete(1);
	}

	@Test
	void testGetCountWithName() {
		long countWithName = customerDao.getCountWithName("Jerry");
		System.out.println(countWithName);
	}

}
