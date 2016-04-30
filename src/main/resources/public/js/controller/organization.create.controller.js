(function () {
    "use strict";
    angular.module('projectManagerSPA').controller('organizationCreateController', ['authenticationService', '$scope', '$state', 'organizationService', function (authenticationService, $scope, $state, organizationService) {
        $scope.save = function () {
            organizationService.create($scope.name, $scope.description, function (response) {
                $state.go('organizationList');
            });
        }
    }]);
})();