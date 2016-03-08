'use strict';

angular.module('jewelryApp')
	.controller('UserInformationDeleteController', function($scope, $modalInstance, entity, UserInformation) {

        $scope.userInformation = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            UserInformation.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });