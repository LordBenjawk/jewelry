'use strict';

describe('Item Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockItem, MockPrice, MockItemInformation, MockSize, MockColor, MockImage, MockPurchaseOrderDetails, MockStatus;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockItem = jasmine.createSpy('MockItem');
        MockPrice = jasmine.createSpy('MockPrice');
        MockItemInformation = jasmine.createSpy('MockItemInformation');
        MockSize = jasmine.createSpy('MockSize');
        MockColor = jasmine.createSpy('MockColor');
        MockImage = jasmine.createSpy('MockImage');
        MockPurchaseOrderDetails = jasmine.createSpy('MockPurchaseOrderDetails');
        MockStatus = jasmine.createSpy('MockStatus');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Item': MockItem,
            'Price': MockPrice,
            'ItemInformation': MockItemInformation,
            'Size': MockSize,
            'Color': MockColor,
            'Image': MockImage,
            'PurchaseOrderDetails': MockPurchaseOrderDetails,
            'Status': MockStatus
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
