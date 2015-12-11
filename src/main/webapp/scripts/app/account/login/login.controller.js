'use strict';

angular.module('jewelryApp')
    .controller('LoginController', function ($rootScope, $scope, $state, $timeout, Auth) {
        $scope.user = {};
        $scope.errors = {};
        $scope.rememberMe = true;

        var position = {
            bottom: false,
            top: true,
            left: false,
            right: true
        };
        $scope.toastPosition = angular.extend({},position);

        $scope.showSimpleToast = function() {
            $mdToast.show(
                $mdToast.simple()
                    .textContent('Simple Toast!')
                    .position($scope.getToastPosition())
                    .hideDelay(3000)
            );
        };

        $timeout(function (){angular.element('[ng-model="username"]').focus();});

        $scope.login = function (event) {
            event.preventDefault();
            Auth.login({
                username: $scope.username,
                password: $scope.password,
                rememberMe: $scope.rememberMe
            }).then(function () {
                $scope.authenticationError = false;
                if ($rootScope.previousStateName === 'register') {
                    $state.go('home');
                } else {
                    $rootScope.back();
                }
            }).catch(function () {
                $scope.authenticationError = true;
            });
        };

        $scope.getToastPosition = function() {
            sanitizePosition();

            return Object.keys($scope.toastPosition)
                .filter(function(pos) { return $scope.toastPosition[pos]; })
                .join(' ');
        };

        function sanitizePosition() {
            var current = $scope.toastPosition;

            if ( current.bottom && last.top ) current.top = false;
            if ( current.top && last.bottom ) current.bottom = false;
            if ( current.right && last.left ) current.left = false;
            if ( current.left && last.right ) current.right = false;

            last = angular.extend({},current);
        }
    });
