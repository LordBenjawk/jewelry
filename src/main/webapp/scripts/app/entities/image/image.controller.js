'use strict';

angular.module('jewelryApp')
    .controller('ImageController', function ($scope, $state, $modal, Image, ParseLinks) {
      
        $scope.images = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Image.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.images = result;
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
            $scope.image = {
                name: null,
                id: null
            };
        };
    });
