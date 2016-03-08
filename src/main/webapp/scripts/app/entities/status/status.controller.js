'use strict';

angular.module('jewelryApp')
    .controller('StatusController', function ($scope, $state, $modal, Status, ParseLinks) {
      
        $scope.statuss = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Status.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.statuss = result;
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
            $scope.status = {
                name: null,
                id: null
            };
        };
    });
