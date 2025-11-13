package com.example.lab4;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "quoteServlet", value = "/quote")
public class QuoteServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");

        String quote = "Any fool can write code that a computer can understand. Good programmers write code that humans can understand";
        String author = "Martin Fowler";

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head><title>Цитата</title></head>");
            out.println("<body>");
            out.println("<h1>Цитата Мартіна Фаулера</h1>");
            out.println("<p style='font-style: italic; font-size: 1.2em;'>\"" + quote + "\"</p>");
            out.println("<p>— " + author + "</p>");
            out.println("<p><a href='/'>Назад до меню</a></p>");
            out.println("</body>");
            out.println("</html>");
        }
    }
}