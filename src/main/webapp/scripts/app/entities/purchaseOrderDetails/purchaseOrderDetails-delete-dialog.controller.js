'use strict';

angular.module('jewelryApp')
	.controller('PurchaseOrderDetailsDeleteController', function($scope, $modalInstance, entity, PurchaseOrderDetails) {

        $scope.purchaseOrderDetails = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            PurchaseOrderDetails.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });