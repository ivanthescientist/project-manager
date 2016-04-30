(function () {
    "use strict";
    angular.module('projectManagerSPA').controller('organizationEditController', ['authenticationService', '$scope', '$state', 'organizationService', function (authenticationService, $scope, $state, organizationService) {
        organizationService.getById($state.params.organizationId, function (response) {
            $scope.name = response.data.name;
            $scope.description = response.data.description;
        });
        
        $scope.save = function () {
            organizationService.update($state.params.organizationId, $scope.name, $scope.description, function (response) {
                $state.go('organizationList');
            });
        }
    }]);
})();