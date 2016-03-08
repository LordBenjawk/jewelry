'use strict';

angular.module('jewelryApp')
    .controller('ShoppingCartDetailsController', function ($scope, $state, $modal, ShoppingCartDetails, ParseLinks) {
      
        $scope.shoppingCartDetailss = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            ShoppingCartDetails.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.shoppingCartDetailss = result;
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
            $scope.shoppingCartDetails = {
                id: null
            };
        };
    });
