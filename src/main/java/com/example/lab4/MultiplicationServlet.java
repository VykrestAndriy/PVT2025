package com.example.lab4;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "multiplicationServlet", value = "/multiply-table")
public class MultiplicationServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        int num = 0;
        try {
            num = Integer.parseInt(request.getParameter("num"));
        } catch (NumberFormatException e) {
            out.println("<h1>Помилка: Введено некоректне число.</h1>");
            out.println("<p><a href='multiply.html'>Спробувати ще раз</a></p>");
            return;
        }

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head><title>Таблиця множення для " + num + "</title>");
        out.println("<style>table, th, td {border: 1px solid black; border-collapse: collapse; padding: 8px;}</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Таблиця множення для числа " + num + "</h1>");

        out.println("<table>");
        for (int i = 1; i <= 10; i++) {
            out.println("<tr><td>" + num + " x " + i + "</td><td>=</td><td>" + (num * i) + "</td></tr>");
        }
        out.println("</table>");

        out.println("<p><a href='multiply.html'>Спробувати інше число</a></p>");
        out.println("<p><a href='/'>Назад до меню</a></p>");
        out.println("</body>");
        out.println("</html>");
    }
}