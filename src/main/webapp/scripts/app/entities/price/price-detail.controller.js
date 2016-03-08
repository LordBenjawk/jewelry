'use strict';

angular.module('jewelryApp')
    .controller('PriceDetailController', function ($scope, $rootScope, $stateParams, entity, Price, Item) {
        $scope.price = entity;
        $scope.load = function (id) {
            Price.get({id: id}, function(result) {
                $scope.price = result;
            });
        };
        var unsubscribe = $rootScope.$on('jewelryApp:priceUpdate', function(event, result) {
            $scope.price = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
