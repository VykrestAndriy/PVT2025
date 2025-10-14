package org.ua.cafe.repo;

import org.ua.cafe.model.Client;
import org.ua.cafe.model.Dessert;
import org.ua.cafe.model.Drink;
import org.ua.cafe.model.Staff;
import java.time.LocalDate;
import java.util.List;

public interface CafeRepositoryInterface {
    List<Drink> findAllDrinks();
    List<String> findTopNPopularDrinksLastMonth(int limit);
    List<Dessert> findAllDesserts();
    List<String> findTopNPopularDessertsLastDays(int limit, int days);
    List<Staff> findAllBaristas();
    List<Staff> findAllWaiters();
    double getAverageOrderSumByDate(LocalDate date);
    List<Double> findLargestOrderSumByDate(LocalDate date);
    List<Client> findRegularClients(int minVisits, int days);
    boolean createNewOrder(int clientId, int staffId, double totalSum);
}