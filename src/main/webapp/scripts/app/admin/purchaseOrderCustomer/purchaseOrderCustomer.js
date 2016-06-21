'use strict';

angular.module('jewelryApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('purchaseOrdersCustomer', {
                parent: 'entity',
                url: '/purchaseOrdersCustomer',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jewelryApp.purchaseOrder.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/purchaseOrder/purchaseOrders.html',
                        controller: 'PurchaseOrderCustomerController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('purchaseOrder');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            });
            //.state('purchaseOrder.detail', {
            //    parent: 'entity',
            //    url: '/purchaseOrder/{id}',
            //    data: {
            //        authorities: ['ROLE_USER'],
            //        pageTitle: 'jewelryApp.purchaseOrder.detail.title'
            //    },
            //    views: {
            //        'content@': {
            //            templateUrl: 'scripts/app/entities/purchaseOrder/purchaseOrder-detail.html',
            //            controller: 'PurchaseOrderDetailController'
            //        }
            //    },
            //    resolve: {
            //        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
            //            $translatePartialLoader.addPart('purchaseOrder');
            //            return $translate.refresh();
            //        }],
            //        entity: ['$stateParams', 'PurchaseOrder', function($stateParams, PurchaseOrder) {
            //            return PurchaseOrder.get({id : $stateParams.id});
            //        }]
            //    }
            //})
            //.state('purchaseOrder.new', {
            //    parent: 'purchaseOrder',
            //    url: '/new',
            //    data: {
            //        authorities: ['ROLE_USER'],
            //    },
            //    onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
            //        $modal.open({
            //            templateUrl: 'scripts/app/entities/purchaseOrder/purchaseOrder-dialog.html',
            //            controller: 'PurchaseOrderDialogController',
            //            size: 'lg',
            //            resolve: {
            //                entity: function () {
            //                    return {
            //                        purchaseOrderNumber: null,
            //                        id: null
            //                    };
            //                }
            //            }
            //        }).result.then(function(result) {
            //            $state.go('purchaseOrder', null, { reload: true });
            //        }, function() {
            //            $state.go('purchaseOrder');
            //        })
            //    }]
            //})
            //.state('purchaseOrder.edit', {
            //    parent: 'purchaseOrder',
            //    url: '/{id}/edit',
            //    data: {
            //        authorities: ['ROLE_USER'],
            //    },
            //    onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
            //        $modal.open({
            //            templateUrl: 'scripts/app/entities/purchaseOrder/purchaseOrder-dialog.html',
            //            controller: 'PurchaseOrderDialogController',
            //            size: 'lg',
            //            resolve: {
            //                entity: ['PurchaseOrder', function(PurchaseOrder) {
            //                    return PurchaseOrder.get({id : $stateParams.id});
            //                }]
            //            }
            //        }).result.then(function(result) {
            //            $state.go('purchaseOrder', null, { reload: true });
            //        }, function() {
            //            $state.go('^');
            //        })
            //    }]
            //})
            //.state('purchaseOrder.delete', {
            //    parent: 'purchaseOrder',
            //    url: '/{id}/delete',
            //    data: {
            //        authorities: ['ROLE_USER'],
            //    },
            //    onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
            //        $modal.open({
            //            templateUrl: 'scripts/app/entities/purchaseOrder/purchaseOrder-delete-dialog.html',
            //            controller: 'PurchaseOrderDeleteController',
            //            size: 'md',
            //            resolve: {
            //                entity: ['PurchaseOrder', function(PurchaseOrder) {
            //                    return PurchaseOrder.get({id : $stateParams.id});
            //                }]
            //            }
            //        }).result.then(function(result) {
            //            $state.go('purchaseOrder', null, { reload: true });
            //        }, function() {
            //            $state.go('^');
            //        })
            //    }]
            //});
    });
