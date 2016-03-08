'use strict';

angular.module('jewelryApp')
    .controller('PurchaseOrderController', function ($scope, $state, $modal, PurchaseOrder, ParseLinks) {
      
        $scope.purchaseOrders = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            PurchaseOrder.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.purchaseOrders = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.purchaseOrder = {
                purchaseOrderNumber: null,
                id: null
            };
        };
    });
