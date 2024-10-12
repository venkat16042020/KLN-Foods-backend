package Food_Orders.Repository;

import Food_Orders.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);  // Optional: Find by email for login
}
