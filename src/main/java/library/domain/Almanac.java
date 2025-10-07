package library.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Almanac implements CatalogItem {
    private String title;
    private String genre;
    private int year;
    private String publisher;
    private int pageCount;
    private List<Book> works;

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public String getPublisher() {
        return this.publisher;
    }

    @Override
    public int getYear() {
        return this.year;
    }

    @Override
    public String getType() {
        return "Almanac";
    }
}