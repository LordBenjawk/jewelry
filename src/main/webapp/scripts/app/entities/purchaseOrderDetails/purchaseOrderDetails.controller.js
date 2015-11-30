'use strict';

angular.module('jewelryApp')
    .controller('PurchaseOrderDetailsController', function ($scope, $state, $modal, PurchaseOrderDetails, ParseLinks) {
      
        $scope.purchaseOrderDetailss = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            PurchaseOrderDetails.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.purchaseOrderDetailss.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.purchaseOrderDetailss = [];
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
            $scope.purchaseOrderDetails = {
                id: null
            };
        };
    });
