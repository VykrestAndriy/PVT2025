package autobase.service;

import autobase.domain.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Optional;
import java.util.stream.Collectors;

public class AutoBase {
    private final List<Driver> drivers = new ArrayList<>();
    private final List<Vehicle> vehicles = new ArrayList<>();
    private final List<Trip> activeTrips = new ArrayList<>();
    private final Dispatcher dispatcher = new Dispatcher();
    private final Random random = new Random();

    public AutoBase() {
        initializeResources();
    }

    private void initializeResources() {
        drivers.add(new Driver(1, "Іван", 5, true, 0.0));
        drivers.add(new Driver(2, "Петро", 10, true, 0.0));
        drivers.add(new Driver(3, "Олена", 2, true, 0.0));

        vehicles.add(new Vehicle(101, "Volvo FH", 20000.0, 3, true, false));
        vehicles.add(new Vehicle(102, "Renault Magnum", 15000.0, 2, true, false));
        vehicles.add(new Vehicle(103, "Газель", 3000.0, 1, true, false));
    }

    public TripRequest generateRandomRequest() {
        String[] destinations = {"Київ", "Львів", "Одеса", "Харків"};
        String[] cargoTypes = {"Продукти", "Будматеріали", "Електроніка"};

        String dest = destinations[random.nextInt(destinations.length)];
        String cargoType = cargoTypes[random.nextInt(cargoTypes.length)];
        double weight = 1000 + random.nextDouble() * 19000;
        int experience = random.nextInt(10) + 1;
        int length = 200 + random.nextInt(1000);

        return new TripRequest(dest, new Cargo(cargoType, weight), experience, length);
    }

    public Optional<Trip> createTrip(TripRequest request) {
        List<Driver> availableDrivers = drivers.stream().filter(Driver::isAvailable).collect(Collectors.toList());
        List<Vehicle> availableVehicles = vehicles.stream().filter(Vehicle::isAvailable).collect(Collectors.toList());

        Optional<Driver> driver = dispatcher.findOptimalDriver(request, availableDrivers);
        Optional<Vehicle> vehicle = dispatcher.findOptimalVehicle(request, availableVehicles);

        if (driver.isPresent() && vehicle.isPresent()) {
            Driver assignedDriver = driver.get();
            Vehicle assignedVehicle = vehicle.get();

            assignedDriver.setAvailable(false);
            assignedVehicle.setAvailable(false);

            Trip trip = new Trip(
                    request.getId(),
                    assignedDriver,
                    assignedVehicle,
                    request.getDestination(),
                    request.getCargo(),
                    request.getTripLengthKm(),
                    LocalDateTime.now(),
                    false,
                    false
            );
            activeTrips.add(trip);
            System.out.println("Рейс створено: " + trip.getDestination() + " з водієм " + assignedDriver.getName());
            return Optional.of(trip);
        } else {
            System.out.println("Помилка: Не вдалося знайти водія або авто для заявки до " + request.getDestination());
            return Optional.empty();
        }
    }

    public void completeTrip(Trip trip, boolean needsRepair) {
        trip.setCompleted(true);
        trip.setNeedsRepair(needsRepair);

        Driver driver = trip.getDriver();
        Vehicle vehicle = trip.getVehicle();

        double payout = dispatcher.calculatePayout(trip);
        driver.setTotalPayouts(driver.getTotalPayouts() + payout);

        if (needsRepair) {
            System.out.println("Автомобіль " + vehicle.getModel() + " потребує ремонту.");
            vehicle.setBroken(true);
        }

        driver.setAvailable(true);
        vehicle.setAvailable(!needsRepair);

        activeTrips.remove(trip);
        System.out.println("Рейс до " + trip.getDestination() + " завершено. Виплата водію: " + String.format("%.2f", payout));
    }

    public void repairVehicle(int vehicleId) {
        vehicles.stream()
                .filter(v -> v.getId() == vehicleId)
                .findFirst()
                .ifPresent(v -> {
                    v.setBroken(false);
                    v.setAvailable(true);
                    System.out.println("Автомобіль " + v.getModel() + " відремонтовано та доступно.");
                });
    }

    public List<Driver> getDrivers() {
        return drivers;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }
}