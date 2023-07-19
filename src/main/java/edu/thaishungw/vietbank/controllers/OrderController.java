package edu.thaishungw.vietbank.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.thaishungw.vietbank.models.Order;
import edu.thaishungw.vietbank.models.RequestObject;
import edu.thaishungw.vietbank.service.OrderService;
import edu.thaishungw.vietbank.service.UserService;
import jakarta.servlet.http.HttpSession;

@Controller
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @RequestMapping("/orders")
    public String index(Model model) {
        List<Order> orders = orderService.getAllOrder();
        model.addAttribute("orders", orders);
        return ("orderList");
    }
}
