package com.example.lab4;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "guessingGameServlet", value = "/guess-number")
public class GuessingGameServlet extends HttpServlet {
    private static final int MIN_RANGE = 0;
    private static final int MAX_RANGE = 100;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        session.setAttribute("min", MIN_RANGE);
        session.setAttribute("max", MAX_RANGE);
        session.removeAttribute("lastGuess");

        displayGame(response, MIN_RANGE, MAX_RANGE, "Загадайте число від " + MIN_RANGE + " до " + MAX_RANGE + ", і натисніть одну з кнопок.");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();

        if (session.getAttribute("min") == null || session.getAttribute("max") == null) {
            doGet(request, response);
            return;
        }

        int min = (int) session.getAttribute("min");
        int max = (int) session.getAttribute("max");
        int currentGuess;

        String action = request.getParameter("action");
        String message;

        if (session.getAttribute("lastGuess") == null || action == null) {
            currentGuess = min + (max - min) / 2;
        } else {
            int lastGuess = (int) session.getAttribute("lastGuess");

            if ("greater".equals(action)) {
                min = lastGuess + 1;
                session.setAttribute("min", min);
            } else if ("less".equals(action)) {
                max = lastGuess - 1;
                session.setAttribute("max", max);
            } else if ("equals".equals(action)) {
                message = "Я вгадав! Ваше число — **" + lastGuess + "**! 🎉";
                displayEndGame(response, message);
                return;
            }
            currentGuess = min + (max - min) / 2;
        }

        if (min > max) {
            message = "Ви, схоже, десь помилились. Натисніть **Нова гра**.";
            displayEndGame(response, message);
            return;
        }

        message = "Моє припущення: Число більше, менше чи дорівнює **" + currentGuess + "**?";
        session.setAttribute("lastGuess", currentGuess);

        displayGame(response, min, max, message);
    }

    private void displayGame(HttpServletResponse response, int min, int max, String message) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html><html><head><title>Гра \"Вгадай число\"</title></head><body>");
        out.println("<h1>Гра \"Вгадай число\"</h1>");
        out.println("<p>Діапазон: [" + min + " - " + max + "]</p>");
        out.println("<p style='font-size: 1.2em; color: blue;'>" + message + "</p>");

        out.println("<form action='/guess-number' method='post'>");
        out.println("<button type='submit' name='action' value='greater'>Моє число БІЛЬШЕ</button> &nbsp;");
        out.println("<button type='submit' name='action' value='less'>Моє число МЕНШЕ</button> &nbsp;");
        out.println("<button type='submit' name='action' value='equals'>Моє число ДОРІВНЮЄ</button>");
        out.println("</form>");

        out.println("<p><a href='/guess-number'>Нова гра</a> | <a href='/'>Назад до меню</a></p>");
        out.println("</body></html>");
    }

    private void displayEndGame(HttpServletResponse response, String message) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html><html><head><title>Гра \"Вгадай число\"</title></head><body>");
        out.println("<h1>Гра \"Вгадай число\" - Завершено</h1>");
        out.println("<p style='font-size: 1.4em; color: green;'>**" + message + "**</p>");

        out.println("<p><a href='/guess-number'>Нова гра</a> | <a href='/'>Назад до меню</a></p>");
        out.println("</body></html>");
    }
}