'use strict';

describe('PurchaseOrderDetails Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockPurchaseOrderDetails, MockItem, MockPurchaseOrder;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockPurchaseOrderDetails = jasmine.createSpy('MockPurchaseOrderDetails');
        MockItem = jasmine.createSpy('MockItem');
        MockPurchaseOrder = jasmine.createSpy('MockPurchaseOrder');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'PurchaseOrderDetails': MockPurchaseOrderDetails,
            'Item': MockItem,
            'PurchaseOrder': MockPurchaseOrder
        };
        createController = function() {
            $injector.get('$controller')("PurchaseOrderDetailsDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'jewelryApp:purchaseOrderDetailsUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
