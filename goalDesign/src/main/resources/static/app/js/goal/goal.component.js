angular.module('goalApp')
	.component('goal', {
		templateUrl: '/app/template/goal/goal.html',
		controller: function($scope, $location, $routeParams, GoalApi, PlanApi, CommentApi) {
			$scope.id = $routeParams.id;

			$scope.goToAccount = function(profile) {
				$location.path('/list/' + profile.id);
			};

			$scope.share = function() {
				GoalApi.share({ id: $scope.id }, function(resp) {
					if (resp.username) {
						toastr.success("successfully copied");
						if($scope.goal.participants == null) {
							$scope.goal.participants = [];
						}
						$scope.goal.participants.push(resp);
						$scope.shareButton = false;
					}
				});
			};

			$scope.plansClick = function() {
				$('#commentList').collapse('hide');
			};

			$scope.commentsClick = function() {
				$scope.comments = CommentApi.list({ id: $scope.id });
				$('#planList').collapse('hide');
			};

			$scope.sendComment = function(comment, goal) {
				CommentApi.save({ id: goal.id }, comment, function(resp) {
					if (resp.code == 10) {
						toastr.success('Comment sent successfully');
						$scope.comments.push(resp.comment);
					} else {
						toastr.error('There is an error!');
					}
				});
			};

			$scope.delete = function(plan) {
				PlanApi.delete({ id: plan.id });
				_.remove($scope.plans, { id: plan.id });
				$scope.getProgress($scope.plans);
				toastr.success('successfully deleted');
			};

			$scope.finish = function(plan) {
				plan.status = 'FINISHED';
				$scope.update(plan);
			};

			$scope.revert = function(plan) {
				plan.status = 'CREATED';
				$scope.update(plan);
			};

			$scope.update = function(plan) {
				PlanApi.update({ id: $scope.id }, plan, function(resp) {
					if (resp) {
						$scope.plans = _.pull($scope.plans, { id: plan.id });
						$scope.getProgress($scope.plans);
						toastr.success('Successfully updated');
					} else {
						toastr.error('Error');
					}
				});
			};

			$scope.addPlan = function(newPlan) {
				if (newPlan.title == null || newPlan.title == '') {
					toastr.warning('Empty title!');
				} else if (newPlan.startPoint == null || newPlan.startPoint == '') {
					toastr.warning('Empty start point!');
				} else if (newPlan.targetPoint == null || newPlan.targetPoint == '') {
					toastr.warning('Empty target point!');
				} else if (newPlan.end == null || newPlan.end == '') {
					toastr.warning('Empty finish date!');
				} else if (newPlan.end < newPlan.start) {
					toastr.warning('End date can not be earlier than start date!')
				} else {
					if (newPlan.unit === 'Unit') {
						newPlan.unit = 'Amount';
					}
					PlanApi.save({ id: $scope.id }, newPlan, function(resp) {
						$('#planList').collapse('show');
						$('#commentList').collapse('hide');
						if (resp.code == 10) {
							$scope.plans.unshift(resp.plan);
							$scope.getProgress($scope.plans);
							toastr.success('New plan created');
						} else if (resp.code == 1) {
							toastr.warning('There is a plan with the same name!');
						} else {
							toastr.error('There is an error!');
						}
					});
					$scope.newPlan = {
					unit: 'Unit'
				};
				}
			};

			$scope.getProgress = function(list) {
				let length = list.length;
				let finished = 0;
				if (length === 0) {
					$scope.progress = 0;
					$scope.goal.status = 'CREATED';
				} else {
					list.forEach(function(p) {
						if (p.status == 'FINISHED') {
							finished += 1;
						}
					});
					$scope.progress = finished / length;
					$scope.progress *= 100;
					$scope.progress = Math.floor($scope.progress);
					if ($scope.progress == 100) {
						$scope.goal.status = 'FINISHED';
					} else if ($scope.progress == 0) {
						$scope.goal.status = 'CREATED';
					}
					else {
						$scope.goal.status = 'STARTED';
					}
				}
			};

			$scope.getPlans = function() {
				PlanApi.list({ id: $scope.id }, function(resp) {
					if (resp.length > 0) {
						$('#planList').collapse('show');
					}
					$scope.plans = resp;
					$scope.getProgress(resp);
				});
			};

			$scope.init = function() {
				$scope.goal = GoalApi.get({ id: $scope.id }, function(resp) {
					let participants = resp.participants;
					if (resp.owner) {
						if (user.username === resp.owner.username) {
							$scope.shareButton = false;
							$scope.visible = true;
						}
						if (resp.participant) {
							if (user.username === resp.participant.username) {
								$scope.shareButton = false;
								$scope.visible = true;
							}
						}
						if (participants) {
							for (var i = 0; i < participants.length; i++) {
								if (participants[i].username === user.username) {
									$scope.shareButton = false;
								}
							}

						}
					} else {
						$location.path('/list/me');
					}
				});
				$scope.newPlan = {
					unit: 'Unit'
				};
				$scope.getPlans();
				$scope.newComment = {};
				let date = new Date();
				$scope.today = date.toISOString().slice(0, 16);
				$scope.shareButton = true;
				$scope.visible = false;
			}
			$scope.init();
		}
	});










