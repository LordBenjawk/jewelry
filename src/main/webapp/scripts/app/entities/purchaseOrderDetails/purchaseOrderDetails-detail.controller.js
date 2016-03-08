'use strict';

angular.module('jewelryApp')
    .controller('PurchaseOrderDetailsDetailController', function ($scope, $rootScope, $stateParams, entity, PurchaseOrderDetails, Item, PurchaseOrder) {
        $scope.purchaseOrderDetails = entity;
        $scope.load = function (id) {
            PurchaseOrderDetails.get({id: id}, function(result) {
                $scope.purchaseOrderDetails = result;
            });
        };
        var unsubscribe = $rootScope.$on('jewelryApp:purchaseOrderDetailsUpdate', function(event, result) {
            $scope.purchaseOrderDetails = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
