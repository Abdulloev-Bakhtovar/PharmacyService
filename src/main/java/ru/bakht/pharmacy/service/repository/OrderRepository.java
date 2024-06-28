package ru.bakht.pharmacy.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bakht.pharmacy.service.model.Order;
import ru.bakht.pharmacy.service.model.dto.TotalOrdersProjection;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    @Query("SELECT SUM(o.quantity) AS totalQuantity, SUM(o.totalAmount) AS totalAmount "
            + "FROM Order o "
            + "WHERE o.orderDate BETWEEN :startDate AND :endDate")
    TotalOrdersProjection findTotalQuantityAndAmountByDateRange(@Param("startDate") LocalDate startDate,
                                                                @Param("endDate") LocalDate endDate);

    @Query("SELECT o "
            + "FROM Order o "
            + "JOIN o.customer c "
            + "WHERE c.phone = :phone")
    List<Order> findOrdersByCustomerPhone(@Param("phone") String phone);
}
