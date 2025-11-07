<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${pageTitle}</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .container { display: flex; gap: 30px; }
        .sidebar { width: 250px; }
        .main-content { flex-grow: 1; }
        h2 { border-bottom: 2px solid #ccc; padding-bottom: 5px; }
        .form-group { margin-bottom: 10px; }
        .filter-menu ul { list-style: none; padding: 0; }
        .filter-menu li { margin-bottom: 5px; }
    </style>
</head>
<body>

<h1>${pageTitle}</h1>

<div class="container">

    <div class="sidebar">

        <h2>Операції CRUD</h2>

        <form action="${pageContext.request.contextPath}/notebooks" method="POST">
            <h3>Додати блокнот</h3>
            <div class="form-group">
                <label>Фірма:</label>
                <input type="text" name="manufacturerName" required>
            </div>
            <div class="form-group">
                <label>Шифр:</label>
                <input type="text" name="notebookNameCode" required>
            </div>
            <div class="form-group">
                <label>Сторінок:</label>
                <input type="number" name="pageCount" required>
            </div>
            <div class="form-group">
                <label>Обкладинка:</label>
                <select name="coverType">
                    <option value="тверда">Тверда</option>
                    <option value="м’яка">М’яка</option>
                </select>
            </div>
            <div class="form-group">
                <label>Країна:</label>
                <input type="text" name="country" required>
            </div>
            <div class="form-group">
                <label>Тираж:</label>
                <input type="number" name="circulation" required>
            </div>
            <div class="form-group">
                <label>Розкладка:</label>
                <select name="pageLayout">
                    <option value="клітинка">Клітинка</option>
                    <option value="лінійка">Лінійка</option>
                    <option value="порожня">Порожня</option>
                </select>
            </div>
            <input type="hidden" name="action" value="add">
            <button type="submit">Додати</button>
        </form>

        <hr>

        <h2>Фільтри та звіти</h2>
        <div class="filter-menu">
            <ul>
                <li><a href="${pageContext.request.contextPath}/notebooks?action=all">Показати всі блокноти</a></li>
                <li><a href="${pageContext.request.contextPath}/notebooks?action=aggregateCountryCount">Кількість по країнах</a></li>
                <li><a href="${pageContext.request.contextPath}/notebooks?action=aggregateManufacturerCount">Кількість по виробниках</a></li>
                <li><a href="${pageContext.request.contextPath}/notebooks?action=maxCountry">Країна з max. кількістю</a></li>
                <li><a href="${pageContext.request.contextPath}/notebooks?action=minManufacturer">Виробник з min. кількістю</a></li>
            </ul>

            <h4>Фільтри за властивостями</h4>
            <ul>
                <li><a href="${pageContext.request.contextPath}/notebooks?action=filterByCover&value=тверда">Тверда обкладинка</a></li>
                <li><a href="${pageContext.request.contextPath}/notebooks?action=filterByCover&value=м’яка">М'яка обкладинка</a></li>
                <li><a href="${pageContext.request.contextPath}/notebooks?action=filterByLayout&value=клітинка">Розкладка: Клітинка</a></li>
                <li><a href="${pageContext.request.contextPath}/notebooks?action=filterByLayout&value=лінійка">Розкладка: Лінійка</a></li>
            </ul>

            <h4>Фільтр за країною</h4>
            <form action="${pageContext.request.contextPath}/notebooks" method="GET">
                <input type="text" name="value" placeholder="Назва країни" required>
                <input type="hidden" name="action" value="filterByCountry">
                <button type="submit">Фільтр</button>
            </form>

            <h4>Фільтр за сторінками (min-max)</h4>
            <form action="${pageContext.request.contextPath}/notebooks" method="GET">
                <input type="number" name="min" placeholder="Від" required>
                <input type="number" name="max" placeholder="До" required>
                <input type="hidden" name="action" value="filterByPages">
                <button type="submit">Фільтр</button>
            </form>

            <h4>Фільтр за тиражем (min-max)</h4>
            <form action="${pageContext.request.contextPath}/notebooks" method="GET">
                <input type="number" name="min" placeholder="Від" required>
                <input type="number" name="max" placeholder="До" required>
                <input type="hidden" name="action" value="filterByCirculation">
                <button type="submit">Фільтр</button>
            </form>
        </div>
    </div>

    <div class="main-content">

        <c:choose>
            <c:when test="${not empty aggregationResult}">

                <%-- Якщо це багаторядкова агрегація (List<Object[]>) --%>
                <c:if test="${aggregationResult.size() > 1 || (aggregationResult.size() == 1 && aggregationResult[0].size() > 1)}">
                    <table border="1">
                        <thead>
                        <tr>
                            <th>Назва</th>
                            <th>Кількість</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="item" items="${aggregationResult}">
                            <tr>
                                <td>${item[0]}</td>
                                <td>${item[1]}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:if>

                <%-- Якщо це однорядковий результат (Object[]), наприклад, maxCountry --%>
                <c:if test="${aggregationResult.size() == 2 && aggregationResult.getClass().isArray()}">
                    <p><strong>Результат:</strong> ${aggregationResult[0]} (Кількість: ${aggregationResult[1]})</p>
                </c:if>

            </c:when>

            <c:when test="${not empty notebooksList}">
                <table border="1">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Виробник</th>
                        <th>Шифр</th>
                        <th>Сторінок</th>
                        <th>Обкладинка</th>
                        <th>Країна</th>
                        <th>Тираж</th>
                        <th>Розкладка</th>
                        <th>Дії</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="notebook" items="${notebooksList}">
                        <tr>
                            <td>${notebook.id}</td>
                            <td>${notebook.manufacturerName}</td>
                            <td>${notebook.notebookNameCode}</td>
                            <td>${notebook.pageCount}</td>
                            <td>${notebook.coverType}</td>
                            <td>${notebook.country}</td>
                            <td>${notebook.circulation}</td>
                            <td>${notebook.pageLayout}</td>
                            <td>
                                    <%-- Форма для видалення --%>
                                <form action="${pageContext.request.contextPath}/notebooks" method="POST" style="display:inline;">
                                    <input type="hidden" name="id" value="${notebook.id}">
                                    <input type="hidden" name="action" value="delete">
                                    <button type="submit">Видалити</button>
                                </form>
                                    <%-- Посилання для оновлення --%>
                                <button onclick="document.getElementById('updateForm${notebook.id}').style.display='block'">Редагувати</button>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>

                <c:forEach var="notebook" items="${notebooksList}">
                    <div id="updateForm${notebook.id}" style="display:none; border: 1px solid #ccc; padding: 10px; margin-top: 10px;">
                        <h3>Редагувати блокнот ID: ${notebook.id}</h3>
                        <form action="${pageContext.request.contextPath}/notebooks" method="POST">
                            <input type="hidden" name="id" value="${notebook.id}">
                            <input type="hidden" name="action" value="update">
                            <p>Фірма: <input type="text" name="manufacturerName" value="${notebook.manufacturerName}" required></p>
                            <p>Шифр: <input type="text" name="notebookNameCode" value="${notebook.notebookNameCode}" required></p>
                            <p>Сторінок: <input type="number" name="pageCount" value="${notebook.pageCount}" required></p>
                            <p>Обкладинка: <input type="text" name="coverType" value="${notebook.coverType}" required></p>
                            <p>Країна: <input type="text" name="country" value="${notebook.country}" required></p>
                            <p>Тираж: <input type="number" name="circulation" value="${notebook.circulation}" required></p>
                            <p>Розкладка: <input type="text" name="pageLayout" value="${notebook.pageLayout}" required></p>
                            <button type="submit">Зберегти зміни</button>
                            <button type="button" onclick="document.getElementById('updateForm${notebook.id}').style.display='none'">Скасувати</button>
                        </form>
                    </div>
                </c:forEach>
            </c:when>

            <c:otherwise>
                <p>Дані не знайдено або база даних порожня.</p>
            </c:otherwise>
        </c:choose>
    </div>
</div>

</body>
</html>