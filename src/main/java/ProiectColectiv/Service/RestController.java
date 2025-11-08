package ProiectColectiv.Service;

import ProiectColectiv.Repository.DatabaseRepo.ProductRepo;
import ProiectColectiv.Repository.DatabaseRepo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@CrossOrigin(origins = "*")
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
public class RestController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ProductRepo productRepo;
}
