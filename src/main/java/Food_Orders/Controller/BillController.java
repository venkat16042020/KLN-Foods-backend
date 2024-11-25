package Food_Orders.Controller;

import Food_Orders.Entity.Bill;
import Food_Orders.Repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/bills")
public class BillController {

    @Autowired
    private BillRepository billRepository;

    @PostMapping("/add")
    public Bill createBill(@RequestBody Bill bill) {
        // Save the bill object
        return billRepository.save(bill);
    }

    @GetMapping("all")
    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bill> getBillById(@PathVariable Long id) {
        Optional<Bill> bill = billRepository.findById(id);
        if (bill.isPresent()) {
            return ResponseEntity.ok(bill.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bill> updateBill(@PathVariable Long id, @RequestBody Bill billDetails) {
        Optional<Bill> existingBill = billRepository.findById(id);
        if (existingBill.isPresent()) {
            Bill bill = existingBill.get();
            bill.setName(billDetails.getName());
            bill.setPrice(billDetails.getPrice());
            bill.setQuantity(billDetails.getQuantity());
            bill.setTotalAmount(billDetails.getTotalAmount());
            bill.setSumAllGST(billDetails.getSumAllGST());
            bill.setPhoneNumber(billDetails.getPhoneNumber());
            Bill updatedBill = billRepository.save(bill);
            return ResponseEntity.ok(updatedBill);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBill(@PathVariable Long id) {
        if (billRepository.existsById(id)) {
            billRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
