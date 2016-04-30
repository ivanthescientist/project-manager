(function () {
    "use strict";
    angular.module('projectManagerSPA').controller('projectEditController', ['authenticationService', '$scope', '$state', 'projectService', function (authenticationService, $scope, $state, projectService) {
        projectService.getById($state.params.projectId, function (response) {
            $scope.name = data.name;
            $scope.description = data.description;
        });
    }]);
})();