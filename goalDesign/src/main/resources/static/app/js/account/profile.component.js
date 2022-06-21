angular.module('goalApp')
	.component('profile', {
		templateUrl: '/app/template/account/profile.html',
		controller: function($scope, $location, Upload, AccountService, AccountApi) {

			$scope.removeAccount = function() {
				AccountApi.remove($scope.account, function() {
					AccountService.setUser(null);
					$location.path('/login');
				});
			};

			$scope.cancel = function() {
				$location.path('/list/me');
			}

			$scope.delete = function() {
				$scope.default = true;
				newImage.src = '/app/img/account.png';
			};

			$scope.onload = function(file) {
				$scope.default = false;
				console.log(file);
				if (file != null) {
					if (file.size > 190000) {
						toastr.warning('Image size is too big');
					} else {
						newImage.src = URL.createObjectURL(file);
						$scope.image = file;
					}
				}
			};

			$scope.save = function() {
				if ($scope.default != undefined) {
					Upload.upload({
						url: '/accounts/profile',
						method: 'POST',
						data: { file: $scope.image },
						params: { 'defaultImage': $scope.default }
					}).then(function onSuccess(response) {
						AccountService.updateProfile(response.data);
						$location.path('/list/me');
					}).catch(function onError(response) {
						console.log(response);
					});
				} else {
					$location.path('/list/me');
				}

			};

			$scope.init = function() {
				$scope.account = AccountService.getUser();

			};
			$scope.init();
		}
	});
