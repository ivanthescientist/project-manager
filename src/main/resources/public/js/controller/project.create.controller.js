(function () {
    "use strict";
    angular.module('projectManagerSPA').controller('projectCreateController', ['authenticationService', '$scope', '$state', 'projectService', function (authenticationService, $scope, $state, projectService) {
        projectService.create($scope.name, $scope.description, function (response) {
            $state.go('projectList');
        });
    }]);
})();