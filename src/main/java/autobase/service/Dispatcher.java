package autobase.service;

import autobase.domain.*;
import java.util.List;
import java.util.Optional;

public class Dispatcher {

    public Optional<Driver> findOptimalDriver(TripRequest request, List<Driver> availableDrivers) {
        return availableDrivers.stream()
                .filter(Driver::isAvailable)
                .filter(driver -> driver.getExperienceYears() >= request.getRequiredExperience())
                .min((d1, d2) -> {
                    double score1 = d1.getExperienceYears();
                    double score2 = d2.getExperienceYears();
                    return Double.compare(score2, score1);
                });
    }

    public Optional<Vehicle> findOptimalVehicle(TripRequest request, List<Vehicle> availableVehicles) {
        return availableVehicles.stream()
                .filter(Vehicle::isAvailable)
                .filter(vehicle -> !vehicle.isBroken())
                .filter(vehicle -> vehicle.getCarryingCapacityKg() >= request.getCargo().getWeightKg())
                .min((v1, v2) -> Double.compare(v1.getCarryingCapacityKg(), v2.getCarryingCapacityKg()));
    }

    public double calculatePayout(Trip trip) {
        double baseRate = 10.0;
        double experienceBonus = trip.getDriver().getExperienceYears() * 0.5;
        double distanceFactor = trip.getTripLengthKm() * 0.1;

        return baseRate + experienceBonus + distanceFactor + trip.getCargo().getWeightKg() * 0.05;
    }
}