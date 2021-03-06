package com.softserve.itacademy.kek.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.softserve.itacademy.kek.configuration.PersistenceTestConfig;
import com.softserve.itacademy.kek.models.enums.ActorRoleEnum;
import com.softserve.itacademy.kek.models.enums.EventTypeEnum;
import com.softserve.itacademy.kek.models.enums.IdentityTypeEnum;
import com.softserve.itacademy.kek.models.impl.ActorRole;
import com.softserve.itacademy.kek.models.impl.IdentityType;
import com.softserve.itacademy.kek.models.impl.OrderEventType;
import com.softserve.itacademy.kek.repositories.ActorRoleRepository;
import com.softserve.itacademy.kek.repositories.IdentityTypeRepository;
import com.softserve.itacademy.kek.repositories.OrderEventTypeRepository;


@ContextConfiguration(classes = {PersistenceTestConfig.class})
public class WriteConstantsToDB extends AbstractTestNGSpringContextTests {

    @Autowired
    private IdentityTypeRepository identityTypeRepository;

    @Autowired
    private ActorRoleRepository actorRoleRepository;

    @Autowired
    private OrderEventTypeRepository orderEventTypeRepository;

    private IdentityType identityType1;

    private ActorRole actorRole1;
    private ActorRole actorRole2;

    private OrderEventType orderEventType1;
    private OrderEventType orderEventType2;
    private OrderEventType orderEventType3;
    private OrderEventType orderEventType4;


    @BeforeMethod(groups = {"integration-tests"})
    public void setUp() {

        identityType1 = new IdentityType();
        identityType1.setName(IdentityTypeEnum.KEY.toString());

        actorRole1 = new ActorRole();
        actorRole1.setName(ActorRoleEnum.CUSTOMER.toString());
        actorRole2 = new ActorRole();
        actorRole2.setName(ActorRoleEnum.CURRIER.toString());

        orderEventType1 = new OrderEventType();
        orderEventType1.setName(EventTypeEnum.CREATED.toString());

        orderEventType2 = new OrderEventType();
        orderEventType2.setName(EventTypeEnum.ASSIGNED.toString());

        orderEventType3 = new OrderEventType();
        orderEventType3.setName(EventTypeEnum.STARTED.toString());

        orderEventType4 = new OrderEventType();
        orderEventType4.setName(EventTypeEnum.DELIVERED.toString());
    }

    @AfterMethod(groups = {"integration-tests"})
    public void tearDown() {
        identityTypeRepository.deleteAll();
        actorRoleRepository.deleteAll();
        orderEventTypeRepository.deleteAll();
    }

    @Test(groups = {"integration-tests"})
    public void saveActorRolesAndOrderEventTypesToDb() {

        identityTypeRepository.save(identityType1);

        actorRoleRepository.save(actorRole1);
        actorRoleRepository.save(actorRole2);

        orderEventTypeRepository.save(orderEventType1);
        orderEventTypeRepository.save(orderEventType2);
        orderEventTypeRepository.save(orderEventType3);
        orderEventTypeRepository.save(orderEventType4);
    }
}
