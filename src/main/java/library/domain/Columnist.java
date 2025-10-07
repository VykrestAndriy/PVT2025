package library.domain;

import lombok.Value;
import lombok.AllArgsConstructor;

@Value
@AllArgsConstructor
public class Columnist {
    String columnName;
    String journalistName;
}