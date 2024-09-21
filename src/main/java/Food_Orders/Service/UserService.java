package Food_Orders.Service;
import Food_Orders.Entity.User;
import java.util.List;

public interface UserService {

    User createUser(User user);

    List<User> getAllUsers();

    User getUserById(Long id);

    User updateUser(Long id, User userDetails);

    void deleteUser(Long id);
}

