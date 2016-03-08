'use strict';

describe('PurchaseOrder Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockPurchaseOrder, MockPurchaseOrderDetails, MockUser, MockStatus;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockPurchaseOrder = jasmine.createSpy('MockPurchaseOrder');
        MockPurchaseOrderDetails = jasmine.createSpy('MockPurchaseOrderDetails');
        MockUser = jasmine.createSpy('MockUser');
        MockStatus = jasmine.createSpy('MockStatus');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'PurchaseOrder': MockPurchaseOrder,
            'PurchaseOrderDetails': MockPurchaseOrderDetails,
            'User': MockUser,
            'Status': MockStatus
        };
        createController = function() {
            $injector.get('$controller')("PurchaseOrderDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'jewelryApp:purchaseOrderUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
