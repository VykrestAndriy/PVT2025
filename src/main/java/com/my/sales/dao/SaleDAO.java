package com.my.sales.dao;

import com.my.sales.model.Sale;
import com.my.sales.model.Seller;
import com.my.sales.model.Customer;
import com.my.sales.model.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class SaleDAO {

    @FunctionalInterface
    private interface Consumer<T> {
        void accept(T t);
    }

    @FunctionalInterface
    private interface Function<T, R> {
        R apply(T t);
    }

    private void executeTransaction(Consumer<EntityManager> action) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            action.accept(em);
            transaction.commit();
        } catch (Exception e) {
            System.err.println("Transaction failed: " + e.getMessage());
            if (transaction.isActive()) transaction.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    private <R> R executeQuery(Function<EntityManager, R> action) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            return action.apply(em);
        } finally {
            em.close();
        }
    }

    public void save(Object entity) {
        executeTransaction(em -> em.persist(entity));
    }

    public void update(Object entity) {
        executeTransaction(em -> em.merge(entity));
    }

    public void delete(Class<?> entityClass, Long id) {
        executeTransaction(em -> {
            Object entity = em.find(entityClass, id);
            if (entity != null) em.remove(entity);
        });
    }

    public <T> T find(Class<T> entityClass, Long id) {
        return executeQuery(em -> em.find(entityClass, id));
    }

    public List<Seller> getAllSellers() {
        return executeQuery(em ->
                em.createQuery("SELECT s FROM Seller s", Seller.class).getResultList()
        );
    }

    public List<Customer> getAllCustomers() {
        return executeQuery(em ->
                em.createQuery("SELECT c FROM Customer c", Customer.class).getResultList()
        );
    }

    public List<Product> getAllProducts() {
        return executeQuery(em ->
                em.createQuery("SELECT p FROM Product p", Product.class).getResultList()
        );
    }

    public List<Sale> getAllSales() {
        return executeQuery(em ->
                em.createQuery("SELECT s FROM Sale s JOIN FETCH s.seller JOIN FETCH s.customer JOIN FETCH s.product ORDER BY s.saleDate DESC", Sale.class).getResultList()
        );
    }

    public List<Sale> getSalesByDate(LocalDate date) {
        return executeQuery(em -> em.createQuery("SELECT s FROM Sale s JOIN FETCH s.seller JOIN FETCH s.customer JOIN FETCH s.product WHERE s.saleDate = :date", Sale.class)
                .setParameter("date", date)
                .getResultList());
    }

    public List<Sale> getSalesByDateRange(LocalDate startDate, LocalDate endDate) {
        return executeQuery(em -> em.createQuery("SELECT s FROM Sale s JOIN FETCH s.seller JOIN FETCH s.customer JOIN FETCH s.product WHERE s.saleDate BETWEEN :start AND :end ORDER BY s.saleDate", Sale.class)
                .setParameter("start", startDate)
                .setParameter("end", endDate)
                .getResultList());
    }

    public List<Sale> getSalesBySeller(Long sellerId) {
        return executeQuery(em -> em.createQuery("SELECT s FROM Sale s JOIN FETCH s.seller JOIN FETCH s.customer JOIN FETCH s.product WHERE s.seller.id = :sellerId", Sale.class)
                .setParameter("sellerId", sellerId)
                .getResultList());
    }

    public List<Sale> getSalesByCustomer(Long customerId) {
        return executeQuery(em -> em.createQuery("SELECT s FROM Sale s JOIN FETCH s.seller JOIN FETCH s.customer JOIN FETCH s.product WHERE s.customer.id = :customerId", Sale.class)
                .setParameter("customerId", customerId)
                .getResultList());
    }

    public Optional<Object[]> getMostSuccessfulSeller() {
        return executeQuery(em -> {
            String jpql = "SELECT s.name, SUM(p.price) " +
                    "FROM Sale sa JOIN sa.seller s JOIN sa.product p " +
                    "GROUP BY s.name ORDER BY SUM(p.price) DESC";
            List<Object[]> result = em.createQuery(jpql, Object[].class)
                    .setMaxResults(1)
                    .getResultList();
            return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
        });
    }

    public Optional<Object[]> getMostSuccessfulCustomer() {
        return executeQuery(em -> {
            String jpql = "SELECT c.name, SUM(p.price) " +
                    "FROM Sale sa JOIN sa.customer c JOIN sa.product p " +
                    "GROUP BY c.name ORDER BY SUM(p.price) DESC";
            List<Object[]> result = em.createQuery(jpql, Object[].class)
                    .setMaxResults(1)
                    .getResultList();
            return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
        });
    }

    public Optional<Double> getAverageSaleAmount() {
        return executeQuery(em -> {
            Double avg = em.createQuery("SELECT AVG(p.price) FROM Sale sa JOIN sa.product p", Double.class).getSingleResult();
            return Optional.ofNullable(avg);
        });
    }

    public Optional<Object[]> getMostDemandedProduct() {
        return executeQuery(em -> {
            String jpql = "SELECT p.name, COUNT(sa.id) " +
                    "FROM Sale sa JOIN sa.product p " +
                    "GROUP BY p.name ORDER BY COUNT(sa.id) DESC";
            List<Object[]> result = em.createQuery(jpql, Object[].class)
                    .setMaxResults(1)
                    .getResultList();
            return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
        });
    }
}