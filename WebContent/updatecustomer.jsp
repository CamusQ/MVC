<%@page import="com.atguigu.mvcapp.domain.Customer"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

	<%
		Object msg = request.getAttribute("message");
		if (msg != null) {
	%>
		<br>
		<font color="red"><%= msg %></font>
		<br>
		<br>
	<%
		}
		
		String id = null;
		String oldName = null;
		String name = null;
		String address = null;
		String phone = null;
		
		Customer customer = (Customer)request.getAttribute("customer");
		if(customer!=null){
			id = customer.getId() + "";
			oldName = customer.getName();
			name = customer.getName();
			address = customer.getAddress();
			phone = customer.getPhone();
		}else{
			id = request.getParameter("id");
			oldName = request.getParameter("oldName");
			name = request.getParameter("oldName");
			address = request.getParameter("address");
			phone = request.getParameter("phone");
		}
		
	%>

	<form action="update.do" method="post">
	
		<input type="hidden" name="id" value="<%= id %>"	/>
		<input type="hidden" name="oldName" value="<%= oldName %>"	/>
	
		<table>
			<tr>
				<td>CustomerName:</td>
				<td><input type="text" name="name"
					value="<%= name%>"></td>
			</tr>
			<tr>
				<td>Address:</td>
				<td><input type="text" name="address"
					value="<%= address%>"></td>
			</tr>
			<tr>
				<td>Phone:</td>
				<td><input type="text" name="phone"
					value="<%= phone%>"></td>
			</tr>
			<tr>
				<td colspan="2"><input type="submit" value="submit" /></td>
			</tr>
		</table>

	</form>

</body>
</html>