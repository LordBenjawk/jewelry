package com.ea.jewelry.repository;

import com.ea.jewelry.domain.PurchaseOrderDetails;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PurchaseOrderDetails entity.
 */
public interface PurchaseOrderDetailsRepository extends JpaRepository<PurchaseOrderDetails,Long> {

}
