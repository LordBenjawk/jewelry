'use strict';

angular.module('jewelryApp')
    .controller('StatusDetailController', function ($scope, $rootScope, $stateParams, entity, Status, Category, PurchaseOrder) {
        $scope.status = entity;
        $scope.load = function (id) {
            Status.get({id: id}, function(result) {
                $scope.status = result;
            });
        };
        var unsubscribe = $rootScope.$on('jewelryApp:statusUpdate', function(event, result) {
            $scope.status = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
