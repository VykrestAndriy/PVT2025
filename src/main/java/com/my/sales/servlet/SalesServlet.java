package com.my.sales.servlet;

import com.my.sales.dao.SaleDAO;
import com.my.sales.model.Customer;
import com.my.sales.model.Product;
import com.my.sales.model.Sale;
import com.my.sales.model.Seller;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@WebServlet("/sales")
public class SalesServlet extends HttpServlet {

    private final SaleDAO saleDAO = new SaleDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            if (action == null || action.isEmpty()) {
                request.setAttribute("salesList", saleDAO.getAllSales());
            } else {
                handleGetAction(action, request);
            }
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Помилка при обробці запиту: " + e.getMessage());
        }

        request.getRequestDispatcher("/sales.jsp").forward(request, response);
    }

    private void handleGetAction(String action, HttpServletRequest request) throws Exception {
        switch (action) {
            case "allSellers":
                request.setAttribute("sellersList", saleDAO.getAllSellers());
                break;
            case "allCustomers":
                request.setAttribute("customersList", saleDAO.getAllCustomers());
                break;
            case "allProducts":
                request.setAttribute("productsList", saleDAO.getAllProducts());
                break;
            case "allSales":
                request.setAttribute("salesList", saleDAO.getAllSales());
                break;

            case "salesByDate":
                LocalDate date = LocalDate.parse(request.getParameter("date"));
                request.setAttribute("salesList", saleDAO.getSalesByDate(date));
                break;
            case "salesByRange":
                LocalDate start = LocalDate.parse(request.getParameter("startDate"));
                LocalDate end = LocalDate.parse(request.getParameter("endDate"));
                request.setAttribute("salesList", saleDAO.getSalesByDateRange(start, end));
                break;
            case "salesBySeller":
                Long sellerId = Long.parseLong(request.getParameter("sellerId"));
                request.setAttribute("salesList", saleDAO.getSalesBySeller(sellerId));
                break;
            case "salesByCustomer":
                Long customerId = Long.parseLong(request.getParameter("customerId"));
                request.setAttribute("salesList", saleDAO.getSalesByCustomer(customerId));
                break;

            case "successfulSeller":
                saleDAO.getMostSuccessfulSeller().ifPresent(result -> request.setAttribute("aggregationResult", result));
                break;
            case "successfulCustomer":
                saleDAO.getMostSuccessfulCustomer().ifPresent(result -> request.setAttribute("aggregationResult", result));
                break;
            case "avgSale":
                saleDAO.getAverageSaleAmount().ifPresent(result -> request.setAttribute("singleResult", result));
                break;
            case "demandedProduct":
                saleDAO.getMostDemandedProduct().ifPresent(result -> request.setAttribute("aggregationResult", result));
                break;

            default:
                request.setAttribute("salesList", saleDAO.getAllSales());
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String message = "";

        try {
            switch (action) {
                case "saveSeller":
                    handleSaveSeller(request);
                    message = "Продавець успішно доданий!";
                    break;
                case "saveCustomer":
                    handleSaveCustomer(request);
                    message = "Покупець успішно доданий!";
                    break;
                case "saveProduct":
                    handleSaveProduct(request);
                    message = "Товар успішно доданий!";
                    break;
                case "saveSale":
                    handleSaveSale(request);
                    message = "Угода успішно додана!";
                    break;
                case "delete":
                    handleDelete(request);
                    message = "Запис успішно видалений!";
                    break;
                case "update":
                    handleUpdate(request);
                    message = "Запис успішно оновлений!";
                    break;
                default:
                    message = "Невідома дія.";
                    break;
            }
            request.setAttribute("successMessage", message);
        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", "Помилка введення: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Помилка при виконанні операції: " + e.getMessage());
            e.printStackTrace();
        }

        doGet(request, response);
    }

    private void handleSaveSeller(HttpServletRequest request) {
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        Seller seller = new Seller(name, phone, email);
        saleDAO.save(seller);
    }

    private void handleSaveCustomer(HttpServletRequest request) {
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        Customer customer = new Customer(name, phone, email);
        saleDAO.save(customer);
    }

    private void handleSaveProduct(HttpServletRequest request) {
        String name = request.getParameter("name");
        BigDecimal price = new BigDecimal(request.getParameter("price"));
        Product product = new Product(name, price);
        saleDAO.save(product);
    }

    private void handleSaveSale(HttpServletRequest request) {
        Long sellerId = Long.parseLong(request.getParameter("sellerId"));
        Long customerId = Long.parseLong(request.getParameter("customerId"));
        Long productId = Long.parseLong(request.getParameter("productId"));
        LocalDate saleDate = LocalDate.parse(request.getParameter("saleDate"));

        Seller seller = saleDAO.find(Seller.class, sellerId);
        Customer customer = saleDAO.find(Customer.class, customerId);
        Product product = saleDAO.find(Product.class, productId);

        if (seller != null && customer != null && product != null) {
            Sale sale = new Sale(seller, customer, product, saleDate);
            saleDAO.save(sale);
        } else {
            String missingEntity = seller == null ? "Продавець" : (customer == null ? "Покупець" : "Товар");
            throw new IllegalArgumentException(missingEntity + " із вказаним ID не знайдено.");
        }
    }

    private void handleDelete(HttpServletRequest request) {
        String entityType = request.getParameter("entityType");
        Long id = Long.parseLong(request.getParameter("id"));

        switch (entityType) {
            case "seller":
                saleDAO.delete(Seller.class, id);
                break;
            case "customer":
                saleDAO.delete(Customer.class, id);
                break;
            case "product":
                saleDAO.delete(Product.class, id);
                break;
            case "sale":
                saleDAO.delete(Sale.class, id);
                break;
            default:
                throw new IllegalArgumentException("Невідомий тип сутності для видалення.");
        }
    }

    private void handleUpdate(HttpServletRequest request) {
        String entityType = request.getParameter("entityType");
        Long id = Long.parseLong(request.getParameter("id"));

        switch (entityType) {
            case "seller":
                Seller seller = saleDAO.find(Seller.class, id);
                if (seller != null) {
                    seller.setName(request.getParameter("name"));
                    seller.setPhone(request.getParameter("phone"));
                    seller.setEmail(request.getParameter("email"));
                    saleDAO.update(seller);
                }
                break;
            case "customer":
                Customer customer = saleDAO.find(Customer.class, id);
                if (customer != null) {
                    customer.setName(request.getParameter("name"));
                    customer.setPhone(request.getParameter("phone"));
                    customer.setEmail(request.getParameter("email"));
                    saleDAO.update(customer);
                }
                break;
            case "product":
                Product product = saleDAO.find(Product.class, id);
                if (product != null) {
                    product.setName(request.getParameter("name"));
                    // Перевірка на null/порожній рядок для ціни
                    String priceParam = request.getParameter("price");
                    if (priceParam != null && !priceParam.isEmpty()) {
                        product.setPrice(new BigDecimal(priceParam));
                    }
                    saleDAO.update(product);
                }
                break;
            case "sale":
                throw new UnsupportedOperationException("Оновлення Угоди не підтримується через UI.");
            default:
                throw new UnsupportedOperationException("Оновлення цього типу сутності поки не підтримується.");
        }
    }
}