'use strict';

angular.module('jewelryApp')
    .controller('CategoryDetailController', function ($scope, $rootScope, $stateParams, entity, Category, Item, Status) {
        $scope.category = entity;
        $scope.load = function (id) {
            Category.get({id: id}, function(result) {
                $scope.category = result;
            });
        };
        var unsubscribe = $rootScope.$on('jewelryApp:categoryUpdate', function(event, result) {
            $scope.category = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
