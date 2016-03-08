'use strict';

angular.module('jewelryApp')
    .controller('ColorDetailController', function ($scope, $rootScope, $stateParams, entity, Color, Item) {
        $scope.color = entity;
        $scope.load = function (id) {
            Color.get({id: id}, function(result) {
                $scope.color = result;
            });
        };
        var unsubscribe = $rootScope.$on('jewelryApp:colorUpdate', function(event, result) {
            $scope.color = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
