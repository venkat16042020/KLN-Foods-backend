package Food_Orders.Service;


import Food_Orders.Entity.Cart;
import Food_Orders.Repository.CartRepository;
import Food_Orders.Entity.CartItem;
import Food_Orders.Repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartRepository cartRepository;

    public Cart saveCart(Cart cart) {
        return cartRepository.save(cart);
    }
    public Optional<Cart> getCartById(Long cartId) {
        return cartRepository.findById(cartId);
    }
    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }
    public Cart updateCart(Cart cart) {
        return cartRepository.save(cart);
    }
    public void deleteCart(Long cartId) {
        cartRepository.deleteById(cartId);
    }
}
