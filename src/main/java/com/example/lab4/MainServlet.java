package com.example.lab4;

import com.example.lab4.model.LaptopModel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "mainServlet", urlPatterns = {"/", "/manufacturer/*"})
public class MainServlet extends HttpServlet {

    private final List<LaptopModel> NEWS_LIST = Arrays.asList(
            new LaptopModel("Нове партнерство", "Компанія уклала угоду про співпрацю з великим розробником програмного забезпечення."),
            new LaptopModel("Перемога в рейтингу", "Наші ноутбуки визнані найкращими за інноваційність у 2024 році.")
    );

    private final List<LaptopModel> MODEL_LIST = Arrays.asList(
            new LaptopModel("Zenbook 13", "Ультрабук з OLED-дисплеєм для мобільної роботи."),
            new LaptopModel("ROG Strix G15", "Потужний ігровий ноутбук з високою частотою оновлення екрана."),
            new LaptopModel("VivoBook Flip", "Гнучкий ноутбук-трансформер 2-в-1 для навчання та розваг.")
    );

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();

        boolean isRootRequest = requestURI.equals(contextPath + "/") || requestURI.equals(contextPath);

        if (isRootRequest) {
            displayMainMenu(request, out);
        } else if (requestURI.startsWith(contextPath + "/manufacturer")) {
            String pathInfo = request.getPathInfo();
            if (pathInfo != null) {
                if (pathInfo.startsWith("/info")) {
                    displayManufacturerInfo(request, out);
                } else if (pathInfo.startsWith("/news")) {
                    displayNews(request, out);
                } else if (pathInfo.startsWith("/models")) {
                    displayModels(request, out);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Сторінка виробника не знайдена");
                }
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Сторінка не знайдена. Файл " + requestURI + " не знайдено.");
        }
    }

    private void displayMainMenu(HttpServletRequest request, PrintWriter out) {
        String contextPath = request.getContextPath();

        out.println("<!DOCTYPE html><html><head><title>Лабораторна Робота №4: Меню</title></head><body>");
        out.println("<h1>Лабораторна Робота №4: Меню</h1>");

        out.println("<h2>Завдання 1-4: Приклади Сервлетів</h2>");
        out.println("<ul>");
        out.println("<li><a href='" + contextPath + "/quote'>Завдання 1: Цитата Мартіна Фаулера</a></li>");
        out.println("<li><a href='" + contextPath + "/multiply.html'>Завдання 2: Таблиця множення</a></li>");
        out.println("<li><a href='" + contextPath + "/guess-number'>Завдання 3: Гра \"Вгадай число\"</a></li>");
        out.println("<li><a href='" + contextPath + "/calc.html'>Завдання 4: Обчислення 3-х чисел (Мін/Макс/Сер)</a></li>");
        out.println("</ul>");

        out.println("<hr>");
        out.println("<h2>Завдання 5: Веб-застосунок про Виробника (Статичні дані)</h2>");
        out.println("<ul>");
        out.println("<li><a href='" + contextPath + "/manufacturer/info'>Загальна інформація</a></li>");
        out.println("<li><a href='" + contextPath + "/manufacturer/news'>Новини</a></li>");
        out.println("<li><a href='" + contextPath + "/manufacturer/models'>Моделі ноутбуків</a></li>");
        out.println("</ul>");

        out.println("</body></html>");
    }

    private void displayManufacturerInfo(HttpServletRequest request, PrintWriter out) {
        String contextPath = request.getContextPath();
        String manufacturerName = "Глобальний Виробник Ноутбуків";
        String country = "Міжнародна";
        String employeeCount = "Понад 100,000";
        String shortInfo = "Ця компанія є світовим лідером у виробництві високотехнологічного комп'ютерного обладнання, включаючи інноваційні лінійки ноутбуків для бізнесу та геймінгу.";

        out.println("<!DOCTYPE html><html><head><title>Загальна інформація</title>");
        out.println("<style>.info-table td:first-child { font-weight: bold; width: 250px; }</style>");
        out.println("</head><body>");
        out.println("<h1>" + manufacturerName + ": Загальна інформація</h1>");

        out.println("<table class='info-table'>");
        out.println("<tr><td>Назва виробника:</td><td>" + manufacturerName + "</td></tr>");
        out.println("<tr><td>Країна (штаб-квартира):</td><td>" + country + "</td></tr>");
        out.println("<tr><td>Кількість співробітників:</td><td>" + employeeCount + "</td></tr>");
        out.println("</table>");

        out.println("<h2>Коротка інформація</h2>");
        out.println("<p>" + shortInfo + "</p>");

        out.println("<p><a href='" + contextPath + "/'>**Повернутися до меню**</a></p>");
        out.println("</body></html>");
    }

    private void displayNews(HttpServletRequest request, PrintWriter out) {
        String contextPath = request.getContextPath();
        List<LaptopModel> newsList = NEWS_LIST;

        out.println("<!DOCTYPE html><html><head><title>Новини Виробника</title>");
        out.println("<style>.news-item { border: 1px solid #ccc; padding: 15px; margin-bottom: 20px; }</style>");
        out.println("</head><body>");
        out.println("<h1>Новини Виробника</h1>");

        if (newsList.isEmpty()) {
            out.println("<p>Наразі немає новин.</p>");
        } else {
            for (LaptopModel news : newsList) {
                out.println("<div class='news-item'>");
                out.println("<h3>" + news.getName() + "</h3>");
                out.println("<p>" + news.getDescription() + "</p>");
                out.println("</div>");
            }
        }

        out.println("<p><a href='" + contextPath + "/'>**Повернутися до меню**</a></p>");
        out.println("</body></html>");
    }

    private void displayModels(HttpServletRequest request, PrintWriter out) {
        String contextPath = request.getContextPath();
        List<LaptopModel> modelsList = MODEL_LIST;

        out.println("<!DOCTYPE html><html><head><title>Моделі ноутбуків</title>");
        out.println("<style>.model-card { border: 1px solid #ddd; padding: 15px; margin-bottom: 20px; }</style>");
        out.println("</head><body>");
        out.println("<h1>Моделі ноутбуків</h1>");

        if (modelsList.isEmpty()) {
            out.println("<p>Наразі немає моделей.</p>");
        } else {
            for (LaptopModel model : modelsList) {
                out.println("<div class='model-card'>");
                out.println("<h2>" + model.getName() + "</h2>");
                out.println("<p>" + model.getDescription() + "</p>");
                out.println("</div>");
            }
        }

        out.println("<p><a href='" + contextPath + "/'>**Повернутися до меню**</a></p>");
        out.println("</body></html>");
    }
}