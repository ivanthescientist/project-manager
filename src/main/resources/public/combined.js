(function () {
    var app = angular.module("projectManagerSPA", [
        'ui.router',
        'ui.bootstrap',
        'toaster'
    ]);

    app.config(['$stateProvider', '$urlRouterProvider', '$httpProvider', function($stateProvider, $urlRouterProvider, $httpProvider) {
        $httpProvider.interceptors.push(function ($q) {
            return {
                'responseError': function (rejection) {
                    var defer = $q.defer();
                    if(rejection.status === 401) {
                        console.log("unauthenticated");
                        window.location = "#/login";
                    }
                    if(rejection.status === 400) {
                        console.log("bad request");
                    }
                    if(rejection.status === 403) {
                        console.log("unauthorized")
                    }
                    if(rejection.status === 500) {
                        console.log("internal server error")
                    }

                    console.log(rejection.data);

                    defer.reject(rejection);
                    return defer.promise;
                }
            };
        });

        $httpProvider.interceptors.push(function () {
            return {
                request: function (config) {

                    if(config.headers.username == null && config.headers.password == null) {
                        config.headers.username = localStorage.getItem('username');
                        config.headers.password = localStorage.getItem('password');
                    }
                    return config;
                }
            };
        });

        $urlRouterProvider.otherwise('projects');

        $stateProvider.state('organizationList', {
            url: '/organizations',
            templateUrl: '/partials/organization.list.html',
            controller: 'organizationListController'
        });

        $stateProvider.state('organizationCreate', {
            url: '/organizations/new',
            templateUrl: '/partials/organization.create.html',
            controller: 'organizationCreateController'
        });

        $stateProvider.state('organizationEdit', {
            url: '/organizations/:organizationId',
            templateUrl: '/partials/organization.edit.html',
            controller: 'organizationEditController'
        });

        $stateProvider.state('projectList', {
            url: '/projects',
            templateUrl: '/partials/project.list.html',
            controller: 'projectListController'
        });

        $stateProvider.state('projectCreate', {
            url: '/projects/new',
            templateUrl: '/partials/project.create.html',
            controller: 'projectCreateController'
        });

        $stateProvider.state('projectEdit', {
            url: '/projects/:projectId',
            templateUrl: '/partials/project.edit.html',
            controller: 'projectEditController'
        });

        $stateProvider.state('userList', {
            url: '/users',
            templateUrl: '/partials/user.list.html',
            controller: 'userListController'
        });

        $stateProvider.state('userLogin', {
            url: '/login',
            templateUrl: '/partials/user.login.html',
            controller: 'userLoginController'
        });

        $stateProvider.state('userLogout', {
            url: '/logout',
            controller: ['authenticationService', '$state', function (authenticationService, $state) {
                authenticationService.logOut();
                $state.go('userLogin');
            }]
        });
    }]);

    app.run(['$state', 'authenticationService', '$rootScope', function ($state, authenticationService, $rootScope) {
        if(!authenticationService.isAuthenticated()) {
            $state.go('userLogin', {});
        } else {
            authenticationService.getCurrentUser(function (response) {
                $rootScope.currentUser = response.data;
                console.log(response.data);
            }, function () {

            })
        }
    }])
})();


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
(function () {
    "use strict";
    angular.module('projectManagerSPA').controller('organizationListController', ['authenticationService', '$scope', '$state', 'organizationService', function (authenticationService, $scope, $state, organizationService) {
        organizationService.getList(function (response) {
            $scope.list = response.data;
        });
    }]);
})();
(function () {
    "use strict";
    angular.module('projectManagerSPA').controller('projectCreateController', ['authenticationService', '$scope', '$state', 'projectService', function (authenticationService, $scope, $state, projectService) {
        projectService.create($scope.name, $scope.description, function (response) {
            $state.go('projectList');
        });
    }]);
})();
(function () {
    "use strict";
    angular.module('projectManagerSPA').controller('projectEditController', ['authenticationService', '$scope', '$state', 'projectService', function (authenticationService, $scope, $state, projectService) {
        projectService.getById($state.params.projectId, function (response) {
            $scope.name = data.name;
            $scope.description = data.description;
        });
    }]);
})();
(function () {
    "use strict";
    angular.module('projectManagerSPA').controller('projectListController', ['authenticationService', '$scope', '$state', 'projectService', function (authenticationService, $scope, $state, projectService) {
        projectService.getList(function (response) {
            $scope.list = response.data;
        });
    }]);
})();
(function () {
    "use strict";
    angular.module('projectManagerSPA').controller('userListController', ['authenticationService', '$scope', '$state', 'userService', function (authenticationService, $scope, $state, userService) {
        userService.getList(function (response) {
            $scope.list = response.data;
        });
    }]);
})();
(function () {
    "use strict";
    angular.module('projectManagerSPA').controller('userLoginController', ['authenticationService', '$scope', '$state',function (authenticationService, $scope, $state) {
        $scope.logIn = function () {
            authenticationService.logIn($scope.username, $scope.password, function () {
                $state.go('organizationList');
            });
        };

        $scope.logOut = function () {
            $state.go('userLogout');
        };
    }]);
})();
(function () {
    "use strict";
    angular.module('projectManagerSPA').controller('userRegisterController', ['authenticationService', '$scope', '$state', 'userService', function (authenticationService, $scope, $state, userService) {

    }]);
})();

