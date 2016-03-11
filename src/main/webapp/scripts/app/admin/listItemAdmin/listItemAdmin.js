'use strict';

angular.module('jewelryApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('listItemAdmin', {
                parent: 'entity',
                url: '/listItemAdmin',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jewelryApp.listItem.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/admin/listItemAdmin/listItemAdmin.html',
                        controller: 'ListItemAdminController'
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
            .state('listItemAdmin.detail', {
                parent: 'entity',
                url: '/listItemAdmin/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'jewelryApp.image.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/admin/listItemAdmin/listItemAdmin-detail.html',
                        controller: 'ListItemAdminDetailController'
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
            //.state('listItem.new', {
            //    parent: 'entity',
            //    url: '/new',
            //    data: {
            //        authorities: ['ROLE_USER'],
            //    },
            //    onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
            //        $modal.open({
            //            templateUrl: 'scripts/app/entities/listItem/listItem-dialog.html',
            //            controller: 'ListItemDialogController',
            //            size: 'lg',
            //            resolve: {
            //                entity: function () {
            //                    return {
            //                        itemNumber: null,
            //                        vendorItemNumber: null,
            //                        description: null,
            //                        vip: null,
            //                        id: null
            //                    };
            //                }
            //            }
            //        }).result.then(function(result) {
            //            $state.go('listItem', null, { reload: true });
            //        }, function() {
            //            $state.go('listItem');
            //        })
            //    }]
            //})
            .state('listItemAdmin.editItemInformation', {
                parent: 'listItemAdmin.detail',
                url: '/editItemInformation',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/admin/listItemAdmin/listItemAdmin-itemInformation-dialog.html',
                        controller: 'ListItemAdminItemInformationDialogController',
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
            .state('listItemAdmin.editItem', {
                parent: 'listItemAdmin.detail',
                url: '/editItem/{idItem}',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/admin/listItemAdmin/listItemAdmin-item-dialog.html',
                        controller: 'ListItemAdminItemDialog',
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
            .state('listItemAdmin.newItem', {
                parent: 'listItemAdmin.detail',
                url: '/newItem',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/admin/listItemAdmin/listItemAdmin-item-dialog.html',
                        controller: 'ListItemAdminItemDialog',
                        size: 'lg',
                        resolve: {
                            entity: ['ListItem', function(ListItem) {
                                return {
                                    name: null,
                                    id: null,
                                    itemInformation: ListItem.get({id : $stateParams.id})
                                };
                                //return ListItem.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('^', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
            //.state('listItem.delete', {
            //    parent: 'listItem',
            //    url: '/{id}/delete',
            //    data: {
            //        authorities: ['ROLE_USER'],
            //    },
            //    onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
            //        $modal.open({
            //            templateUrl: 'scripts/app/entities/image/image-delete-dialog.html',
            //            controller: 'ImageDeleteController',
            //            size: 'md',
            //            resolve: {
            //                entity: ['Image', function(Image) {
            //                    return Image.get({id : $stateParams.id});
            //                }]
            //            }
            //        }).result.then(function(result) {
            //            $state.go('image', null, { reload: true });
            //        }, function() {
            //            $state.go('^');
            //        })
            //    }]
            //});
    });