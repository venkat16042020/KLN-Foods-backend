package Food_Orders.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "feedbacks" )
public class Feedback {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
    private Long  id;

    private String email;

    private String text;
}
