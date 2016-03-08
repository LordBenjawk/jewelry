'use strict';

angular.module('jewelryApp')
    .controller('ShoppingCartController', function ($scope, $state, $modal, ShoppingCart, ParseLinks) {
      
        $scope.shoppingCarts = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            ShoppingCart.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.shoppingCarts = result;
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
            $scope.shoppingCart = {
                id: null
            };
        };
    });
