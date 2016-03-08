'use strict';

angular.module('jewelryApp')
    .controller('ShoppingCartDetailController', function ($scope, $rootScope, $stateParams, entity, ShoppingCart, User) {
        $scope.shoppingCart = entity;
        $scope.load = function (id) {
            ShoppingCart.get({id: id}, function(result) {
                $scope.shoppingCart = result;
            });
        };
        var unsubscribe = $rootScope.$on('jewelryApp:shoppingCartUpdate', function(event, result) {
            $scope.shoppingCart = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
