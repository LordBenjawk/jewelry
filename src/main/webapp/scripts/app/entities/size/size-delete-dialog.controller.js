'use strict';

angular.module('jewelryApp')
	.controller('SizeDeleteController', function($scope, $modalInstance, entity, Size) {

        $scope.size = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Size.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });