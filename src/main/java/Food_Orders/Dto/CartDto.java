package Food_Orders.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter


public class CartDto {
    private Long id;
    private double totalAmount;
    private String phoneNumber;
    private String email;
    private Date date;
    private AddressDto address;
    private List<CartItemDto> cartItems;
}
