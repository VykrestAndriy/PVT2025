package autobase.domain;

import lombok.Data;
import lombok.AllArgsConstructor;
import java.util.UUID;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Trip {
    private UUID requestId;
    private Driver driver;
    private Vehicle vehicle;
    private String destination;
    private Cargo cargo;
    private int tripLengthKm;
    private LocalDateTime startTime;
    private boolean isCompleted;
    private boolean needsRepair;
}