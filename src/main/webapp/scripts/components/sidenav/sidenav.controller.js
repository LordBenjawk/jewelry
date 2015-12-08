'use strict';

angular.module('jewelryApp')
    .controller('SidenavController', function ($scope, $location, $state, Auth, Principal, ENV) {
        $scope.isAuthenticated = Principal.isAuthenticated;
        $scope.$state = $state;
        $scope.inProduction = ENV === 'prod';

        $scope.logout = function () {
            Auth.logout();
            $state.go('login');
        };

        $scope.toggleSideNav = function(menuId) {
            $mdSidenav(menuId).toggle();
        };
    });
