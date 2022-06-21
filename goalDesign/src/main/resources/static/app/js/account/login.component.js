angular.module('goalApp')
	.component('login', {
		templateUrl: '/app/template/account/login.html',
		controller: function($scope, $location, AccountApi, AccountService, NotificationService) {

			$scope.login = function(form) {
				form.submitted = true;
				AccountApi.login($scope.loginRequest, function(resp) {
					if (resp.code == 10) {
						AccountService.setUser(resp.profile);
						NotificationService.setUser(resp);	
						$location.path('/list/me');
					} else {
						$scope.error = true;
					}
				});
			};

			$scope.init = function() {
				$scope.loginRequest = {};
			}
			$scope.init();
		}
	});