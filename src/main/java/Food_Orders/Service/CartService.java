package Food_Orders.Service;

import Food_Orders.Entity.Address;
import Food_Orders.Entity.Cart;
import Food_Orders.Repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class CartService {
    @Autowired
    private AddressRepository addressRepository;
    public Address getAddressForCart(Cart cart) {
        return addressRepository.findByCart(cart);
    }
}
