angular.module('main', [])
.controller('memberships', function($scope, $http) {
    $http.get('localhost:8080/api/courses').
        then(function(response) {
            console.log("response")
            $scope.membershipList = response.data;
        }).catch(function (err) {});;
});

