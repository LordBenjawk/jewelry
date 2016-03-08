'use strict';

angular.module('jewelryApp')
	.controller('ColorDeleteController', function($scope, $modalInstance, entity, Color) {

        $scope.color = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Color.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });