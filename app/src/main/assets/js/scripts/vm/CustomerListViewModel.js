(function(){

    this.CustomerListViewModel = function() {
        this.name = "CustomerListViewModel";
        this.init();

        this.myCustCount = 0;
        this.publicCustCount = 0;
    };
    this.CustomerListViewModel.prototype = new unify.ViewModel();
    this.CustomerListViewModel.prototype.constructor = this.CustomerListViewModel;

    this.CustomerListViewModel.prototype.getCustomerListRequest = function(condition){
        var self = this;
        // if(condition.type == "my" && self.myCustCount != 0 && condition.page != 0) {
        //     if(condition.page*condition.pageSize > self.myCustCount) {
        //         var response = util.HttpUtil.createResponseData();
        //         response.params.action = "append";
        //         response.content = {records:0, page:0, total:0, rows:[]};
        //         self.notify(util.Constant.NOTIFY_NATIVE_GET_CUSTOMER_LIST_RESULT, response);
        //         return;
        //     } 
        // } else if(condition.type == "public" && self.publicCustCount != 0 && condition.page != 0) {
        //     if(condition.page*condition.pageSize > self.publicCustCount) {
        //         var response = util.HttpUtil.createResponseData();
        //         response.params.action = "append";
        //         response.content = {records:0, page:0, total:0, rows:[]};
        //         self.notify(util.Constant.NOTIFY_NATIVE_GET_CUSTOMER_LIST_RESULT, response);
        //         return;
        //     } 
        // }

        util.HttpUtil.getCustomerList(condition.type, condition.page, condition.pageSize, function(response){
            if(response.isSuccess) {
                if(condition.page == 1) {
                    //说明是刚进入，获取最新的数据
                    response.params.action = "refresh";
                } else {
                    response.params.action = "append";
                }

                // if(condition.type == "my")
                //     self.myCustCount = response.content.records;
                // else
                //     self.publicCustCount = response.content.records;
            }
            //print(response);
            self.notify(util.Constant.NOTIFY_NATIVE_GET_CUSTOMER_LIST_RESULT, response);
        });
    };

    this.CustomerListViewModel.prototype.getCustomerList = function(params){
        var condition = JSON.parse(params);
        this.getCustomerListRequest(condition);
    };
    
}.call(this));