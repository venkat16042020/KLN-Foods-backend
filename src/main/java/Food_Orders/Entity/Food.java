package Food_Orders.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Food_Items")
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private double price;
    private double stateGST;
    private double centralGST;

    private double totalGST;

    private String description;

    private double totalPrice;

    private String categoryName;
    private String imageUrl;
}
