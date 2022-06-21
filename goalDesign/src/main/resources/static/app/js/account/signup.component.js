angular.module('goalApp')
	.component('signup', {
		templateUrl: '/app/template/account/signup.html',
		controller: function($scope, $location, AccountApi) {
			
			
			$scope.signup = function (form) {
				form.submitted = true;
				AccountApi.signup($scope.loginRequest, function(resp) {
					if(resp.code == 10) {
						$location.path('/login');
					} else {
						$scope.error = true;
					}
				});
			};
			
			$scope.init = function() {
				$scope.loginRequest = {};
			};
			$scope.init();
		}
	});