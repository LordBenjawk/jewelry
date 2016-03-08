'use strict';

angular.module('jewelryApp')
    .controller('ImageDetailController', function ($scope, $rootScope, $stateParams, entity, Image, Item) {
        $scope.image = entity;
        $scope.load = function (id) {
            Image.get({id: id}, function(result) {
                $scope.image = result;
            });
        };
        var unsubscribe = $rootScope.$on('jewelryApp:imageUpdate', function(event, result) {
            $scope.image = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
