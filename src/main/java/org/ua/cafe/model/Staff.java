package org.ua.cafe.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Staff {
    private int id;
    private String fullName;
    private String position;
    private String phone;
}