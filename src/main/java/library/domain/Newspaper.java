package library.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Newspaper implements CatalogItem {
    private int issueNumber;
    private String title;
    private LocalDate releaseDate;
    private List<Columnist> columnists;

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public String getPublisher() {
        return "Unknown";
    }

    @Override
    public int getYear() {
        return this.releaseDate.getYear();
    }

    @Override
    public String getType() {
        return "Newspaper";
    }
}