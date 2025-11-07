<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Лабораторна робота №5</title>
    <%
        response.sendRedirect(request.getContextPath() + "/notebooks");
    %>
</head>
<body>
<p>Перенаправлення на <a href="${pageContext.request.contextPath}/notebooks">Список блокнотів...</a></p>
</body>
</html>