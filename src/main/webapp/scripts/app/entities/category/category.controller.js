'use strict';

angular.module('jewelryApp')
    .controller('CategoryController', function ($scope, $state, $modal, Category, ParseLinks) {
      
        $scope.categorys = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Category.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.categorys.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.categorys = [];
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
            $scope.category = {
                name: null,
                categoryOrder: null,
                id: null
            };
        };
    });
