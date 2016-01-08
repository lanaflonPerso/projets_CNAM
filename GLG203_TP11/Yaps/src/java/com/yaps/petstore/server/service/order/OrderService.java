package com.yaps.petstore.server.service.order;

import com.yaps.petstore.common.dto.OrderDTO;
import com.yaps.petstore.common.exception.*;

import java.util.Map;
import javax.ejb.Remote;

/**
 * This interface gives a remote view of the OrderServiceBean. Any distant client that wants to call
 * a method on the OrderServiceBean has to use this interface.
 */
@Remote
public interface OrderService {

    // ======================================
    // =           Business methods         =
    // ======================================

    /**
     * Given a customer id and a shopping card, this method creates a Order. It first gets the
     * customer information, creates an Order domain object, uses the Order object to apply the
     * business rules for creation. It then creates OrderLines using the shopping cart
     * and sends back the OrderId.
     *
     * @param customerId cannot be null.
     * @param shoppingCart cannot be null.
     * @return the order id
     * @throws CreateException is thrown if a DomainException is caught
     *                         or a system failure is occurs
     * @throws CheckException  is thrown if a invalid data is found
     */
    String createOrder(final String customerId, Map shoppingCart) throws CreateException, CheckException;
    
    /**
     * Given a OrderDTO object, this method creates a Order. It first transforms
     * a OrderDTO into a Order domain object, uses the Order object to apply the
     * business rules for creation. It then transforms back the Order object into a
     * OrderDTO.
     *
     * @param orderDTO cannot be null.
     * @return the created OrderDTO
     * @throws CreateException is thrown if a DomainException is caught
     *                         or a system failure is occurs
     * @throws CheckException  is thrown if a invalid data is found
     */
    OrderDTO createOrder(OrderDTO orderDTO) throws CreateException, CheckException;

    /**
     * Given an id this method uses the Order domain object to load all the data of this
     * object. It then transforms this object into a OrderDTO and returns it.
     *
     * @param orderId identifier
     * @return OrderDTO
     * @throws ObjectNotFoundException is thrown if no object with this given id is found
     * @throws FinderException         is thrown if a DomainException is caught
     *                                 or a system failure is occurs
     * @throws CheckException          is thrown if a invalid data is found
     */
    OrderDTO findOrder(String orderId) throws FinderException, CheckException;

    /**
     * Given an id, this method finds an Order domain object and then calls its deletion
     * method.
     *
     * @param orderId identifier
     * @throws RemoveException is thrown if a DomainException is caught
     *                         or a system failure is occurs
     * @throws CheckException  is thrown if a invalid data is found
     */
    void deleteOrder(String orderId) throws RemoveException, CheckException;
}