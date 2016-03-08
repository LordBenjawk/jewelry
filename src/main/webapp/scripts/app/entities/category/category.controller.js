'use strict';

angular.module('jewelryApp')
    .controller('CategoryController', function ($scope, $state, $modal, Category, ParseLinks) {
      
        $scope.categorys = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Category.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.categorys = result;
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
            $scope.category = {
                name: null,
                categoryOrder: null,
                id: null
            };
        };
    });
