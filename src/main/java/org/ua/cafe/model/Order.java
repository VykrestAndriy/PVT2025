package org.ua.cafe.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private int orderId;
    private int clientId;
    private int staffId;
    private LocalDateTime orderDateTime;
    private double totalSum;
}