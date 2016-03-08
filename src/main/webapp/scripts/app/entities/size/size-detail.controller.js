'use strict';

angular.module('jewelryApp')
    .controller('SizeDetailController', function ($scope, $rootScope, $stateParams, entity, Size, Item) {
        $scope.size = entity;
        $scope.load = function (id) {
            Size.get({id: id}, function(result) {
                $scope.size = result;
            });
        };
        var unsubscribe = $rootScope.$on('jewelryApp:sizeUpdate', function(event, result) {
            $scope.size = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
