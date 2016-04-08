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

        $urlRouterProvider.otherwise('organizationList');

        $stateProvider.state('organizationList', {
            url: '/organizations',
            templateUrl: '/partials/organization.list.html',
            controller: 'organizationListController'
        });

        $stateProvider.state('projectList', {
            url: '/projects',
            templateUrl: '/partials/project.list.html',
            controller: 'projectListController'
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

})();
(function () {
    var app = angular.module("projectManagerSPA");

    app.controller('organizationListController',['$scope', function ($scope) {
        
    }]);
})();
(function () {

})();
(function () {

})();
(function () {

})();
(function () {

})();
(function () {
   angular.module('projectManagerSPA').controller('userLoginController', ['authenticationService', '$scope', '$state',function (authenticationService, $scope, $state) {
      $scope.logIn = function () {
          authenticationService.logIn($scope.username, $scope.password, function () {
              $state.go('organizationList');
          });
      }
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
               if($rootScope.currentUser != null) {
                   return $rootScope.currentUser;
               }

               return $http({
                   'method': 'GET',
                   'url': '/api/users/me',
                   'headers': {
                       'username': this.getCookie().username,
                       'password': this.getCookie().password
                   }
               }).then(function (response) {
                   $rootScope.currentUser = response.data;
                   successCallback(response);
               }, failureCallback);
           },
           logIn: function (username, password, successCallback) {
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
    angular.module('projectManagerSPA').factory('organizationService',['$http', function ($http) {
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
            create: function (command, successCallback) {
                return $http({

                }).then(successCallback);
            }
        };
    }]);
})();
(function () {

})();
(function () {

})();