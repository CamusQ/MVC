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
//			System.out.println("不存在该方法");
			resp.sendRedirect("error.jsp");
		}
	}

	private void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1.获取表单参数
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String address = request.getParameter("address");
		String phone = request.getParameter("phone");
		String oldName = request.getParameter("oldName");

		// 2.检验name是否已经被占用：
		// 2.1比较name 和 oldName是否相同，若相同说明name可用
		// 2.2若不相同，调用customerDAO的getCountWithName(String name)获取name在数据库中是否存在
		if (!oldName.equalsIgnoreCase(name)) {
			long count = customerDAO.getCountWithName(name);
			// 2.2.1若返回值大于0，则响应updatecustomer.jsp,通过转发来响应newcustomer.jsp
			if (count > 0) {
				/**
				 * 2.2.1要求在updatecustomer.jsp页面显示一个错误信息：用户名name已经被占用，请重新选择!
				 * 在页面上通过request.getAttribute("Message")的方式来显示
				 */
				request.setAttribute("message", "用户名" + name + "已经被占用，请重新选择!");

				/**
				 * 2.2.2 updatecustomer.jsp表单值可以回显
				 * address,phone显示提交表单的新的值，而name显示oldname，而不是提交的name
				 */

				request.getRequestDispatcher("/updatecustomer.jsp").forward(request, response);
				return;
			}
		}

		// 3.把表单参数封装成为一个Customer 对象customer
		Customer customer = new Customer(name, address, phone);
		customer.setId(Integer.parseInt(id));
		
		// 4.调用CustomerDAO的save（Customer customer)执行保存操作
		customerDAO.update(customer);
		
		// 5.重新定向到success.jsp，，使用重定向可以避免表单的重复提交问题
		response.sendRedirect("query.do");
	}

	private void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String forwardPath = "/error.jsp";
		// 1.获取请求ID
		String idStr = request.getParameter("id");

		// 2.调用CustomerDAO 的customerDAO.get(id) 获取 id 对应 的Customer 对象customer
		try {
			Customer customer = customerDAO.get(Integer.parseInt(idStr));
			if (customer != null) {
				forwardPath = "/updatecustomer.jsp";
				request.setAttribute("customer", customer);
			}
		} catch (NumberFormatException e) {
		}
		// 3.将customer放入request中

		// 4.响应updatecustomer.jsp 页面：
		request.getRequestDispatcher(forwardPath).forward(request, response);
	}

	private void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idStr = request.getParameter("id");
		int id = 0;
//		try-catch作用：防止IdStr不能转为int型
//		若不能转则id=0，无法进行任何删除操作。

		try {
			id = Integer.parseInt(idStr);
			customerDAO.delete(id);
		} catch (Exception e) {
		}

		response.sendRedirect("query.do");

	}

	private void query(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		获取模糊查询的请求参数
		String name = request.getParameter("name");
		String phone = request.getParameter("phone");
		String address = request.getParameter("address");

		CriteriaCustomer cc = new CriteriaCustomer(name, address, phone);

		// 1.得到Customer的集合
		List<Customer> customers = customerDAO.getForListWithCriteriaCustomer(cc);

		// 2.把customer的集合放到request中
		request.setAttribute("customers", customers);

		// 3.转发页面到 index.jsp(不能使用重定向)
		request.getRequestDispatcher("/index.jsp").forward(request, response);

	}

	private void addCustomer(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 1.获取表单参数
		String name = request.getParameter("name");
		String phone = request.getParameter("phone");
		String address = request.getParameter("address");

		// 2.检验name是否已经被占用：
		// 2.1调用customerDAO的getCountWithName(String name)获取name在数据库中是否存在
		long count = customerDAO.getCountWithName(name);

		// 2.2若返回值大于0，则响应newcustomer.jsp
		if (count > 0) {
			/**
			 * 2.2.1要求在newcustomer.jsp页面显示一个错误信息：用户名name已经被占用，请重新选择
			 * 在页面上通过request.getAttribute("Message")的方式来显示
			 */
			request.setAttribute("message", "用户名" + name + "已经被占用，请重新选择!");
			/**
			 * 2.2.2 newcustomer.jsp表单值可以回显 通过value=<"<%= request.getParameter("name") ==
			 * null ? "" : request.getParameter("name") %>"
			 */
			request.getRequestDispatcher("/addCustomer.jsp").forward(request, response);
			return;

		}
		// 3.把表单参数封装成为一个Customer 对象customer
		Customer customer = new Customer(name, address, phone);

		// 4.调用CustomerDAO的save（Customer customer)执行保存操作
		customerDAO.save(customer);

		// 5.重新定向到success.jsp，，使用重定向可以避免表单的重复提交问题
		response.sendRedirect("success.jsp");// 发了两个请求，刷新时，地址栏不变
//		equest.getRequestDispatcher("/success.jsp").forward(request, response);//发了一个请求，刷新时地址栏会变成第一个
		/**
		 * 页面里面需要用请求域里面的属性，必用转发，其余可以用重定向
		 */
	}

}
