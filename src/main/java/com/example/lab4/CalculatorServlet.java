package com.example.lab4;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

@WebServlet(name = "calculatorServlet", value = "/calculator")
public class CalculatorServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            int n1 = Integer.parseInt(request.getParameter("n1"));
            int n2 = Integer.parseInt(request.getParameter("n2"));
            int n3 = Integer.parseInt(request.getParameter("n3"));
            String operation = request.getParameter("operation");

            double result = 0;
            String operationName = "";
            int[] numbers = {n1, n2, n3};

            if ("min".equals(operation)) {
                result = Arrays.stream(numbers).min().orElseThrow();
                operationName = "Мінімальне значення";
            } else if ("max".equals(operation)) {
                result = Arrays.stream(numbers).max().orElseThrow();
                operationName = "Максимальне значення";
            } else if ("avg".equals(operation)) {
                result = (double) (n1 + n2 + n3) / 3.0;
                operationName = "Середнє арифметичне";
            } else {
                operationName = "Невідома операція";
            }

            out.println("<!DOCTYPE html><html><head><title>Результат обчислення</title></head><body>");
            out.println("<h1>Результат обчислення</h1>");
            out.println("<p>Числа: " + n1 + ", " + n2 + ", " + n3 + "</p>");
            out.println("<p>Операція: **" + operationName + "**</p>");
            out.println("<p style='font-size: 1.5em; color: green;'>**Результат: " + String.format("%.2f", result) + "**</p>");
            out.println("<p><a href='calc.html'>Обчислити ще раз</a></p>");
            out.println("<p><a href='/'>Назад до меню</a></p>");
            out.println("</body></html>");

        } catch (NumberFormatException | NullPointerException e) {
            out.println("<h1>Помилка: Необхідно ввести числа та обрати дію.</h1>");
            out.println("<p><a href='calc.html'>Спробувати ще раз</a></p>");
        }
    }
}