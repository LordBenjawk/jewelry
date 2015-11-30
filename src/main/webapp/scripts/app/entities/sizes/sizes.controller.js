'use strict';

angular.module('jewelryApp')
    .controller('SizesController', function ($scope, $state, $modal, Sizes, ParseLinks) {
      
        $scope.sizess = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Sizes.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.sizess.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.sizess = [];
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
            $scope.sizes = {
                name: null,
                id: null
            };
        };
    });
