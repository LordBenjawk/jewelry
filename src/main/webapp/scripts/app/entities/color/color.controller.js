'use strict';

angular.module('jewelryApp')
    .controller('ColorController', function ($scope, $state, $modal, Color, ParseLinks) {
      
        $scope.colors = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Color.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.colors.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.colors = [];
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
            $scope.color = {
                name: null,
                id: null
            };
        };
    });
