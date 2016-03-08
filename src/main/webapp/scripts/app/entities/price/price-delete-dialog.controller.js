'use strict';

angular.module('jewelryApp')
	.controller('PriceDeleteController', function($scope, $modalInstance, entity, Price) {

        $scope.price = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Price.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });