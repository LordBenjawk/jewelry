'use strict';

angular.module('jewelryApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('price', {
                parent: 'entity',
                url: '/prices',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jewelryApp.price.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/price/prices.html',
                        controller: 'PriceController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('price');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('price.detail', {
                parent: 'entity',
                url: '/price/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jewelryApp.price.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/price/price-detail.html',
                        controller: 'PriceDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('price');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Price', function($stateParams, Price) {
                        return Price.get({id : $stateParams.id});
                    }]
                }
            })
            .state('price.new', {
                parent: 'price',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/price/price-dialog.html',
                        controller: 'PriceDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    tOne: null,
                                    tTwo: null,
                                    tThree: null,
                                    vendorPrice: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('price', null, { reload: true });
                    }, function() {
                        $state.go('price');
                    })
                }]
            })
            .state('price.edit', {
                parent: 'price',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/price/price-dialog.html',
                        controller: 'PriceDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Price', function(Price) {
                                return Price.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('price', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('price.delete', {
                parent: 'price',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/price/price-delete-dialog.html',
                        controller: 'PriceDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Price', function(Price) {
                                return Price.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('price', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
