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