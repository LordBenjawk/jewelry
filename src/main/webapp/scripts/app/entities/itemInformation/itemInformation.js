'use strict';

angular.module('jewelryApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('itemInformation', {
                parent: 'entity',
                url: '/itemInformations',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jewelryApp.itemInformation.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/itemInformation/itemInformations.html',
                        controller: 'ItemInformationController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('itemInformation');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('itemInformation.detail', {
                parent: 'entity',
                url: '/itemInformation/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jewelryApp.itemInformation.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/itemInformation/itemInformation-detail.html',
                        controller: 'ItemInformationDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('itemInformation');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'ItemInformation', function($stateParams, ItemInformation) {
                        return ItemInformation.get({id : $stateParams.id});
                    }]
                }
            })
            .state('itemInformation.new', {
                parent: 'itemInformation',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/itemInformation/itemInformation-dialog.html',
                        controller: 'ItemInformationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    itemNumber: null,
                                    vendorItemNumber: null,
                                    description: null,
                                    active: null,
                                    vip: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('itemInformation', null, { reload: true });
                    }, function() {
                        $state.go('itemInformation');
                    })
                }]
            })
            .state('itemInformation.edit', {
                parent: 'itemInformation',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/itemInformation/itemInformation-dialog.html',
                        controller: 'ItemInformationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ItemInformation', function(ItemInformation) {
                                return ItemInformation.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('itemInformation', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('itemInformation.delete', {
                parent: 'itemInformation',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/itemInformation/itemInformation-delete-dialog.html',
                        controller: 'ItemInformationDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['ItemInformation', function(ItemInformation) {
                                return ItemInformation.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('itemInformation', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
