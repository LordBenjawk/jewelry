'use strict';

angular.module('jewelryApp')
    .controller('ItemController', function ($scope, $state, $modal, Item, ParseLinks) {
      
        $scope.items = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Item.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.items = result;
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
            $scope.item = {
                name: null,
                id: null
            };
        };
    });
