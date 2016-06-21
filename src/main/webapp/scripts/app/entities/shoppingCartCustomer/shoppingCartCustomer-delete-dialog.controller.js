'use strict';

angular.module('jewelryApp')
	.controller('ShoppingCartCustomerDeleteController', function($scope, $modalInstance, entity, ShoppingCartService) {

        $scope.shoppingCart = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            ShoppingCartService.removeItemFromShoppingCart({id: id}).then(function (response) {
                $modalInstance.close(true);
            });
        };
    });
