package autobase.domain;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class Driver {
    private int id;
    private String name;
    private int experienceYears;
    private boolean isAvailable;
    private double totalPayouts;
}