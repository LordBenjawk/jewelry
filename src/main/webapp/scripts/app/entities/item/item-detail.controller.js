'use strict';

angular.module('jewelryApp')
    .controller('ItemDetailController', function ($scope, $rootScope, $stateParams, entity, Item, ItemInformation, Category, Price, Sizes, Color, Image, PurchaseOrderDetails) {
        $scope.item = entity;
        $scope.load = function (id) {
            Item.get({id: id}, function(result) {
                $scope.item = result;
            });
        };
        var unsubscribe = $rootScope.$on('jewelryApp:itemUpdate', function(event, result) {
            $scope.item = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
