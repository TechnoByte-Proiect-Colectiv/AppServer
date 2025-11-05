package ProiectColectiv.Domain;
import org.hibernate.sql.ast.tree.predicate.BooleanExpressionPredicate;

import java.time.LocalDate;

public class Order {
    private Integer userID;
    private Integer productID;
    private LocalDate quantity;
    private String deliveryStatus;
}
