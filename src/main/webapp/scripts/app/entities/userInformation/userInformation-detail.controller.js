'use strict';

angular.module('jewelryApp')
    .controller('UserInformationDetailController', function ($scope, $rootScope, $stateParams, entity, UserInformation, User) {
        $scope.userInformation = entity;
        $scope.load = function (id) {
            UserInformation.get({id: id}, function(result) {
                $scope.userInformation = result;
            });
        };
        var unsubscribe = $rootScope.$on('jewelryApp:userInformationUpdate', function(event, result) {
            $scope.userInformation = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
