'use strict';

angular.module('jewelryApp')
	.controller('ShoppingCartDetailsDeleteController', function($scope, $modalInstance, entity, ShoppingCartDetails) {

        $scope.shoppingCartDetails = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            ShoppingCartDetails.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });