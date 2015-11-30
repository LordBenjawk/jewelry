'use strict';

angular.module('jewelryApp')
    .controller('PriceController', function ($scope, $state, $modal, Price, ParseLinks) {
      
        $scope.prices = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Price.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.prices.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.prices = [];
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
            $scope.price = {
                tOne: null,
                tTwo: null,
                tThree: null,
                vendorPrice: null,
                id: null
            };
        };
    });
