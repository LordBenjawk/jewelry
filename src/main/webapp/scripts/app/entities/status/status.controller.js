'use strict';

angular.module('jewelryApp')
    .controller('StatusController', function ($scope, $state, $modal, Status, ParseLinks) {
      
        $scope.statuss = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Status.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.statuss.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.statuss = [];
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
            $scope.status = {
                name: null,
                id: null
            };
        };
    });
