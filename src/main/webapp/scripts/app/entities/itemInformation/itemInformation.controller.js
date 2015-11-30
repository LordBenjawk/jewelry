'use strict';

angular.module('jewelryApp')
    .controller('ItemInformationController', function ($scope, $state, $modal, ItemInformation, ParseLinks) {
      
        $scope.itemInformations = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            ItemInformation.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.itemInformations.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.itemInformations = [];
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
            $scope.itemInformation = {
                itemNumber: null,
                vendorItemNumber: null,
                description: null,
                active: null,
                vip: null,
                id: null
            };
        };
    });
