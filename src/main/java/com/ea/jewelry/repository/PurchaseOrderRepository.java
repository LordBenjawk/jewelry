package com.ea.jewelry.repository;

import com.ea.jewelry.domain.PurchaseOrder;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PurchaseOrder entity.
 */
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder,Long> {

    @Query("select purchaseOrder from PurchaseOrder purchaseOrder where purchaseOrder.user.login = ?#{principal.username}")
    List<PurchaseOrder> findByUserIsCurrentUser();

}
