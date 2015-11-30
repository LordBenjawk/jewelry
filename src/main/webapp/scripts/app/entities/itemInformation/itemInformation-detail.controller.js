'use strict';

angular.module('jewelryApp')
    .controller('ItemInformationDetailController', function ($scope, $rootScope, $stateParams, entity, ItemInformation, Item, User, Category) {
        $scope.itemInformation = entity;
        $scope.load = function (id) {
            ItemInformation.get({id: id}, function(result) {
                $scope.itemInformation = result;
            });
        };
        var unsubscribe = $rootScope.$on('jewelryApp:itemInformationUpdate', function(event, result) {
            $scope.itemInformation = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
