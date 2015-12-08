'use strict';

angular.module('jewelryApp')
    .controller('NavbarController', function ($scope, $location, $state, Auth, Principal, ENV, $mdSidenav) {
        $scope.isAuthenticated = Principal.isAuthenticated;
        $scope.$state = $state;
        $scope.inProduction = ENV === 'prod';

        $scope.logout = function () {
            Auth.logout();
            $state.go('home');
        };

        $scope.toggleSidenav = function(menuId) {
            console.log("toggle");
            $mdSidenav(menuId).toggle();
        };
    });
