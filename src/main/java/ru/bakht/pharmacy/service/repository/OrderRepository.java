package ru.bakht.pharmacy.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bakht.pharmacy.service.model.Order;
import ru.bakht.pharmacy.service.model.dto.TotalOrdersProjection;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT SUM(o.quantity) AS totalQuantity, SUM(o.totalAmount) AS totalAmount "
            + "FROM Order o "
            + "WHERE o.orderDate BETWEEN :startDate AND :endDate")
    TotalOrdersProjection findTotalQuantityAndAmountByDateRange(@Param("startDate") Date startDate,
                                                                @Param("endDate") Date endDate);

    @Query("SELECT o "
            + "FROM Order o "
            + "JOIN o.customer c "
            + "WHERE c.phone = :phone")
    List<Order> findOrdersByCustomerPhone(@Param("phone") String phone);
}
