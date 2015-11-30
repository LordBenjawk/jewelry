'use strict';

angular.module('jewelryApp')
    .controller('PurchaseOrderController', function ($scope, $state, $modal, PurchaseOrder, ParseLinks) {
      
        $scope.purchaseOrders = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            PurchaseOrder.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.purchaseOrders.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.purchaseOrders = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.purchaseOrder = {
                pONumber: null,
                id: null
            };
        };
    });
