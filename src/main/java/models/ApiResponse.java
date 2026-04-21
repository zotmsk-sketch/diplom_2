package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
    private boolean success;
    private String message;
    private User user;
    private String accessToken;
    private String refreshToken;
    private String name;
    private Order order;
    private Integer total;
    private Integer totalToday;
}