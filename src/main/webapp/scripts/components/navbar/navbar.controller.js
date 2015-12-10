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
            $mdSidenav(menuId).toggle();
        };

        $scope.$watch(
            function(scope) { return scope.isAuthenticated()},
            function(newValue, oldValue) {
                (newValue) ? $('.js-sidenav').addClass("hidden") : $mdSidenav('left').close();
            }
        )
    });
