package Food_Orders.Controller;

import Food_Orders.Entity.Address;
import Food_Orders.Service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/addresses")
@CrossOrigin(origins = "http://localhost:3000")
public class AddressController {

    @Autowired
    private AddressService addressService;


    @PostMapping("add")
    public ResponseEntity<Address> createAddress(@RequestBody Address address) {
        Address createdAddress = addressService.saveAddress(address);
        return new ResponseEntity<>(createdAddress, HttpStatus.CREATED);
    }


    @GetMapping("all")
    public ResponseEntity<List<Address>> getAllAddresses() {
        List<Address> addresses = addressService.getAllAddresses();
        return ResponseEntity.ok(addresses);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Address> getAddressById(@PathVariable Long id) {
        Optional<Address> address = addressService.getAddressById(id);
        return address.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PutMapping("/{id}")
    public ResponseEntity<Address> updateAddress(
            @PathVariable Long id,
            @RequestBody Address address) {

        Optional<Address> addressOptional = addressService.getAddressById(id);
        if (addressOptional.isPresent()) {
            Address existingAddress = addressOptional.get();
            existingAddress.setHouseNumber(address.getHouseNumber());
            existingAddress.setLandMark(address.getLandMark());
            existingAddress.setStreet(address.getStreet());
            existingAddress.setZipCode(address.getZipCode());
            existingAddress.setCity(address.getCity());
            existingAddress.setState(address.getState());

            Address updatedAddress = addressService.saveAddress(existingAddress);
            return ResponseEntity.ok(updatedAddress);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long id) {
        Optional<Address> addressOptional = addressService.getAddressById(id);
        if (addressOptional.isPresent()) {
            addressService.deleteAddress(id);
            return ResponseEntity.ok("Address successfully deleted.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Address not found.");
        }
    }
}
