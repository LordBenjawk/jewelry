package com.ea.jewelry.repository;

import com.ea.jewelry.domain.PurchaseOrder;
import com.ea.jewelry.domain.PurchaseOrderDetails;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the PurchaseOrderDetails entity.
 */
public interface PurchaseOrderDetailsRepository extends JpaRepository<PurchaseOrderDetails,Long> {

    @Query("select distinct purchaseOrderDetails from PurchaseOrderDetails purchaseOrderDetails left join fetch purchaseOrderDetails.items")
    List<PurchaseOrderDetails> findAllWithEagerRelationships();

    @Query("select purchaseOrderDetails from PurchaseOrderDetails purchaseOrderDetails left join fetch purchaseOrderDetails.items where purchaseOrderDetails.id =:id")
    PurchaseOrderDetails findOneWithEagerRelationships(@Param("id") Long id);

    List<PurchaseOrderDetails> findAllByPurchaseOrder(PurchaseOrder purchaseOrder);

}
