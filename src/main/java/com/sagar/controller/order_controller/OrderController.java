package com.sagar.controller.order_controller;

import com.sagar.model.Order;
import com.sagar.model.Users;
import com.sagar.request.OrderRequest;
import com.sagar.response.PaymentResponse;
import com.sagar.service.UserService;
import com.sagar.service.order_service.OrderService;
import com.sagar.service.payment_service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/api")
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserService userService;

    @PostMapping("/order")
    public ResponseEntity<?> createOrder(
            @RequestBody OrderRequest req,
            @RequestHeader("Authorization") String jwt) {

        try {
            System.out.println("==> createOrder called");

            Users user = userService.findUserByJwtToken(jwt);
            System.out.println("==> user resolved: " + user.getEmail());

            Order order = orderService.createOrder(req, user);
            System.out.println("==> order created: id=" + order.getId() + ", total=" + order.getTotalPrice());

            // TEMP: bypass Stripe to see if order part is OK
            PaymentResponse res = new PaymentResponse();
            res.setPayment_url("https://example.com/mock-payment/" + order.getId());
            System.out.println("==> mock payment response created");

            return new ResponseEntity<>(res, HttpStatus.CREATED);

        } catch (Exception e) {
            e.printStackTrace(); // will show full stack trace in Render logs

            // return a readable error to frontend
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "message", e.getMessage(),
                            "type", e.getClass().getName()
                    ));
        }
    }


    @GetMapping("/order/user")
    public ResponseEntity<List<Order>> getOrderHistory(@RequestHeader("Authorization") String jwt) throws Exception {

        Users user = userService.findUserByJwtToken(jwt);
        List<Order> orders = orderService.getUsersOrder(user.getId());

        return new ResponseEntity<>(orders, HttpStatus.OK);

    }

}
