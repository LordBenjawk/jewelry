'use strict';

angular.module('jewelryApp')
    .controller('ItemController', function ($scope, $state, $modal, Item, ParseLinks) {
      
        $scope.items = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Item.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.items.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.items = [];
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
            $scope.item = {
                id: null
            };
        };
    });
