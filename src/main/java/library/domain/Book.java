package library.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.AllArgsConstructor;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class Book implements CatalogItem {
    private String author;
    private String title;
    private String genre;
    private int year;
    private String publisher;
    private int pageCount;

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
        return "Book";
    }
}