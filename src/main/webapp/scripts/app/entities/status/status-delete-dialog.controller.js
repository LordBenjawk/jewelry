'use strict';

angular.module('jewelryApp')
	.controller('StatusDeleteController', function($scope, $modalInstance, entity, Status) {

        $scope.status = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Status.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });