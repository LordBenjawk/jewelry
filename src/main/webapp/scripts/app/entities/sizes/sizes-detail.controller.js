'use strict';

angular.module('jewelryApp')
    .controller('SizesDetailController', function ($scope, $rootScope, $stateParams, entity, Sizes, Item) {
        $scope.sizes = entity;
        $scope.load = function (id) {
            Sizes.get({id: id}, function(result) {
                $scope.sizes = result;
            });
        };
        var unsubscribe = $rootScope.$on('jewelryApp:sizesUpdate', function(event, result) {
            $scope.sizes = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
