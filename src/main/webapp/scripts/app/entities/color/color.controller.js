'use strict';

angular.module('jewelryApp')
    .controller('ColorController', function ($scope, $state, $modal, Color, ParseLinks) {
      
        $scope.colors = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Color.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.colors = result;
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
            $scope.color = {
                name: null,
                id: null
            };
        };
    });
