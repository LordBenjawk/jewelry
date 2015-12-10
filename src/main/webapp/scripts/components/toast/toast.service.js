'use strict';

angular.module('jewelryApp')
    .provider('ToastService', function () {

        this.position = {
            bottom: false,
            top: true,
            left: false,
            right: true
        };

        this.$get = ['$timeout', '$sce', '$translate', '$mdToast', function($timeout, $sce, $translate, $mdToast) {

            var exports = {
                clear   : clear,
                get     : get
            },
            toastId = 0,
            toasts = [],
            timeout = 5000;

            function clear() {
                toasts = [];
            }

            function get() {
                return toasts;
            }

            function factory(toastOptions) {
                var toast = {
                    type    :toastOptions.type,
                    msg     :$sce.trustAsHtml(toastOptions.msg),
                    id      :toastOptions.toastId,
                    timeout :toastOptions.timeout,
                    position:'top right'
                }
            }


            return exports;
        }];
    });
