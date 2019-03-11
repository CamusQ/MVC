package com.atguigu.mvcapp.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atguigu.mvcapp.dao.CriteriaCustomer;
import com.atguigu.mvcapp.dao.CustomerDAO;
import com.atguigu.mvcapp.dao.impl.CustomerDAOJdbcImpl;
import com.atguigu.mvcapp.domain.Customer;

@WebServlet("*.do")
public class CustomerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private CustomerDAO customerDAO = new CustomerDAOJdbcImpl();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

//	protected void doPost(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//		String method = request.getParameter("method");
//
//		switch (method) {
//			case "add":add(request, response);break;
//			case "query":query(request, response);break;
//			case "delete":delete(request, response);break;
//		}
//	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String servletPath = req.getServletPath();
		String methodName = servletPath.substring(1);

		methodName = methodName.substring(0, methodName.length() - 3);
//		System.out.println(methodName);

		try {
			Method method = getClass().getDeclaredMethod(methodName, HttpServletRequest.class,
					HttpServletResponse.class);
			method.invoke(this, req, resp);
		} catch (Exception e) {
//			System.out.println("�����ڸ÷���");
			resp.sendRedirect("error.jsp");
		}
	}

	private void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1.��ȡ������
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String address = request.getParameter("address");
		String phone = request.getParameter("phone");
		String oldName = request.getParameter("oldName");

		// 2.����name�Ƿ��Ѿ���ռ�ã�
		// 2.1�Ƚ�name �� oldName�Ƿ���ͬ������ͬ˵��name����
		// 2.2������ͬ������customerDAO��getCountWithName(String name)��ȡname�����ݿ����Ƿ����
		if (!oldName.equalsIgnoreCase(name)) {
			long count = customerDAO.getCountWithName(name);
			// 2.2.1������ֵ����0������Ӧupdatecustomer.jsp,ͨ��ת������Ӧnewcustomer.jsp
			if (count > 0) {
				/**
				 * 2.2.1Ҫ����updatecustomer.jspҳ����ʾһ��������Ϣ���û���name�Ѿ���ռ�ã�������ѡ��!
				 * ��ҳ����ͨ��request.getAttribute("Message")�ķ�ʽ����ʾ
				 */
				request.setAttribute("message", "�û���" + name + "�Ѿ���ռ�ã�������ѡ��!");

				/**
				 * 2.2.2 updatecustomer.jsp��ֵ���Ի���
				 * address,phone��ʾ�ύ�����µ�ֵ����name��ʾoldname���������ύ��name
				 */

				request.getRequestDispatcher("/updatecustomer.jsp").forward(request, response);
				return;
			}
		}

		// 3.�ѱ�������װ��Ϊһ��Customer ����customer
		Customer customer = new Customer(name, address, phone);
		customer.setId(Integer.parseInt(id));
		
		// 4.����CustomerDAO��save��Customer customer)ִ�б������
		customerDAO.update(customer);
		
		// 5.���¶���success.jsp����ʹ���ض�����Ա�������ظ��ύ����
		response.sendRedirect("query.do");
	}

	private void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String forwardPath = "/error.jsp";
		// 1.��ȡ����ID
		String idStr = request.getParameter("id");

		// 2.����CustomerDAO ��customerDAO.get(id) ��ȡ id ��Ӧ ��Customer ����customer
		try {
			Customer customer = customerDAO.get(Integer.parseInt(idStr));
			if (customer != null) {
				forwardPath = "/updatecustomer.jsp";
				request.setAttribute("customer", customer);
			}
		} catch (NumberFormatException e) {
		}
		// 3.��customer����request��

		// 4.��Ӧupdatecustomer.jsp ҳ�棺
		request.getRequestDispatcher(forwardPath).forward(request, response);
	}

	private void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idStr = request.getParameter("id");
		int id = 0;
//		try-catch���ã���ֹIdStr����תΪint��
//		������ת��id=0���޷������κ�ɾ��������

		try {
			id = Integer.parseInt(idStr);
			customerDAO.delete(id);
		} catch (Exception e) {
		}

		response.sendRedirect("query.do");

	}

	private void query(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		��ȡģ����ѯ���������
		String name = request.getParameter("name");
		String phone = request.getParameter("phone");
		String address = request.getParameter("address");

		CriteriaCustomer cc = new CriteriaCustomer(name, address, phone);

		// 1.�õ�Customer�ļ���
		List<Customer> customers = customerDAO.getForListWithCriteriaCustomer(cc);

		// 2.��customer�ļ��Ϸŵ�request��
		request.setAttribute("customers", customers);

		// 3.ת��ҳ�浽 index.jsp(����ʹ���ض���)
		request.getRequestDispatcher("/index.jsp").forward(request, response);

	}

	private void addCustomer(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 1.��ȡ������
		String name = request.getParameter("name");
		String phone = request.getParameter("phone");
		String address = request.getParameter("address");

		// 2.����name�Ƿ��Ѿ���ռ�ã�
		// 2.1����customerDAO��getCountWithName(String name)��ȡname�����ݿ����Ƿ����
		long count = customerDAO.getCountWithName(name);

		// 2.2������ֵ����0������Ӧnewcustomer.jsp
		if (count > 0) {
			/**
			 * 2.2.1Ҫ����newcustomer.jspҳ����ʾһ��������Ϣ���û���name�Ѿ���ռ�ã�������ѡ��
			 * ��ҳ����ͨ��request.getAttribute("Message")�ķ�ʽ����ʾ
			 */
			request.setAttribute("message", "�û���" + name + "�Ѿ���ռ�ã�������ѡ��!");
			/**
			 * 2.2.2 newcustomer.jsp��ֵ���Ի��� ͨ��value=<"<%= request.getParameter("name") ==
			 * null ? "" : request.getParameter("name") %>"
			 */
			request.getRequestDispatcher("/addCustomer.jsp").forward(request, response);
			return;

		}
		// 3.�ѱ�������װ��Ϊһ��Customer ����customer
		Customer customer = new Customer(name, address, phone);

		// 4.����CustomerDAO��save��Customer customer)ִ�б������
		customerDAO.save(customer);

		// 5.���¶���success.jsp����ʹ���ض�����Ա�������ظ��ύ����
		response.sendRedirect("success.jsp");// ������������ˢ��ʱ����ַ������
//		equest.getRequestDispatcher("/success.jsp").forward(request, response);//����һ������ˢ��ʱ��ַ�����ɵ�һ��
		/**
		 * ҳ��������Ҫ����������������ԣ�����ת��������������ض���
		 */
	}

}
