package com.my.notebooks.dao;

import com.my.notebooks.model.Notebook;
import com.my.notebooks.util.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import java.util.List;
import java.util.Optional;

public class NotebookDAO {

    public void saveNotebook(Notebook notebook) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(notebook);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void updateNotebook(Notebook notebook) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.merge(notebook);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void deleteNotebook(Long id) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Notebook notebook = em.find(Notebook.class, id);
            if (notebook != null) {
                em.remove(notebook);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public List<Notebook> getAllNotebooks() {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery("SELECT n FROM Notebook n", Notebook.class).getResultList();
        } finally {
            em.close();
        }
    }

    public List<Object[]> getCountByCountry() {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery("SELECT n.country, COUNT(n.id) FROM Notebook n GROUP BY n.country", Object[].class).getResultList();
        } finally {
            em.close();
        }
    }

    public List<Object[]> getCountByManufacturer() {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery("SELECT n.manufacturerName, COUNT(n.id) FROM Notebook n GROUP BY n.manufacturerName", Object[].class).getResultList();
        } finally {
            em.close();
        }
    }

    public Optional<Object[]> getCountryWithMaxNotebooks() {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT n.country, COUNT(n.id) FROM Notebook n GROUP BY n.country ORDER BY COUNT(n.id) DESC";
            Query query = em.createQuery(jpql);
            query.setMaxResults(1);
            List<Object[]> results = query.getResultList();
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        } finally {
            em.close();
        }
    }

    public Optional<Object[]> getCountryWithMinNotebooks() {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT n.country, COUNT(n.id) FROM Notebook n GROUP BY n.country ORDER BY COUNT(n.id) ASC";
            Query query = em.createQuery(jpql);
            query.setMaxResults(1);
            List<Object[]> results = query.getResultList();
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        } finally {
            em.close();
        }
    }

    public Optional<Object[]> getManufacturerWithMinNotebooks() {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT n.manufacturerName, COUNT(n.id) FROM Notebook n GROUP BY n.manufacturerName ORDER BY COUNT(n.id) ASC";
            Query query = em.createQuery(jpql);
            query.setMaxResults(1);
            List<Object[]> results = query.getResultList();
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        } finally {
            em.close();
        }
    }

    public List<Notebook> getNotebooksByCover(String coverType) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery("SELECT n FROM Notebook n WHERE LOWER(n.coverType) = LOWER(:cover)", Notebook.class)
                    .setParameter("cover", coverType)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Notebook> getNotebooksByHardCover() {
        return getNotebooksByCover("тверда");
    }

    public List<Notebook> getNotebooksBySoftCover() {
        return getNotebooksByCover("м’яка");
    }

    public List<Notebook> getNotebooksByCountry(String country) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery("SELECT n FROM Notebook n WHERE LOWER(n.country) = LOWER(:countryName)", Notebook.class)
                    .setParameter("countryName", country)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Notebook> filterByPageLayout(String pageLayout) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery("SELECT n FROM Notebook n WHERE LOWER(n.pageLayout) = LOWER(:layout)", Notebook.class)
                    .setParameter("layout", pageLayout)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Notebook> filterByPageCount(int minPages, int maxPages) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery("SELECT n FROM Notebook n WHERE n.pageCount BETWEEN :min AND :max", Notebook.class)
                    .setParameter("min", minPages)
                    .setParameter("max", maxPages)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public List<Notebook> filterByCirculation(int minCirculation, int maxCirculation) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery("SELECT n FROM Notebook n WHERE n.circulation BETWEEN :min AND :max", Notebook.class)
                    .setParameter("min", minCirculation)
                    .setParameter("max", maxCirculation)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}