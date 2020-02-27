package com.softserve.itacademy.kek.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.models.IOrder;
import com.softserve.itacademy.kek.models.IOrderEvent;
import com.softserve.itacademy.kek.models.impl.ActorRole;
import com.softserve.itacademy.kek.models.impl.Order;
import com.softserve.itacademy.kek.models.impl.OrderEvent;
import com.softserve.itacademy.kek.models.impl.OrderEventType;
import com.softserve.itacademy.kek.models.impl.Tenant;
import com.softserve.itacademy.kek.models.impl.User;
import com.softserve.itacademy.kek.repositories.ActorRepository;
import com.softserve.itacademy.kek.repositories.ActorRoleRepository;
import com.softserve.itacademy.kek.repositories.OrderEventRepository;
import com.softserve.itacademy.kek.repositories.OrderEventTypeRepository;
import com.softserve.itacademy.kek.repositories.OrderRepository;
import com.softserve.itacademy.kek.repositories.TenantRepository;
import com.softserve.itacademy.kek.repositories.UserRepository;
import com.softserve.itacademy.kek.services.IOrderEventService;
import com.softserve.itacademy.kek.services.IOrderService;

import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createOrdinaryTenant;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createOrdinaryUser;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.getOrder;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.getOrderEvent;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class OrderEventServiceIT extends AbstractTestNGSpringContextTests {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ActorRepository actorRepository;
    @Autowired
    private OrderEventTypeRepository orderEventTypeRepository;
    @Autowired
    private OrderEventRepository orderEventRepository;
    @Autowired
    private ActorRoleRepository actorRoleRepository;

    @Autowired
    private IOrderService orderService;
    @Autowired
    private IOrderEventService orderEventService;

    private ActorRole actorRole1;
    private ActorRole actorRole2;

    private OrderEventType orderEventType1;
    private OrderEventType orderEventType2;
    private OrderEventType orderEventType3;
    private OrderEventType orderEventType4;

    private User user;
    private User customer;
    private Tenant tenant;
    private Order order;

    @BeforeMethod
    public void setUp() {
        actorRole1 = new ActorRole();
        actorRole1.setName("CUSTOMER");
        actorRole2 = new ActorRole();
        actorRole2.setName("CURRIER");

        orderEventType1 = new OrderEventType();
        orderEventType1.setName("CREATED");

        orderEventType2 = new OrderEventType();
        orderEventType2.setName("ASSIGNED");

        orderEventType3 = new OrderEventType();
        orderEventType3.setName("STARTED");

        orderEventType4 = new OrderEventType();
        orderEventType4.setName("DELIVERED");

        actorRoleRepository.save(actorRole1);
        actorRoleRepository.save(actorRole2);

        orderEventTypeRepository.save(orderEventType1);
        orderEventTypeRepository.save(orderEventType2);
        orderEventTypeRepository.save(orderEventType3);
        orderEventTypeRepository.save(orderEventType4);

        user = createOrdinaryUser(1);
        customer = createOrdinaryUser(2);
        tenant = createOrdinaryTenant(1);

        User savedUser = userRepository.save(user);
        assertNotNull(savedUser);

        User savedCustomer = userRepository.save(customer);
        assertNotNull(savedCustomer);

        tenant.setTenantOwner(savedUser);
        Tenant savedTenant = tenantRepository.save(tenant);
        assertNotNull(savedTenant);

        order = getOrder(tenant);
    }

    @AfterMethod
    public void tearDown() {
        orderEventRepository.deleteAll();
        actorRepository.deleteAll();
        orderRepository.deleteAll();
        tenantRepository.deleteAll();
        userRepository.deleteAll();
        actorRoleRepository.deleteAll();
        orderEventTypeRepository.deleteAll();
    }

    @Rollback
    @Test
    public void createSuccess() {
        //when
        IOrder createdOrder = orderService.create(order, customer.getGuid());

        OrderEvent orderEvent = getOrderEvent(orderRepository.findByGuid(createdOrder.getGuid()), orderEventType2, actorRepository.findById(1L).get());

        IOrderEvent createdOrderEvent = orderEventService.create(orderEvent, createdOrder.getGuid());

        //than
        IOrderEvent foundOrderEvent = orderEventRepository.findByGuid(createdOrderEvent.getGuid());

        assertEquals(createdOrderEvent.getPayload(), foundOrderEvent.getPayload());
    }

    @Rollback
    @Test
    public void getAllEventsForOrderSuccess() {
        //when
        IOrder createdOrder = orderService.create(order, customer.getGuid());

        order = (Order) createdOrder;
        order.setIdOrder(2L);

        OrderEvent orderEvent1 = getOrderEvent(order, orderEventType2, actorRepository.findById(2L).get());
        OrderEvent orderEvent2 = getOrderEvent(order, orderEventType3, actorRepository.findById(2L).get());
        OrderEvent orderEvent3 = getOrderEvent(order, orderEventType4, actorRepository.findById(2L).get());

        orderEventRepository.save(orderEvent1);
        orderEventRepository.save(orderEvent2);
        orderEventRepository.save(orderEvent3);

        List<IOrderEvent> orderEventList = orderEventService.getAllEventsForOrder(order.getGuid());

        //then
        assertEquals(orderEventList.size(), 4);
        assertEquals(orderEventList.get(1).getGuid(), orderEvent1.getGuid());
        assertEquals(orderEventList.get(2).getGuid(), orderEvent2.getGuid());
        assertEquals(orderEventList.get(3).getGuid(), orderEvent3.getGuid());
    }
}
