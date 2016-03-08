'use strict';

angular.module('jewelryApp')
    .controller('SizeController', function ($scope, $state, $modal, Size, ParseLinks) {
      
        $scope.sizes = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Size.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.sizes = result;
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
            $scope.size = {
                name: null,
                id: null
            };
        };
    });
