'use strict';

angular.module('jewelryApp')
    .controller('SidenavController', function ($scope, $location, $state, Auth, Principal, ENV, $mdSidenav) {
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

        $scope.$watch(
            function(scope) { return scope.isAuthenticated()},
            function(newValue, oldValue) {
                (newValue) ? $('.js-sidenav').removeClass("hidden") : $('.js-sidenav').addClass("hidden");
            }
        )
    });
