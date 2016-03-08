'use strict';

angular.module('jewelryApp')
    .controller('ShoppingCartDetailsDetailController', function ($scope, $rootScope, $stateParams, entity, ShoppingCartDetails, ShoppingCart, Item) {
        $scope.shoppingCartDetails = entity;
        $scope.load = function (id) {
            ShoppingCartDetails.get({id: id}, function(result) {
                $scope.shoppingCartDetails = result;
            });
        };
        var unsubscribe = $rootScope.$on('jewelryApp:shoppingCartDetailsUpdate', function(event, result) {
            $scope.shoppingCartDetails = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
