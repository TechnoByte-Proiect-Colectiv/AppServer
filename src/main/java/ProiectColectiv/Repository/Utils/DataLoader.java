package ProiectColectiv.Repository.Utils;

import ProiectColectiv.Domain.Product;
import ProiectColectiv.Domain.Review;
import ProiectColectiv.Repository.Interfaces.IProductRepo;
import ProiectColectiv.Repository.Interfaces.IReviewRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private IProductRepo productRepository;

    @Autowired
    private IReviewRepo reviewRepository;

    private final Random random = new Random();

    @Override
    public void run(String... args) throws Exception {
        List<Product> existingProducts = new ArrayList<>();
        productRepository.getAllProducts().forEach(existingProducts::add);

        if (existingProducts.isEmpty()) {
            System.out.println("Generez produse cu imagini random...");

            List<Product> products = generateProducts();

            for (Product product : products) {
                productRepository.save(product);

                // 🔥 DUPĂ SAVE → reviews random
                generateRandomReviews(product.getId());
            }

            System.out.println("Au fost adăugate " + products.size() + " produse cu reviews!");
        }
    }

    private void generateRandomReviews(Integer productId) {
        int numberOfReviews = random.nextInt(6) + 1; // 1–6 reviews

        for (int i = 0; i < numberOfReviews; i++) {
            String userId = "user" + random.nextInt(20) + "@test.com"; // TU modifici
            int rating = random.nextInt(5) + 1;

            String title = REVIEW_TITLES[random.nextInt(REVIEW_TITLES.length)];
            String description = REVIEW_DESCRIPTIONS[random.nextInt(REVIEW_DESCRIPTIONS.length)];

            Review review = new Review(
                    userId,
                    productId,
                    rating,
                    title,
                    description,
                    LocalDate.now().minusDays(random.nextInt(365)),
                    random.nextBoolean()
            );

            reviewRepository.save(review);
        }
    }

    private List<Product> generateProducts() {
        List<Product> products = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            String productName = "Product " + (i + 1);

            Product product = new Product(
                    productName,
                    "Descriere pentru " + productName,
                    "product-" + (i + 1),
                    "Brand " + random.nextInt(10),
                    (float) (random.nextInt(5000) + 100),
                    random.nextInt(100) + 1,
                    "RON",
                    random.nextInt(500),
                    "Category " + random.nextInt(5),
                    downloadRandomImage()
            );

            products.add(product);
        }

        return products;
    }

    private byte[] downloadRandomImage() {
        try {
            int imageId = random.nextInt(100) + 1;
            URL url = new URL("https://picsum.photos/id/" + imageId + "/400/400");

            try (InputStream in = url.openStream()) {
                return in.readAllBytes();
            }
        } catch (IOException e) {
            System.err.println("Eroare la descărcarea imaginii: " + e.getMessage());
            return null;
        }
    }

    private static final String[] REVIEW_TITLES = {
            "Foarte bun", "Excelent", "Nu recomand", "Merita banii",
            "Slab", "Peste asteptari", "Calitate ok", "Dezamagitor"
    };

    private static final String[] REVIEW_DESCRIPTIONS = {
            "Produsul este exact ca in descriere.",
            "Livrare rapida, produs bun.",
            "Calitatea lasa de dorit.",
            "Raport calitate-pret foarte bun.",
            "Nu as mai cumpara.",
            "Sunt multumit de achizitie.",
            "Materiale decente.",
            "Functioneaza perfect."
    };
}
