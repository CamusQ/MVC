package com.atguigu.mvcapp.dao.impl;

import java.util.List;

import com.atguigu.mvcapp.dao.CriteriaCustomer;
import com.atguigu.mvcapp.dao.CustomerDAO;
import com.atguigu.mvcapp.domain.Customer;

public class CustomerDAOImpl implements CustomerDAO {

	@Override
	public List<Customer> getForListWithCriteriaCustomer(CriteriaCustomer cc) {
		System.out.println("getForListWithCriteriaCustomer");

		return null;
	}

	@Override
	public List<Customer> getAll() {
		System.out.println("List");

		return null;
	}

	@Override
	public void save(Customer customer) {
		System.out.println("save");
	}

	@Override
	public Customer get(Integer id) {
		System.out.println("get");

		return null;
	}

	@Override
	public void delete(Integer id) {
		System.out.println("delete");

	}

	@Override
	public void update(Customer customer) {
		System.out.println("update");

	}

	@Override
	public long getCountWithName(String name) {
		System.out.println("getCountWithName");

		return 0;
	}

}
