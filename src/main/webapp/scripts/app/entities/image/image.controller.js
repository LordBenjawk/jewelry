'use strict';

angular.module('jewelryApp')
    .controller('ImageController', function ($scope, $state, $modal, Image, ParseLinks) {
      
        $scope.images = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Image.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.images.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.images = [];
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
            $scope.image = {
                name: null,
                id: null
            };
        };
    });
