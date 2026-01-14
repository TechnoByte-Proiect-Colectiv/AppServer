package ProiectColectiv.Service.RestControllers;

import ProiectColectiv.Domain.Address;
import ProiectColectiv.Domain.CartItem;
import ProiectColectiv.Domain.DTOs.*;
import ProiectColectiv.Domain.Order;
import ProiectColectiv.Domain.Product;
import ProiectColectiv.Repository.Interfaces.ICartItemRepo;
import ProiectColectiv.Repository.Interfaces.IOrderRepo;
import ProiectColectiv.Repository.Interfaces.IProductRepo;
import ProiectColectiv.Repository.Interfaces.IUserRepo;
import ProiectColectiv.Repository.Utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired private IOrderRepo orderRepo;
    @Autowired private ICartItemRepo cartItemRepo;
    @Autowired private IUserRepo userRepo;
    @Autowired private JWTUtils jwtUtils;
    @Autowired private IProductRepo productRepo;

    @GetMapping("/my-orders")
    public ResponseEntity<?> getMyOrders(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            if (!jwtUtils.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
            }

            String userId = jwtUtils.extractEmail(token);

            Iterable<Order> orders = orderRepo.getAllOrdersByUser(userId);
            List<OrderSummaryDTO> summaryList = new ArrayList<>();

            for (Order order : orders) {
                OrderSummaryDTO dto = new OrderSummaryDTO();
                dto.setId(order.getId());
                dto.setCreatedAt(order.getOrderDate());
                dto.setTotal(order.getTotalPrice());
                dto.setCurrency(order.getCurrency());

                List<String> images = new ArrayList<>();

                Iterable<CartItem> items = cartItemRepo.findAllForOrder(String.valueOf(order.getId()));

                if (items != null) {
                    int count = 0;
                    for (CartItem item : items) {
                        if (count >= 5) break;

                        String imgUrl = "http://localhost:8080/api/product/" + item.getProductID() + "/image";

                        images.add(imgUrl);
                        count++;
                    }
                }

                dto.setPreviewImages(images);
                summaryList.add(dto);
            }

            Collections.reverse(summaryList);

            return new ResponseEntity<>(summaryList, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching orders");
        }
    }

    // Get Order by ID
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@RequestHeader("Authorization") String authHeader, @PathVariable Integer orderId) {
        try {
            String token = authHeader.replace("Bearer ", "");
            if (!jwtUtils.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
            }
            String userEmail = jwtUtils.extractEmail(token);

            Order order = orderRepo.findById(orderId);
            if (order == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
            }

            if (!order.getUserID().equals(userEmail)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied.");
            }

            OrderResponseDTO response = new OrderResponseDTO();
            response.setId(order.getId());
            response.setCreatedAt(order.getOrderDate());
            response.setTotal(order.getTotalPrice());
            response.setTotalProducts(order.getTotalProducts());
            response.setTotalShipping(order.getTotalShipping());
            response.setCurrency(order.getCurrency());
            response.setOrderStatus(order.getDeliveryStatus());
            response.setPaymentMethod(order.getPaymentMethod());
            response.setPaymentStatus(order.getPaymentStatus());

            response.setBillingAddress(parseToDomainAddress(order.getBillingAddress(), "billing"));
            response.setShippingAddress(parseToDomainAddress(order.getShippingAddress(), "shipping"));

            OrderShipmentDTO shipment = new OrderShipmentDTO();
            shipment.setSellerId("1");
            shipment.setSellerName("Main Store");
            shipment.setStatus(order.getDeliveryStatus());
            shipment.setShippingCost(order.getTotalShipping() != null ? order.getTotalShipping() : 0f);
            shipment.setShippingCurrency(order.getCurrency());

            List<OrderShipmentItemDTO> shipmentItems = new ArrayList<>();

            Iterable<CartItem> dbItems = cartItemRepo.findAllForOrder(String.valueOf(orderId));

            if (dbItems != null) {
                for (CartItem dbItem : dbItems) {
                    OrderShipmentItemDTO itemDTO = new OrderShipmentItemDTO();
                    itemDTO.setProductId(dbItem.getProductID());
                    itemDTO.setQuantity(dbItem.getNrOrdered());

                    Product product = productRepo.findById(dbItem.getProductID());
                    if (product != null) {
                        itemDTO.setProductName(product.getName());
                        String imageUrl = "http://localhost:8080/api/product/" + product.getId() + "/image";
                        itemDTO.setPicture(imageUrl);
                        itemDTO.setUnitPrice(product.getPrice());
                        itemDTO.setCurrency(product.getCurrency());
                    } else {
                        itemDTO.setProductName("Product Unavailable");
                        itemDTO.setUnitPrice(0f);
                        itemDTO.setCurrency("RON");
                    }
                    shipmentItems.add(itemDTO);
                }
            }

            shipment.setItems(shipmentItems);
            response.setShipments(Collections.singletonList(shipment));

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> addOrder(@RequestHeader("Authorization") String authHeader, @RequestBody OrderRequestDTO orderRequest) {
        try {
            String token = authHeader.replace("Bearer ", "");
            if (!jwtUtils.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
            }
            String userEmail = jwtUtils.extractEmail(token);

            Order newOrder = new Order();

            newOrder.setUserID(userEmail);
            newOrder.setOrderDate(LocalDate.now());
            newOrder.setTotalProducts(orderRequest.getTotal());
            newOrder.setTotalPrice(orderRequest.getTotal());

            newOrder.setBillingAddress(orderRequest.getBillingAddress().toString());
            newOrder.setShippingAddress(orderRequest.getShippingAddress().toString());

            newOrder.setDeliveryStatus("PENDING");
            newOrder.setPaymentStatus(false);
            newOrder.setCurrency("RON");
            newOrder.setPaymentMethod("CARD");

            int generatedOrderId = orderRepo.saveWithReturn(newOrder);

            if (generatedOrderId == -1) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save order in DB");
            }

            if (orderRequest.getItems() != null) {
                for (OrderItemDTO itemDTO : orderRequest.getItems()) {
                    CartItem cartItem = new CartItem();
                    cartItem.setOrderID(generatedOrderId);
                    cartItem.setProductID(itemDTO.getProductId());
                    cartItem.setNrOrdered(itemDTO.getQuantity());

                    cartItemRepo.save(cartItem);
                }
            }

            return new ResponseEntity<>("Order placed successfully with ID: " + generatedOrderId, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error placing order: " + e.getMessage());
        }
    }

    private Address parseToDomainAddress(String rawAddress, String type) {
        Address addr = new Address();
        addr.setId(UUID.randomUUID().toString());
        addr.setType(type);
        addr.setPrimary(false);

        if (rawAddress == null || rawAddress.isEmpty()) {
            return addr;
        }

        try {
            String[] parts = rawAddress.split(", ");

            if (parts.length > 0) {
                String fullName = parts[0];
                String[] names = fullName.split(" ");
                if (names.length >= 2) {
                    addr.setFirstName(names[0]);
                    addr.setLastName(fullName.substring(names[0].length()).trim());
                } else {
                    addr.setFirstName(fullName);
                    addr.setLastName("");
                }
            }

            // 2. Street
            if (parts.length > 1) addr.setStreet(parts[1]);

            // 3. City
            if (parts.length > 2) addr.setCity(parts[2]);

            // 4. Postal Code
            if (parts.length > 3) addr.setPostalCode(parts[3]);

            // 5. Phone
            if (parts.length > 4) {
                String phonePart = parts[4];
                if (phonePart.startsWith("Tel: ")) {
                    addr.setPhoneNumber(phonePart.substring(5));
                } else {
                    addr.setPhoneNumber(phonePart);
                }
            }

            // Default values
            addr.setCountry("Romania");
            addr.setCounty("-");

        } catch (Exception e) {
            // Fallback
            addr.setStreet(rawAddress);
            addr.setFirstName("Unknown");
        }

        return addr;
    }
}