'use strict';

angular.module('jewelryApp')
    .controller('ShoppingCartCustomerController', function ($scope, $state, $modal, ShoppingCartService, ParseLinks) {

        $scope.shoppingCartDetails = [];
        $scope.loadAll = function() {
            ShoppingCartService.getShoppingCartItems().then(function (response) {
                $scope.shoppingCartDetails = response.data;
            });
        };

        $scope.loadAll();

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.shoppingCartDetails = [];
        };

        $scope.getCurrentPriceTier = function (price) {
            if ($scope.shoppingCartDetails && $scope.shoppingCartDetails.userInformation) {
                switch ($scope.shoppingCartDetails.userInformation.priceTier) {
                    case 1:
                        return price.tierOne;
                    case 2:
                        return price.tierTwo;
                    case 3:
                        return price.tierThree;
                    default:
                        return null;
                }
            }
        };

        $scope.placeOrder = function () {
            ShoppingCartService.placeOrder($scope.shoppingCartDetails);
        };
    });
