package library;

import library.domain.Book;
import library.domain.Columnist;
import library.domain.Newspaper;
import library.service.LibraryCatalog;
import java.time.LocalDate;
import java.util.List;

public class App {
    public static void main(String[] args) {
        LibraryCatalog catalog = new LibraryCatalog();

        catalog.initializeTestData();

        catalog.displayGroupedCatalog();

        Book newBook = new Book("Роберт Мартін", "Чистий код", "Програмування", 2008, "Prentice Hall", 464);
        catalog.addItem(newBook);

        Newspaper daily = new Newspaper(1, "Daily News", LocalDate.now(), List.of(new Columnist("Local", "A. Smith")));
        catalog.addItem(daily);

        catalog.addRandomItem();

        System.out.println("\n--- РЕЗУЛЬТАТИ ПОШУКУ (за назвою: Сьогодення) ---");
        catalog.search("Сьогодення", "title").forEach(System.out::println);

        System.out.println("\n--- РЕЗУЛЬТАТИ ПОШУКУ (за автором: Толкін) ---");
        catalog.search("Толкін", "author").forEach(System.out::println);

        catalog.updateItemYear("Гобіт", 1938);
        System.out.println("\nРік видання 'Гобіт' оновлено до 1938.");

        boolean removed = catalog.removeItem("Великий Гетсбі");
        System.out.println("\n'Великий Гетсбі' видалено: " + (removed ? "Так" : "Ні"));

        catalog.displayGroupedCatalog();
    }
}