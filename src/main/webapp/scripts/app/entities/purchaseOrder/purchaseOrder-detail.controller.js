'use strict';

angular.module('jewelryApp')
    .controller('PurchaseOrderDetailController', function ($scope, $rootScope, $stateParams, entity, PurchaseOrder, PurchaseOrderDetails, User, Status) {
        $scope.purchaseOrder = entity;
        $scope.load = function (id) {
            PurchaseOrder.get({id: id}, function(result) {
                $scope.purchaseOrder = result;
            });
        };
        var unsubscribe = $rootScope.$on('jewelryApp:purchaseOrderUpdate', function(event, result) {
            $scope.purchaseOrder = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
