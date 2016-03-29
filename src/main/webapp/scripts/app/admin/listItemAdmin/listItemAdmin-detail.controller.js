'use strict';

angular.module('jewelryApp')
    .controller('ListItemAdminDetailController', function ($scope, $rootScope, $stateParams, entity, ListItem, FileUploader) {
        $scope.itemInformation = entity;

        $scope.load = function (id) {
            ListItem.get({id: id}, function(result) {
                $scope.itemInformation = result;
            });
        };
        var unsubscribe = $rootScope.$on('jewelryApp:listItemUpdate', function(event, result) {
            $scope.itemInformation = result;
        });
        $scope.$on('$destroy', unsubscribe);

        $scope.$on('jewelryApp:listItemAdminUpdate', function(event, result) {
            $scope.load(result)
        });

    });
