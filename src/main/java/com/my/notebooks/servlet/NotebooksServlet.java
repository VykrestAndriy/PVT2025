package com.my.notebooks.servlet;

import com.my.notebooks.dao.NotebookDAO;
import com.my.notebooks.model.Notebook;
import com.my.notebooks.util.JpaUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/notebooks")
public class NotebooksServlet extends HttpServlet {

    private NotebookDAO notebookDAO;

    @Override
    public void init() throws ServletException {
        notebookDAO = new NotebookDAO();
    }

    @Override
    public void destroy() {
        JpaUtil.shutdown();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        List<?> notebooks = null;
        Object aggregationResult = null;
        String title = "Усі блокноти";

        if (action == null || action.isEmpty() || action.equals("all")) {
            notebooks = notebookDAO.getAllNotebooks();
            title = "Усі блокноти у базі даних";

        } else if (action.equals("filterByCover")) {
            String cover = request.getParameter("value");
            if ("тверда".equalsIgnoreCase(cover)) {
                notebooks = notebookDAO.getNotebooksByHardCover();
                title = "Блокноти з твердою обкладинкою";
            } else if ("м'яка".equalsIgnoreCase(cover) || "м’яка".equalsIgnoreCase(cover)) {
                notebooks = notebookDAO.getNotebooksBySoftCover();
                title = "Блокноти з м'якою обкладинкою";
            }

        } else if (action.equals("filterByCountry")) {
            String country = request.getParameter("value");
            notebooks = notebookDAO.getNotebooksByCountry(country);
            title = "Блокноти виробництва: " + country;

        } else if (action.equals("filterByLayout")) {
            String layout = request.getParameter("value");
            notebooks = notebookDAO.filterByPageLayout(layout);
            title = "Блокноти з розкладкою: " + layout;

        } else if (action.equals("filterByPages")) {
            try {
                int min = Integer.parseInt(request.getParameter("min"));
                int max = Integer.parseInt(request.getParameter("max"));
                notebooks = notebookDAO.filterByPageCount(min, max);
                title = "Блокноти з кількістю сторінок від " + min + " до " + max;
            } catch (NumberFormatException ignored) {}

        } else if (action.equals("filterByCirculation")) {
            try {
                int min = Integer.parseInt(request.getParameter("min"));
                int max = Integer.parseInt(request.getParameter("max"));
                notebooks = notebookDAO.filterByCirculation(min, max);
                title = "Блокноти тиражем від " + min + " до " + max;
            } catch (NumberFormatException ignored) {}

        } else if (action.equals("aggregateCountryCount")) {
            aggregationResult = notebookDAO.getCountByCountry();
            title = "Кількість блокнотів по країнах";

        } else if (action.equals("aggregateManufacturerCount")) {
            aggregationResult = notebookDAO.getCountByManufacturer();
            title = "Кількість блокнотів по виробниках";

        } else if (action.equals("maxCountry")) {
            Optional<Object[]> result = notebookDAO.getCountryWithMaxNotebooks();
            if (result.isPresent()) {
                aggregationResult = result.get();
                title = "Країна з найбільшою кількістю блокнотів";
            }

        } else if (action.equals("minManufacturer")) {
            Optional<Object[]> result = notebookDAO.getManufacturerWithMinNotebooks();
            if (result.isPresent()) {
                aggregationResult = result.get();
                title = "Виробник з найменшою кількістю блокнотів";
            }
        }

        request.setAttribute("notebooksList", notebooks);
        request.setAttribute("aggregationResult", aggregationResult);
        request.setAttribute("pageTitle", title);

        request.getRequestDispatcher("/notebooks.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String manufacturerName = request.getParameter("manufacturerName");
        String notebookNameCode = request.getParameter("notebookNameCode");
        int pageCount = Integer.parseInt(request.getParameter("pageCount"));
        String coverType = request.getParameter("coverType");
        String country = request.getParameter("country");
        int circulation = Integer.parseInt(request.getParameter("circulation"));
        String pageLayout = request.getParameter("pageLayout");
        String action = request.getParameter("action");
        String idParam = request.getParameter("id");

        if ("add".equals(action)) {
            Notebook newNotebook = new Notebook(manufacturerName, notebookNameCode, pageCount, coverType, country, circulation, pageLayout);
            notebookDAO.saveNotebook(newNotebook);
        } else if ("update".equals(action) && idParam != null && !idParam.isEmpty()) {
            Long id = Long.parseLong(idParam);
            Notebook existingNotebook = new Notebook(id, manufacturerName, notebookNameCode, pageCount, coverType, country, circulation, pageLayout);
            notebookDAO.updateNotebook(existingNotebook);
        } else if ("delete".equals(action) && idParam != null && !idParam.isEmpty()) {
            Long id = Long.parseLong(idParam);
            notebookDAO.deleteNotebook(id);
        }

        response.sendRedirect(request.getContextPath() + "/notebooks?action=all");
    }
}