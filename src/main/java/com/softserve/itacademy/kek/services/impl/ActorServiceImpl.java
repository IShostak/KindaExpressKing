package com.softserve.itacademy.kek.services.impl;

import com.softserve.itacademy.kek.models.IActor;
import com.softserve.itacademy.kek.models.IOrder;
import com.softserve.itacademy.kek.models.IOrderEvent;
import com.softserve.itacademy.kek.models.impl.Actor;
import com.softserve.itacademy.kek.repositories.ActorRepository;
import com.softserve.itacademy.kek.repositories.ActorRoleRepository;
import com.softserve.itacademy.kek.repositories.TenantRepository;
import com.softserve.itacademy.kek.repositories.UserRepository;
import com.softserve.itacademy.kek.services.IActorService;
import com.softserve.itacademy.kek.services.IOrderEventService;
import com.softserve.itacademy.kek.services.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

/**
 * Service implementation for {@link IActorService}
 */
public class ActorServiceImpl implements IActorService {

    private final ActorRepository actorRepository;
    private final ActorRoleRepository actorRoleRepository;
    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    private final IOrderService orderService;
    private final IOrderEventService orderEventService;


    @Autowired
    public ActorServiceImpl(ActorRepository actorRepository,
                            ActorRoleRepository actorRoleRepository,
                            UserRepository userRepository,
                            TenantRepository tenantRepository, IOrderService orderService, IOrderEventService orderEventService) {
        this.actorRepository = actorRepository;
        this.actorRoleRepository = actorRoleRepository;
        this.userRepository = userRepository;
        this.tenantRepository = tenantRepository;
        this.orderService = orderService;
        this.orderEventService = orderEventService;
    }


    @Override
    public IActor create(IOrderEvent iOrderEvent, String userGuid, String orderGuid) {

        IOrder order = orderService.getByGuid(UUID.fromString(orderGuid));
        orderEventService.create(iOrderEvent, order.getGuid());


        Actor actor = new Actor();
        actor.setGuid(UUID.randomUUID());
//        actor.set
return actor;
    }
}
