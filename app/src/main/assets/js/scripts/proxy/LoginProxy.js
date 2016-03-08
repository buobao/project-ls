(function() {

    var LoginProxy = function(){
        this.name = "LoginProxy";
    };

    /**
    检查用户名是否合法
    */
    LoginProxy.prototype.checkEmail = function(email) {
        return util.AppUtil.checkEmail(mail);
    };


    /**
    检查密码是否合法
    */
    LoginProxy.prototype.checkPassword = function(password) {
        return util.AppUtil.checkPassword(password);
    };


    LoginProxy.prototype.getDeltaServerTime = function() {
        return util.Constant.DELTA_SERVER_TIME;
    };

    /**
    登录接口
    */
    LoginProxy.prototype.login = function(params) {
        unify.ViewModelManager.retrieveViewModel("LoginViewModel").login(params);
        // this.LoginViewModel.login(params);
    };

    this.LoginProxy = LoginProxy;
}.call(this));