(function () {
    'use strict';
    angular.module('projectManagerSPA').factory('projectService', ['$http', function ($http) {
        return {
            getList: function (successCallback) {
                return $http({
                    'method': 'GET',
                    'url': '/api/projects'
                }).then(successCallback);
            },
            getById: function (projectId, successCallback) {
                return $http({
                    'method': 'GET',
                    'url': '/api/projects/' + projectId
                }).then(successCallback);
            },
            create: function (name, description, successCallback) {
                return $http({
                    'method': 'POST',
                    'url': '/api/projects',
                    'data': {
                        'name': name,
                        'description': description
                    }
                }).then(successCallback);
            },
            update: function (projectId, name, description, successCallback) {
                return $http({
                    'method': 'PUT',
                    'url': '/api/projects/' + projectId,
                    'data': {
                        'name': name,
                        'description': description
                    }
                }).then(successCallback);
            },
            addParticipant: function (projectId, userId, successCallback) {
                return $http({
                    'method': 'POST',
                    'url': '/api/projects/' + projectId + '/participants',
                    'data': {
                        'userId': userId
                    }
                }).then(successCallback);
            },
            removeParticipant: function (projectId, userId, successCallback) {
                return $http({
                    'method': 'DELETE',
                    'url': '/api/projects/' + projectId + '/participants/' + userId
                }).then(successCallback);
            }
        };
    }]);
})();