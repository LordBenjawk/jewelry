'use strict';

angular.module('jewelryApp')
	.controller('ShoppingCartDeleteController', function($scope, $modalInstance, entity, ShoppingCart) {

        $scope.shoppingCart = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            ShoppingCart.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });