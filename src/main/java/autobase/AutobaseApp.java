package autobase;

import autobase.domain.Trip;
import autobase.domain.TripRequest;
import autobase.service.AutoBase;
import java.util.Optional;

public class AutobaseApp {
    public static void main(String[] args) {
        AutoBase autoBase = new AutoBase();

        System.out.println("=== Завдання 2: Система Автобаза ===");

        for (int i = 0; i < 5; i++) {
            System.out.println("\n--- День " + (i + 1) + " ---");
            TripRequest request = autoBase.generateRandomRequest();
            System.out.println("Нова заявка: " + request.getDestination() + " (" + String.format("%.0f", request.getCargo().getWeightKg()) + " кг)");

            Optional<Trip> trip = autoBase.createTrip(request);

            trip.ifPresent(t -> {
                // Імітуємо виконання рейсу
                boolean needsRepair = Math.random() < 0.2;
                autoBase.completeTrip(t, needsRepair);

                if (needsRepair) {
                    autoBase.repairVehicle(t.getVehicle().getId());
                }
            });
        }

        System.out.println("\n=== Підсумки ===");
        System.out.println("Водії:");
        autoBase.getDrivers().forEach(d -> System.out.println("  " + d.getName() + ": Доступний=" + d.isAvailable() + ", Виплати=" + String.format("%.2f", d.getTotalPayouts())));

        System.out.println("Автомобілі:");
        autoBase.getVehicles().forEach(v -> System.out.println("  " + v.getModel() + ": Доступний=" + v.isAvailable() + ", Зламаний=" + v.isBroken()));
    }
}