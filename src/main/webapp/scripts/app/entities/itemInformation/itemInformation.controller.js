'use strict';

angular.module('jewelryApp')
    .controller('ItemInformationController', function ($scope, $state, $modal, ItemInformation, ParseLinks) {
      
        $scope.itemInformations = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            ItemInformation.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.itemInformations = result;
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
            $scope.itemInformation = {
                itemNumber: null,
                vendorItemNumber: null,
                description: null,
                vip: false,
                id: null
            };
        };
    });
