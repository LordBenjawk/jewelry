'use strict';

angular.module('jewelryApp')
    .controller('PurchaseOrderDetailsController', function ($scope, $state, $modal, PurchaseOrderDetails, ParseLinks) {
      
        $scope.purchaseOrderDetailss = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            PurchaseOrderDetails.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.purchaseOrderDetailss = result;
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
            $scope.purchaseOrderDetails = {
                id: null
            };
        };
    });
