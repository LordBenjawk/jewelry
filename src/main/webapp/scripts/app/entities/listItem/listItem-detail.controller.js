'use strict';

angular.module('jewelryApp')
    .controller('ListItemDetailController', function ($scope, $rootScope, $stateParams, entity, ListItem, ShoppingCartService, AlertService) {
        $scope.itemInformation = entity;

        $scope.load = function (id) {
            ListItem.get({id: id}, function(result) {
                $scope.itemInformation = result;
            });
        };
        var unsubscribe = $rootScope.$on('jewelryApp:listItemUpdate', function(event, result) {
            $scope.itemInformation = result;
        });
        $scope.$on('$destroy', unsubscribe);

        $scope.sendToCart = function (item) {
            ShoppingCartService.addItemIntoShoppingCart(item);
            item.quantity = 0;
        };
    });
