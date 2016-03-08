(function(){

    this.CustomerDetailViewModel = function() {
        this.name = "CustomerDetailViewModel";
        this.init();
    };
    this.CustomerDetailViewModel.prototype = new unify.ViewModel();
    this.CustomerDetailViewModel.prototype.constructor = this.CustomerDetailViewModel;

    this.CustomerDetailViewModel.prototype.getCustomerInfo = function(params) {
        var condition = JSON.parse(params);
        var self = this;
        self.getCustomerInfoRequest(condition.custCode, self);
    };

    this.CustomerDetailViewModel.prototype.getCustomerInfoRequest = function(custCode, self) {
        print("custCode is " + custCode);
        util.HttpUtil.getCustomerInfo(custCode, function(response){
            print("callback is " + util.Constant.NOTIFY_NATIVE_GET_CUSTOMER_DETAIL_RESULT);
            self.notify(util.Constant.NOTIFY_NATIVE_GET_CUSTOMER_DETAIL_RESULT, response);
        });
    };

    this.CustomerDetailViewModel.prototype.claimCustomer = function(params) {
        var condition = JSON.parse(params);
        var self = this;
        util.HttpUtil.claimCustomer(condition.custCode, function(response){
            self.notify(util.Constant.NOTIFY_NATIVE_CLAIM_CUSTOMER_RESULT, response);
        });
    };

    this.CustomerDetailViewModel.prototype.addTrackInfo = function(params) {
        var condition = JSON.parse(params);
        var self = this;
        print("content is " + condition.content);
        util.HttpUtil.addCustomerTrack(condition.custCode, condition.content, function(response){
            if(response.isSuccess){
                self.getCustomerInfoRequest(condition.custCode, self);
            }
            self.notify(util.Constant.NOTIFY_NATIVE_CUST_TRACK_RESULT, response);
        });
    };

}.call(this));