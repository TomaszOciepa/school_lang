package com.tom.payment_service.service;

import com.tom.payment_service.model.Buyer;
import com.tom.payment_service.model.Order;
import com.tom.payment_service.model.OrderResponse;
import com.tom.payment_service.model.Product;
import com.tom.payment_service.model.dto.CourseDto;
import com.tom.payment_service.model.dto.OrderDto;
import com.tom.payment_service.model.dto.StudentDto;
import com.tom.payment_service.model.payu.TokenResponse;
import com.tom.payment_service.service.payu.PayuService;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Value("${payu.client.id}")
    private String clientId;

    @Value("${payu.client.secret}")
    private String clientSecret;

    @Value("${payu.customer.ip}")
    private String customerIp;

    private final StudentServiceClient studentServiceClient;
    private final CourseServiceClient courseServiceClient;
    private final PayuService payuService;
    public PaymentServiceImpl(StudentServiceClient studentServiceClient, CourseServiceClient courseServiceClient, PayuService payuService) {
        this.studentServiceClient = studentServiceClient;
        this.courseServiceClient = courseServiceClient;
        this.payuService = payuService;
    }

    @Override
    public OrderResponse createPayment(OrderDto orderDto) {
        Order order = new Order();

        order.setNotifyUrl("");
        order.setCustomerIp(customerIp);
        order.setMerchantPosId(clientId);
        order.setDescription("Zakup kursu");
        order.setCurrencyCode("PLN");
        order.setTotalAmount(convertZlotyToGrosze(orderDto.getTotalAmount()));
        order.setExtOrderId(orderDto.getOrderNumber());

        StudentDto studentFromDb = new StudentDto();
        try {
            studentFromDb = studentServiceClient.getStudentById(orderDto.getStudentId());
        }catch (FeignException ex) {
            logger.error("FeignException occurred: {}", ex.getMessage());
        }

        Buyer buyer = new Buyer();
        buyer.setFirstName(studentFromDb.getFirstName());
        buyer.setLastName(studentFromDb.getLastName());
        buyer.setEmail(studentFromDb.getEmail());

        order.setBuyer(buyer);

        CourseDto courseFromDB = new CourseDto();

        try {
          courseFromDB = courseServiceClient.getCourseById(orderDto.getCourseId(), null);
        }catch (FeignException ex) {
            logger.error("FeignException occurred: {}", ex.getMessage());
        }
        Product product = new Product();
        product.setName(courseFromDB.getName());
        product.setQuantity("1");
        product.setUnitPrice(convertZlotyToGrosze(courseFromDB.getPrice()));

        List<Product> productList = new ArrayList<>();
        productList.add(product);
        order.setProducts(productList);
        TokenResponse authToken = new TokenResponse();
        try {
            authToken = payuService.getAuthToken(clientId, clientSecret);
        }catch (FeignException ex) {
            logger.error("FeignException occurred: {}", ex.getMessage());
        }
        OrderResponse orderResponse = new OrderResponse();

        try {
            orderResponse = payuService.createOrder(order, authToken.getAccessToken());
        }catch (FeignException ex) {
            logger.error("FeignException occurred: {}", ex.getMessage());
        }

        return orderResponse;
    }

    private String convertZlotyToGrosze(String price) {
        int priceAsInt = Integer.parseInt(price);
        int multipliedPrice = priceAsInt * 100;
        return String.valueOf(multipliedPrice);
    }
}
