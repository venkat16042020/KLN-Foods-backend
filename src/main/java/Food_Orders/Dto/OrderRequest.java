package Food_Orders.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {


    private Double totalAmount;
    private List<CartItemDto> items;
    private AddressDto address;
}