(function () {
   angular.module('projectManagerSPA').factory('authenticationService', ['$http', '$rootScope', function ($http, $rootScope) {
       return {
           getCookie: function () {
               return {
                   'username': localStorage.getItem('username'),
                   'password': localStorage.getItem('password')
               };
           },
           isAuthenticated: function () {
               return localStorage.getItem('username') != null && localStorage.getItem('password') != null;
           },
           getCurrentUser: function (successCallback, failureCallback) {
               var self = this;
               if($rootScope.currentUser != null) {
                   return $rootScope.currentUser;
               }

               console.log(self);

               return $http({
                   'method': 'GET',
                   'url': '/api/users/me',
                   'headers': {
                       'username': self.getCookie().username,
                       'password': self.getCookie().password
                   }
               }).then(function (response) {
                   $rootScope.currentUser = response.data;

                   $rootScope.currentUser.isSolutionAdmin = function () {
                       return $rootScope.currentUser.roles.indexOf("ROLE_SOLUTION_ADMIN") !== -1;
                   };

                   $rootScope.currentUser.isOrganizationAdmin = function () {
                       return $rootScope.currentUser.roles.indexOf("ROLE_ORGANIZATION_ADMIN") !== -1;
                   };

                   successCallback(response);
               }, failureCallback);
           },
           logIn: function (username, password, successCallback) {
               console.log("Attempting login");
               console.log(username + " : " + password);
               localStorage.setItem('username', username);
               localStorage.setItem('password', password);

               this.getCurrentUser(successCallback, function () {
                   localStorage.clear();
               })
           },
           logOut: function () {
               $rootScope.currentUser = null;
               localStorage.clear();
           }
       };
   }])
})();
(function () {
    angular.module('projectManagerSPA').factory('organizationService', ['$http', function ($http) {
        return {
            getList: function (successCallback) {
                return $http({
                    'method': 'GET',
                    'url': '/api/organizations'
                }).then(successCallback);
            },
            getById: function (id, successCallback) {
                return $http({
                    'method': 'GET',
                    'url': '/api/organizations/' + id
                }).then(successCallback);
            },
            create: function (name, description, successCallback) {
                return $http({
                    'method': 'POST',
                    'url': '/api/organizations',
                    'data': {
                        'name': name,
                        'description': description
                    }
                }).then(successCallback, function (error) {
                    console.log(error);
                });
            },
            update: function (id, name, description, successCallback) {
                return $http({
                    'method': 'PUT',
                    'url': '/api/organizations/' + id,
                    'data': {
                        'name': name,
                        'description': description
                    }
                }).then(successCallback);
            },
            addMember: function (organizationId, userId, successCallback) {
                return $http({
                    'method': 'POST',
                    'url': '/api/organizations/' + organizationId + '/members',
                    'data': {
                        'userId': userId
                    }
                }).then(successCallback);
            },
            removeMember: function (organizationId, userId, successCallback) {
                return $http({
                    'method': 'DELETE',
                    'url': '/api/organizations/' + organizationId + '/members/' + userId
                }).then(successCallback);
            }
        };
    }]);
})();
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
(function () {
    'use strict';
    angular.module('projectManagerSPA').factory('userService', ['$http', function ($http) {
        return {
            getList: function () {
                $http({});
            },
            registerOrganizationMember: function (username, password, organizationId) {
                return $http({
                    'method': 'POST',
                    'url': '/api/users',
                    'data': {
                        'username': username,
                        'password': password,
                        'organizationId': organizationId
                    }
                }).then(successCallback);
            },
            registerOrganizationOwner: function (username, password) {
                return $http({
                    'method': 'POST',
                    'url': '/api/users/organization-owners',
                    'data': {
                        'username': username,
                        'password': password
                    }
                }).then(successCallback);
            }
        };
    }]);
})();