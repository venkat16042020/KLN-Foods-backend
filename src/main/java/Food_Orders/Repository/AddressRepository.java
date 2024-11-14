package Food_Orders.Repository;

import Food_Orders.Entity.Address;
import Food_Orders.Entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address,Long> {
    Address findByCart(Cart cart);
}
