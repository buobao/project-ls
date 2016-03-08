(function() {

    this.LoginViewModel = function() {
        this.name = "LoginViewModel";
        this.init();
        this.serverTimeDeltas = [];
    };
    this.LoginViewModel.prototype = new unify.ViewModel();
    this.LoginViewModel.prototype.constructor = this.LoginViewModel;

    this.LoginViewModel.prototype.login = function(params){
        var condition = JSON.parse(params);
        var self = this;

        util.HttpUtil.login(condition.username, condition.password, function(response){
            if(response.isSuccess){
                util.Models.currUser = new util.Models.User();
                util.Models.currUser.empId = response.empId;
                util.Models.currUser.token = response.token;

                //correcting system time
                self.correctingCount = 0;
                self.correctionSystemTime(self);
            }
            self.notify(util.Constant.NOTIFY_NATIVE_LOGIN_RESULT, response);
        });
    };

    this.LoginViewModel.prototype.correctionSystemTime = function(self){
        if(self.correctingCount == 5){
            var total = 0;
            for(var i=0;i<self.serverTimeDeltas.length;i++) {
                total += self.serverTimeDeltas[i];
            }
            util.Constant.DELTA_SERVER_TIME = total / self.serverTimeDeltas.length;
            print("correctionSystemTime - util.Constant.DELTA_SERVER_TIME is " 
                + util.Constant.DELTA_SERVER_TIME);
            return;
        }
        self.correctingCount += 1;
        print("correctionSystemTime - count is " + self.correctingCount);
        util.HttpUtil.getServerTime(function(response){
            print("correctionSystemTime - count result: " + response);
            if (response.isSuccess) {
                var serverTime = response.content.serverTime;
                print("server time is " + serverTime);
                var currTime = (new Date()).getTime();
                print("local time is " + currTime);
                var deltaTime = serverTime - currTime;
                self.serverTimeDeltas.push(deltaTime);
                self.correctionSystemTime(self);
            }
        });
    }

}.call(this));