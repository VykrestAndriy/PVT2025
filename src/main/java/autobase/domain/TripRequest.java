package autobase.domain;

import lombok.Value;
import java.util.UUID;

@Value
public class TripRequest {
    UUID id = UUID.randomUUID();
    String destination;
    Cargo cargo;
    int requiredExperience;
    int tripLengthKm;
}