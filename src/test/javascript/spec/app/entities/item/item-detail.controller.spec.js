'use strict';

describe('Item Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockItem, MockItemInformation, MockCategory, MockPrice, MockSizes, MockColor, MockImage, MockPurchaseOrderDetails;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockItem = jasmine.createSpy('MockItem');
        MockItemInformation = jasmine.createSpy('MockItemInformation');
        MockCategory = jasmine.createSpy('MockCategory');
        MockPrice = jasmine.createSpy('MockPrice');
        MockSizes = jasmine.createSpy('MockSizes');
        MockColor = jasmine.createSpy('MockColor');
        MockImage = jasmine.createSpy('MockImage');
        MockPurchaseOrderDetails = jasmine.createSpy('MockPurchaseOrderDetails');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Item': MockItem,
            'ItemInformation': MockItemInformation,
            'Category': MockCategory,
            'Price': MockPrice,
            'Sizes': MockSizes,
            'Color': MockColor,
            'Image': MockImage,
            'PurchaseOrderDetails': MockPurchaseOrderDetails
        };
        createController = function() {
            $injector.get('$controller')("ItemDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'jewelryApp:itemUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
