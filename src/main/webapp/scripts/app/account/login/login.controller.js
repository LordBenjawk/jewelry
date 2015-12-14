'use strict';

angular.module('jewelryApp')
    .controller('LoginController', function ($rootScope, $scope, $state, $timeout, Auth, toastr) {
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

        //$timeout(function (){angular.element('[ng-model="username"]').focus();});

        $scope.login = function () {
            // event.preventDefault();
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
                toastr.error('Your credentials are wrong', 'Error');
            });
        };

    });
