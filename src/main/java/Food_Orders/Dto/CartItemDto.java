package Food_Orders.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CartItemDto {
    private String name;
    private Double price;
    private Integer quantity;
    private Double totalGST;


}

