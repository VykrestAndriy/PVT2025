package library.service;

import library.domain.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.LocalDate;

public class LibraryCatalog {
    private final List<CatalogItem> catalog = new ArrayList<>();

    public void initializeTestData() {
        catalog.add(new Book("Дж. Р. Р. Толкін", "Гобіт", "Фентезі", 1937, "Allen & Unwin", 310));
        catalog.add(new Book("Ф. С. Фіцджеральд", "Великий Гетсбі", "Роман", 1925, "Scribner's", 180));

        List<Columnist> gazetaColumns = new ArrayList<>();
        gazetaColumns.add(new Columnist("Спорт", "Іван Спортов"));
        gazetaColumns.add(new Columnist("Політика", "Олена Політична"));
        catalog.add(new Newspaper(503, "Сьогодення", LocalDate.of(2025, 10, 7), gazetaColumns));

        List<Book> almanacWorks = new ArrayList<>();
        almanacWorks.add(new Book("А. П. Чехов", "Палата №6", "Оповідання", 1892, "Н/Д", 0));
        almanacWorks.add(new Book("М. В. Гоголь", "Ніс", "Сатира", 1835, "Н/Д", 0));
        catalog.add(new Almanac("Збірка класиків", "Класика", 2024, "Видавничий Дім", 500, almanacWorks));

        System.out.println("Каталог успішно ініціалізовано тестовими даними.");
    }

    public void addItem(CatalogItem item) {
        catalog.add(item);
        System.out.println("Додано об'єкт: " + item.getTitle());
    }

    public void addRandomItem() {
        addItem(new Book("Random Author", "Random Title " + System.currentTimeMillis() % 1000, "Fiction", 2023, "R-Publisher", 100));
    }

    public boolean removeItem(String title) {
        return catalog.removeIf(item -> item.getTitle().equalsIgnoreCase(title));
    }

    public boolean updateItemYear(String title, int newYear) {
        for (CatalogItem item : catalog) {
            if (item.getTitle().equalsIgnoreCase(title)) {
                if (item instanceof Book) {
                    ((Book) item).setYear(newYear);
                    return true;
                }
                if (item instanceof Almanac) {
                    ((Almanac) item).setYear(newYear);
                    return true;
                }
                if (item instanceof Newspaper) {
                    ((Newspaper) item).setReleaseDate(LocalDate.of(newYear, ((Newspaper) item).getReleaseDate().getMonth(), ((Newspaper) item).getReleaseDate().getDayOfMonth()));
                    return true;
                }
            }
        }
        return false;
    }

    public void displayGroupedCatalog() {
        Map<String, List<CatalogItem>> grouped = catalog.stream()
                .collect(Collectors.groupingBy(CatalogItem::getType));

        System.out.println("\n--- КАТАЛОГ БІБЛІОТЕКИ (Згруповано за типом) ---");
        grouped.forEach((type, items) -> {
            System.out.println("\n== ТИП: " + type + " (" + items.size() + " об'єктів) ==");
            items.forEach(item -> System.out.println("  > " + item));
        });
        System.out.println("----------------------------------------------");
    }

    public List<CatalogItem> search(String query, String field) {
        return catalog.stream()
                .filter(item -> {
                    switch (field.toLowerCase()) {
                        case "title":
                            return item.getTitle().toLowerCase().contains(query.toLowerCase());
                        case "publisher":
                            return item.getPublisher().toLowerCase().contains(query.toLowerCase());
                        case "year":
                            try {
                                return item.getYear() == Integer.parseInt(query);
                            } catch (NumberFormatException e) {
                                return false;
                            }
                        case "author":
                            if (item instanceof Book) {
                                return ((Book) item).getAuthor().toLowerCase().contains(query.toLowerCase());
                            }
                            return false;
                        default:
                            return false;
                    }
                })
                .collect(Collectors.toList());
    }
}