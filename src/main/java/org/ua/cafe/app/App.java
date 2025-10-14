package org.ua.cafe.app;

import org.ua.cafe.repo.CafeRepository;
import org.ua.cafe.repo.CafeRepositoryInterface;
import java.time.LocalDate;

public class App {
    public static void main(String[] args) {
        CafeRepositoryInterface repo = new CafeRepository();

        LocalDate testDate = LocalDate.now();

        System.out.println("--- 1. Усі Напої ---");
        repo.findAllDrinks().forEach(System.out::println);

        System.out.println("\n--- 2. Топ 3 напої за останній місяць ---");
        repo.findTopNPopularDrinksLastMonth(3).forEach(System.out::println);

        System.out.println("\n--- 3. Усі Десерти ---");
        repo.findAllDesserts().forEach(System.out::println);

        System.out.println("\n--- 4. Топ 5 десертів за останні 10 днів ---");
        repo.findTopNPopularDessertsLastDays(5, 10).forEach(System.out::println);

        System.out.println("\n--- 5. Усі Баристи ---");
        repo.findAllBaristas().forEach(System.out::println);

        System.out.println("\n--- 6. Усі Офіціанти ---");
        repo.findAllWaiters().forEach(System.out::println);

        System.out.printf("\n--- 7. Середня сума замовлення на %s: %.2f грн ---\n", testDate, repo.getAverageOrderSumByDate(testDate));

        System.out.println("\n--- 8. Найбільші замовлення на " + testDate + " ---");
        repo.findLargestOrderSumByDate(testDate).forEach(s -> System.out.printf("Сума: %.2f грн\n", s));

        System.out.println("\n--- 9. Постійні клієнти (3+ візити за 7 днів) ---");
        repo.findRegularClients(3, 7).forEach(System.out::println);

        System.out.println("\n--- 10. Створення нового замовлення (Тест CRUD) ---");
        boolean success = repo.createNewOrder(1, 1, 150.50);
        System.out.println(success ? "Замовлення успішно додано!" : "Помилка при додаванні замовлення.");
    }
}