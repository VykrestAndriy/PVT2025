<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Управління Продажами (ЛР 6)</title>
    <style>
        body { font-family: Arial, sans-serif; display: flex; }
        .sidebar { width: 350px; padding: 15px; border-right: 1px solid #ccc; background-color: #f9f9f9; }
        .main-content { flex-grow: 1; padding: 20px; }
        h3 { border-bottom: 2px solid #000; padding-bottom: 5px; margin-top: 15px; }
        form, p { margin-bottom: 10px; }
        input[type="text"], input[type="number"], input[type="date"], select { padding: 5px; margin-right: 10px; border: 1px solid #ccc; }
        button, input[type="submit"] { padding: 8px 12px; cursor: pointer; background-color: #007bff; color: white; border: none; border-radius: 4px; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #e9ecef; }
        .form-group { margin-bottom: 10px; }
    </style>
</head>
<body>

<div class="sidebar">
    <h2>Продажі 🛒</h2>
    <c:if test="${not empty errorMessage}"><p style="color: red;">${errorMessage}</p></c:if>
    <c:if test="${not empty successMessage}"><p style="color: green;">${successMessage}</p></c:if>

    <hr>

    <h3>Додати Новий Запис</h3>

    <h4>Продавець</h4>
    <form action="${pageContext.request.contextPath}/sales" method="POST" class="form-group">
        <input type="hidden" name="action" value="saveSeller">
        <input type="text" name="name" placeholder="Ім'я" required><br>
        <input type="text" name="phone" placeholder="Телефон"><br>
        <input type="text" name="email" placeholder="Email"><br>
        <button type="submit">Додати Продавця</button>
    </form>

    <h4>Покупець</h4>
    <form action="${pageContext.request.contextPath}/sales" method="POST" class="form-group">
        <input type="hidden" name="action" value="saveCustomer">
        <input type="text" name="name" placeholder="Ім'я" required><br>
        <input type="text" name="phone" placeholder="Телефон"><br>
        <input type="text" name="email" placeholder="Email"><br>
        <button type="submit">Додати Покупця</button>
    </form>

    <h4>Товар</h4>
    <form action="${pageContext.request.contextPath}/sales" method="POST" class="form-group">
        <input type="hidden" name="action" value="saveProduct">
        <input type="text" name="name" placeholder="Назва товару" required><br>
        <input type="number" name="price" step="0.01" placeholder="Ціна" required><br>
        <button type="submit">Додати Товар</button>
    </form>

    <h4>Угода</h4>
    <form action="${pageContext.request.contextPath}/sales" method="POST" class="form-group">
        <input type="hidden" name="action" value="saveSale">
        <input type="number" name="sellerId" placeholder="ID Продавця" required><br>
        <input type="number" name="customerId" placeholder="ID Покупця" required><br>
        <input type="number" name="productId" placeholder="ID Товару" required><br>
        <input type="date" name="saleDate" required><br>
        <button type="submit">Додати Угоду</button>
    </form>

    <hr>

    <h3>Звіти та Агрегації</h3>
    <p><a href="${pageContext.request.contextPath}/sales?action=allSales">Показати всі Угоди</a></p>
    <p><a href="${pageContext.request.contextPath}/sales?action=allSellers">Показати всіх Продавців</a></p>
    <p><a href="${pageContext.request.contextPath}/sales?action=allCustomers">Показати всіх Покупців</a></p>
    <p><a href="${pageContext.request.contextPath}/sales?action=allProducts">Показати всі Товари</a></p>

    <hr>

    <h4>Агрегації</h4>
    <p><a href="${pageContext.request.contextPath}/sales?action=successfulSeller">Самий успішний Продавець (Max Сума)</a></p>
    <p><a href="${pageContext.request.contextPath}/sales?action=successfulCustomer">Самий успішний Покупець (Max Сума)</a></p>
    <p><a href="${pageContext.request.contextPath}/sales?action=avgSale">Середня сума Покупки</a></p>
    <p><a href="${pageContext.request.contextPath}/sales?action=demandedProduct">Товар, що найбільше користується попитом</a></p>

    <hr>

    <h4>Фільтри Угод</h4>
    <form action="${pageContext.request.contextPath}/sales" method="GET" class="form-group">
        <input type="hidden" name="action" value="salesByDate">
        <label>По конкретній даті:</label><br>
        <input type="date" name="date" required>
        <button type="submit">Фільтр</button>
    </form>

    <form action="${pageContext.request.contextPath}/sales" method="GET" class="form-group">
        <input type="hidden" name="action" value="salesByRange">
        <label>По діапазону дат:</label><br>
        <input type="date" name="startDate" required>
        <input type="date" name="endDate" required>
        <button type="submit">Фільтр</button>
    </form>

    <form action="${pageContext.request.contextPath}/sales" method="GET" class="form-group">
        <input type="hidden" name="action" value="salesBySeller">
        <label>По Продавцю (ID):</label><br>
        <input type="number" name="sellerId" placeholder="ID Продавця" required>
        <button type="submit">Фільтр</button>
    </form>
</div>

<div class="main-content">
    <h2>Результати та Список</h2>

    <c:choose>
        <c:when test="${not empty singleResult}">
            <h3>Результат Звіту:</h3>
            <p>Середня сума покупки: <strong><fmt:formatNumber value="${singleResult}" type="currency" currencySymbol="грн"/></strong></p>
        </c:when>

        <c:when test="${not empty aggregationResult}">
            <h3>Результат Звіту:</h3>
            <p>
                <c:choose>
                    <c:when test="${aggregationResult[0] == null}">Немає даних для агрегації.</c:when>
                    <c:otherwise>
                        <strong>${aggregationResult[0]}</strong> (Сума/Кількість: <c:out value="${aggregationResult[1]}"/>)
                    </c:otherwise>
                </c:choose>
            </p>
        </c:when>

        <c:when test="${not empty sellersList}">
            <h3>Список Продавців</h3>
            <table>
                <thead><tr><th>ID</th><th>Ім'я</th><th>Телефон</th><th>Email</th><th>Дії</th></tr></thead>
                <tbody>
                <c:forEach var="s" items="${sellersList}">
                    <tr>
                        <td>${s.id}</td>
                        <td>${s.name}</td>
                        <td>${s.phone}</td>
                        <td>${s.email}</td>
                        <td>
                            <form action="${pageContext.request.contextPath}/sales" method="POST" style="display:inline;">
                                <input type="hidden" name="id" value="${s.id}">
                                <input type="hidden" name="entityType" value="seller">
                                <input type="hidden" name="action" value="delete">
                                <button type="submit">Видалити</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:when>

        <c:when test="${not empty customersList}">
            <h3>Список Покупців</h3>
            <table>
                <thead><tr><th>ID</th><th>Ім'я</th><th>Телефон</th><th>Email</th><th>Дії</th></tr></thead>
                <tbody>
                <c:forEach var="c" items="${customersList}">
                    <tr>
                        <td>${c.id}</td>
                        <td>${c.name}</td>
                        <td>${c.phone}</td>
                        <td>${c.email}</td>
                        <td>
                            <form action="${pageContext.request.contextPath}/sales" method="POST" style="display:inline;">
                                <input type="hidden" name="id" value="${c.id}">
                                <input type="hidden" name="entityType" value="customer">
                                <input type="hidden" name="action" value="delete">
                                <button type="submit">Видалити</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:when>

        <c:when test="${not empty productsList}">
            <h3>Список Товарів</h3>
            <table>
                <thead><tr><th>ID</th><th>Назва</th><th>Ціна</th><th>Дії</th></tr></thead>
                <tbody>
                <c:forEach var="p" items="${productsList}">
                    <tr>
                        <td>${p.id}</td>
                        <td>${p.name}</td>
                        <td><fmt:formatNumber value="${p.price}" type="currency" currencySymbol="грн"/></td>
                        <td>
                            <form action="${pageContext.request.contextPath}/sales" method="POST" style="display:inline;">
                                <input type="hidden" name="id" value="${p.id}">
                                <input type="hidden" name="entityType" value="product">
                                <input type="hidden" name="action" value="delete">
                                <button type="submit">Видалити</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:when>

        <c:when test="${not empty salesList}">
            <h3>Список Угод</h3>
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Продавець</th>
                    <th>Покупець</th>
                    <th>Товар</th>
                    <th>Ціна</th>
                    <th>Дата</th>
                    <th>Дії</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="s" items="${salesList}">
                    <tr>
                        <td>${s.id}</td>
                        <td>${s.seller.name}</td>
                        <td>${s.customer.name}</td>
                        <td>${s.product.name}</td>
                        <td><fmt:formatNumber value="${s.product.price}" type="currency" currencySymbol="грн"/></td>
                        <td>${s.saleDate}</td>
                        <td>
                            <form action="${pageContext.request.contextPath}/sales" method="POST" style="display:inline;">
                                <input type="hidden" name="id" value="${s.id}">
                                <input type="hidden" name="entityType" value="sale">
                                <input type="hidden" name="action" value="delete">
                                <button type="submit">Видалити</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:when>

        <c:otherwise>
            <p>Для відображення даних, будь ласка, виберіть дію в меню.</p>
            <c:if test="${empty salesList && empty sellersList && empty productsList && empty customersList}">
                <p>База даних може бути порожньою. Спробуйте додати записи через форми.</p>
            </c:if>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>