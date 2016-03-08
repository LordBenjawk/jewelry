'use strict';

angular.module('jewelryApp')
    .controller('UserInformationController', function ($scope, $state, $modal, UserInformation, ParseLinks) {
      
        $scope.userInformations = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            UserInformation.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.userInformations = result;
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
            $scope.userInformation = {
                priceTier: null,
                vip: null,
                id: null
            };
        };
    });
