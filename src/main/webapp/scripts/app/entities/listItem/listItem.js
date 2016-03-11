'use strict';

angular.module('jewelryApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('listItem', {
                parent: 'entity',
                url: '/listItem',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jewelryApp.listItem.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/listItem/listItem.html',
                        controller: 'ListItemController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('listItem');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('listItem.detail', {
                parent: 'entity',
                url: '/listItem/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jewelryApp.image.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/listItem/listItem-detail.html',
                        controller: 'ListItemDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('listItem');
                        $translatePartialLoader.addPart('itemInformation');
                        $translatePartialLoader.addPart('item');
                        $translatePartialLoader.addPart('price');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'ListItem', function($stateParams, ListItem) {
                        return ListItem.get({id : $stateParams.id});
                    }]
                }
            })
            .state('listItem.new', {
                parent: 'entity',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/listItem/listItem-dialog.html',
                        controller: 'ListItemDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    itemNumber: null,
                                    vendorItemNumber: null,
                                    description: null,
                                    vip: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('listItem', null, { reload: true });
                    }, function() {
                        $state.go('listItem');
                    })
                }]
            })
            .state('listItem.editItemInformation', {
                parent: 'listItem.detail',
                url: '/{id}/editItemInformation',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/itemInformation/itemInformation-dialog.html',
                        controller: 'ItemInformationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ListItem', function(ListItem) {
                                return ListItem.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('listItem', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })

            .state('listItem.editItem', {
                parent: 'listItem.detail',
                url: '/{idItem}/editItem',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/listItem/listItem-edit-item.html',
                        controller: 'ListItemEditItemController',
                        size: 'lg',
                        resolve: {
                            entity: ['Item', function(Item) {
                                return Item.get({id : $stateParams.idItem});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('^', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('listItem.delete', {
                parent: 'listItem',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/image/image-delete-dialog.html',
                        controller: 'ImageDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Image', function(Image) {
                                return Image.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('image', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
