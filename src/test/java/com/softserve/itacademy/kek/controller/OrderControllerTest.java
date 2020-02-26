package com.softserve.itacademy.kek.controller;

import java.util.UUID;

import com.google.gson.Gson;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.softserve.itacademy.kek.dto.OrderDetailsDto;
import com.softserve.itacademy.kek.dto.OrderDto;
import com.softserve.itacademy.kek.dto.OrderEventDto;
import com.softserve.itacademy.kek.dto.OrderEventListDto;
import com.softserve.itacademy.kek.dto.OrderEventTypesDto;
import com.softserve.itacademy.kek.dto.OrderListDto;
import com.softserve.itacademy.kek.services.IOrderService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Test(groups = {"unit-tests"})
public class OrderControllerTest {
    private final Gson gson = new Gson();

    private OrderEventDto orderEventDto;
    private OrderEventListDto orderEventListDto;
    private OrderDto orderDto;
    private OrderListDto orderListDto;
    private OrderDetailsDto orderDetailsDto;

    private UUID guid;
    private UUID tenantGuid;

    @InjectMocks
    private OrderController controller;
    @Mock
    private IOrderService orderService;

    private MockMvc mockMvc;

    @BeforeTest
    public void setup() {
        guid = UUID.fromString("123");
        tenantGuid = UUID.fromString("MyTenant");
        orderDetailsDto = new OrderDetailsDto("some info", "https://mypicture");
        orderDto = new OrderDto(guid, tenantGuid, "summary", orderDetailsDto);
        orderListDto = new OrderListDto().addOrder(orderDto);
        orderEventDto = new OrderEventDto(guid, orderDto, "some info", OrderEventTypesDto.DELIVERED);
        orderEventListDto = new OrderEventListDto(UUID.randomUUID()).addOrderEvent(orderEventDto);

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

    }

    @Test
    public void getOrderListTest() throws Exception {
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/vnd.softserve.order+json"))
                .andExpect(jsonPath("$.orderList[0].tenantGuid").value(tenantGuid))
                .andExpect(jsonPath("$.orderList[0].guid").value(guid))
                .andExpect(jsonPath("$.orderList[0].details.payload").value("some info"))
                .andExpect(jsonPath("$.orderList[0].details.imageUrl").value("https://mypicture"));
    }

    @Test
    public void addOrderTest() throws Exception {
        mockMvc.perform(post("/orders")
                .contentType("application/vnd.softserve.order+json")
                .accept("application/vnd.softserve.order+json")
                .content(gson.toJson(orderDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderList[0].tenantGuid").value("MyTenant"))
                .andExpect(jsonPath("$.orderList[0].guid").value("123"))
                .andExpect(jsonPath("$.orderList[0].summary").value("summary"))
                .andExpect(jsonPath("$.orderList[0].details.payload").value("some info"))
                .andExpect(jsonPath("$.orderList[0].details.imageUrl").value("https://mypicture"));
    }

    @Test
    public void getOrderTest() throws Exception {
        mockMvc.perform(get("/orders/123"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/vnd.softserve.order+json"))
                .andExpect(jsonPath("$.tenant").value("MyTenant"))
                .andExpect(jsonPath("$.guid").value("123"))
                .andExpect(jsonPath("$.orderList[0].summary").value("summary"))
                .andExpect(jsonPath("$.details.payload").value("some info"))
                .andExpect(jsonPath("$.details.imageUrl").value("https://mypicture"));
    }

    @Test
    public void modifyOrderTest() throws Exception {
        mockMvc.perform(put("/orders/123")
                .contentType("application/vnd.softserve.order+json")
                .accept("application/vnd.softserve.order+json")
                .content(gson.toJson(orderDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tenant").value("MyTenant"))
                .andExpect(jsonPath("$.guid").value("123"))
                .andExpect(jsonPath("$.orderList[0].summary").value("summary"))
                .andExpect(jsonPath("$.details.payload").value("some info"))
                .andExpect(jsonPath("$.details.imageUrl").value("https://mypicture"));
    }

    @Test
    public void deleteOrderTest() throws Exception {
        mockMvc.perform(delete("/orders/123"))
                .andExpect(status().isOk());
    }

    @Test
    public void getEventsTest() throws Exception {
        mockMvc.perform(get("/orders/123/events"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/vnd.softserve.event+json"))
                .andExpect(jsonPath("$.orderEventList[0].guid").value("wqewqe1r1"))
                .andExpect(jsonPath("$.tenant").value("MyTenant"))
                .andExpect(jsonPath("$.guid").value("123"))
                .andExpect(jsonPath("$.orderList[0].summary").value("summary"))
                .andExpect(jsonPath("$.details.payload").value("some info"))
                .andExpect(jsonPath("$.details.imageUrl").value("https://mypicture"))
                .andExpect(jsonPath("$.orderEventList[0].payload").value("some info"))
                .andExpect(jsonPath("$.orderEventList[0].type").value("DELIVERED"));
    }

    @Test
    public void addEventTest() throws Exception {
        mockMvc.perform(post("/orders/123/events")
                .contentType("application/vnd.softserve.event+json")
                .accept("application/vnd.softserve.event+json")
                .content(gson.toJson(orderEventDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/vnd.softserve.event+json"))
                .andExpect(jsonPath("$.guid").value("wqewqe1r1"))
                .andExpect(jsonPath("$.tenant").value("MyTenant"))
                .andExpect(jsonPath("$.guid").value("123"))
                .andExpect(jsonPath("$.orderList[0].summary").value("summary"))
                .andExpect(jsonPath("$.details.payload").value("some info"))
                .andExpect(jsonPath("$.details.imageUrl").value("https://mypicture"))
                .andExpect(jsonPath("$.payload").value("some info"))
                .andExpect(jsonPath("$.type").value("DELIVERED"));
    }
}
