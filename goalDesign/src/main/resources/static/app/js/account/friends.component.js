angular.module('goalApp')
	.component('friends', {
		templateUrl: '/app/template/account/friends.html',
		controller: function($scope, $location, AccountApi, NotificationService) {

			$scope.goToAccount = function(profile) {
				$location.path('/list/' + profile.id);
			};

			$scope.searchAccount = function() {
				$scope.search = true;
				if ($scope.userInfo.username.length == 3) {
					AccountApi.searchAccount({ id: $scope.userInfo.username }, function(resp) {
						$scope.accounts = resp;
					});

				}
				if ($scope.userInfo.username.length == 0) {
					$scope.search = false;
				}
			};

			$scope.sendRequest = function(account) {
				let send = true;
				let request = {
					receiver: {},
					sender: {}
				};
				if (account.username == user.username) {
					toastr.error('This is you ');
					send = false;
				} else {
					$scope.requests.forEach(function(f) {
						if (f.receiver.username == account.username) {
							toastr.error('Already sent');
							send = false;
						}
					});
					$scope.friends.forEach(function(f) {
						if (f.username == account.username) {
							toastr.error('This User is already your friend');
							send = false;
						}
					});
				}

				if (send) {
					request.receiver = account;
					request.sender = user;
					NotificationService.sendRequest(request);
				}
				$scope.search = false;
				$scope.userInfo.username = '';
			};

			$scope.deleteRequest = function(request) {
				AccountApi.cancelRequest(request, function(resp) {
					if (resp) {
						_.remove($scope.requests, { id: request.id });
						toastr.success('Successfully Deleted');
					}
				});
			};

			$scope.acceptRequest = function(req) {
				AccountApi.acceptRequest(req, function(resp) {
					if (resp) {
						_.remove($scope.requests, { id: req.id });
						$scope.friends.push(req.sender);
						toastr.success('Successfully Accepted');
					}
				});
			};


			$scope.deleteFriend = function(account) {
				AccountApi.deleteFriend(account, function() {
					_.remove($scope.friends, { id: account.id });
					toastr.success('Successfully Deleted');
				});
			};

			$scope.incomingRequest = function() {
				if (NotificationService.listenRequest()) {
					$scope.requests.push(NotificationService.getRequest());
					NotificationService.setRequest(null);
				}
			};

			$scope.init = function() {
				$scope.requests = AccountApi.listRequests();
				$scope.friends = AccountApi.listFriends();
				NotificationService.subscribe($scope.incomingRequest);
				$scope.search = false;
				$scope.userInfo = {};
			};
			$scope.init();
		}
	}).filter('requestFilter', function(AccountService) {

		return function(item, req) {
			let usr = AccountService.getUser();
			let result = [];
			if (item) {
				item.forEach(a => {
					if (req == true) {
						if (a.sender.username == usr.username) {
							result.push(a);
						}
					} else {
						if (a.receiver.username == usr.username) {
							result.push(a);
						}
					}
				});
			}
			return result;
		}
	});