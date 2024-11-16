package Food_Orders.Dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Setter
@Getter

public class CartItemDto {
    private Long id;
    private String name;
    private double price;
    private int quantity;
    private double totalGST;
}
