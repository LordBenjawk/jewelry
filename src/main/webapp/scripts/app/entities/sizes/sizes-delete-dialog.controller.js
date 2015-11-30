'use strict';

angular.module('jewelryApp')
	.controller('SizesDeleteController', function($scope, $modalInstance, entity, Sizes) {

        $scope.sizes = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Sizes.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });