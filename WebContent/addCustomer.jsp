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
	%>

	<form action="addCustomer.do" method="post">
		<table>
			<tr>
				<td>CustomerName:</td>
				<td><input type="text" name="name"
					value="<%=request.getParameter("name") == null ? "" : request.getParameter("name")%>"></td>
			</tr>
			<tr>
				<td>Address:</td>
				<td><input type="text" name="address"
					value="<%=request.getParameter("address") == null ? "" : request.getParameter("address")%>"></td>
			</tr>
			<tr>
				<td>Phone:</td>
				<td><input type="text" name="phone"
					value="<%=request.getParameter("phone") == null ? "" : request.getParameter("phone")%>"></td>
			</tr>
			<tr>
				<td><input type="submit" value="submit" /></td>
			</tr>
		</table>

	</form>

</body>
</html>