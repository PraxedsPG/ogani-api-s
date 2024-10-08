package com.ms.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.order.OrderApplicationTests;
import com.ms.order.dto.OrderDTO;
import com.ms.order.dto.OrderItemDTO;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ActiveProfiles("tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderControllerTests extends OrderApplicationTests {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private MockMvc mockMvc;

    private String id;

    @Autowired
    private OrderController controller;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        this.id = "65f5a824a2baa927d194b02c";
    }

    @Test
    @Order(1)
    public void testCreate() throws Exception {
        log.info("testCreate");

        // Create order items
        List<OrderItemDTO> orderItems = new ArrayList<>();
        orderItems.add(new OrderItemDTO("65f874feb544004f76a23a59", "Morango Orgânico", "Bandeja de morangos de 300g", new BigDecimal("2"), new BigDecimal("19.99"), new BigDecimal("19.99")));
        orderItems.add(new OrderItemDTO("65f4a2469fc0a90f375dba5f", " Saca de Cebola Nacional", "Saca de 1kg de cebola branca", new BigDecimal ("1"), new BigDecimal("2.75"), new BigDecimal("19.99")));

        // Create order DTO
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(id);
        orderDTO.setClient("Fabio Argona");
        orderDTO.setOrderStatus("Pending");
        orderDTO.setOrderItems(orderItems);
        orderDTO.setPaymentMethod("Credit Card");
        orderDTO.setPaymentInformation("Card ending in 1234, Exp: 12/26, CVV: ***");
        orderDTO.setOrderNotes("Discount: $5.00, Fees: $2.50");
        orderDTO.setCreated("15/03/2024");
        orderDTO.setUpdated(null);
        orderDTO.setStatus("Admin");

        this.mockMvc.perform( MockMvcRequestBuilders
                        .post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(orderDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andReturn();
    }
    @Test
    @Order(2)
    public void testFindAll() throws Exception {
        log.info("testFindAll");
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/orders"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty());
    }

    @Test
    @Order(3)
    public void testFindById() throws Exception {
        log.info("testFindById");
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/getId/" + id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());;
    }
    @Test
    @Order(4)
    public void testFindByClient() throws Exception {
        log.info("testFindByClient");
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/getClient/Fabio Argona"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.client").exists());;
    }

    @Test
    @Order(5)
    public void testUpdate() throws Exception {
        log.info("testUpdate");

        // Create order items
        List<OrderItemDTO> orderItems = new ArrayList<>();
        orderItems.add(new OrderItemDTO("65f874feb544004f76a23a59", "Morango Orgânico", "Bandeja de morangos de 300g", new BigDecimal("2"), new BigDecimal("19.99"), new BigDecimal("19.99")));
        orderItems.add(new OrderItemDTO("65f4a2469fc0a90f375dba5f", " Saca de Cebola Nacional", "Saca de 1kg de cebola branca", new BigDecimal("1"), new BigDecimal("2.75"), new BigDecimal("19.99")));

        // Create order DTO
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(id);
        orderDTO.setClient("Fabio Argona");
        orderDTO.setOrderStatus("Pending");
        orderDTO.setOrderItems(orderItems);
        orderDTO.setPaymentMethod("Credit Card");
        orderDTO.setPaymentInformation("Card ending in 1234, Exp: 12/26, CVV: ***");
        orderDTO.setOrderNotes("Discount: $5.00, Fees: $2.50");
        orderDTO.setCreated("15/03/2024");
        orderDTO.setUpdated(null);
        orderDTO.setStatus("Admin");

        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/orders/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(orderDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

    @Test
    @Order(6)
    public void testDelete() throws Exception {
        log.info("testDelete");
        this.mockMvc.perform( MockMvcRequestBuilders
                        .delete("/api/orders/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist());
    }

    @Test
    @Order(7)
    public void testEnumsStatusOrder() throws Exception {
        log.info("testFindAllOrders");
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/liststatusorders"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
