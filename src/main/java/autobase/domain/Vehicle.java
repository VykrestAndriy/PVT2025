package autobase.domain;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class Vehicle {
    private int id;
    private String model;
    private double carryingCapacityKg;
    private int complexity;
    private boolean isAvailable;
    private boolean isBroken;
}