package Food_Orders.Dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Setter
@Getter
public class AddressDto {
    private String houseNumber;
    private String landMark;
    private String street;
    private String city;
    private String state;
    private String zipCode;

}
