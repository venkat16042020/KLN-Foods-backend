package Food_Orders.Repository;
import Food_Orders.Entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
public interface BillRepository extends JpaRepository<Bill,Long> {
}
