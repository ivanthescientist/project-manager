(function () {
    "use strict";
    angular.module('projectManagerSPA').controller('organizationListController', ['authenticationService', '$scope', '$state', 'organizationService', function (authenticationService, $scope, $state, organizationService) {
        organizationService.getList(function (response) {
            $scope.list = response.data;
        });
    }]);
})();