'use strict';

angular.module('jewelryApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('purchaseOrderDetails', {
                parent: 'entity',
                url: '/purchaseOrderDetailss',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jewelryApp.purchaseOrderDetails.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/purchaseOrderDetails/purchaseOrderDetailss.html',
                        controller: 'PurchaseOrderDetailsController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('purchaseOrderDetails');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('purchaseOrderDetails.detail', {
                parent: 'entity',
                url: '/purchaseOrderDetails/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jewelryApp.purchaseOrderDetails.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/purchaseOrderDetails/purchaseOrderDetails-detail.html',
                        controller: 'PurchaseOrderDetailsDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('purchaseOrderDetails');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'PurchaseOrderDetails', function($stateParams, PurchaseOrderDetails) {
                        return PurchaseOrderDetails.get({id : $stateParams.id});
                    }]
                }
            })
            .state('purchaseOrderDetails.new', {
                parent: 'purchaseOrderDetails',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/purchaseOrderDetails/purchaseOrderDetails-dialog.html',
                        controller: 'PurchaseOrderDetailsDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('purchaseOrderDetails', null, { reload: true });
                    }, function() {
                        $state.go('purchaseOrderDetails');
                    })
                }]
            })
            .state('purchaseOrderDetails.edit', {
                parent: 'purchaseOrderDetails',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/purchaseOrderDetails/purchaseOrderDetails-dialog.html',
                        controller: 'PurchaseOrderDetailsDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['PurchaseOrderDetails', function(PurchaseOrderDetails) {
                                return PurchaseOrderDetails.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('purchaseOrderDetails', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('purchaseOrderDetails.delete', {
                parent: 'purchaseOrderDetails',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/purchaseOrderDetails/purchaseOrderDetails-delete-dialog.html',
                        controller: 'PurchaseOrderDetailsDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['PurchaseOrderDetails', function(PurchaseOrderDetails) {
                                return PurchaseOrderDetails.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('purchaseOrderDetails', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
