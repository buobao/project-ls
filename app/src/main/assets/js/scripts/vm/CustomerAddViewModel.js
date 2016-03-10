(function(){

    this.CustomerAddViewModel = function() {
        this.name = "CustomerAddViewModel";
        this.init();
    };
    this.CustomerAddViewModel.prototype = new unify.ViewModel();
    this.CustomerAddViewModel.prototype.constructor = this.CustomerAddViewModel;

    this.CustomerAddViewModel.prototype.addCustomer = function(params) {
        var condition = JSON.parse(params);
        var self = this;
        util.HttpUtil.addCustomer(condition, function(response){
            self.notify(util.Constant.NOTIFY_NATIVE_ADD_CUSTOMER_RESULT, response);

            //添加成功之后，需要刷新数据
            if (response.isSuccess) {
                var condition = {
                    type : "my",
                    page : 1,
                    pageSize : 50,
                };
                unify.ViewModelManager.retrieveViewModel("CustomerListViewModel").getCustomerListRequest(condition);
            }
        });
    };

 
    this.CustomerAddViewModel.prototype.getDistrictArray = function() {
 
        var districtArray = [
            {districtCode:310101, districtName:"黄浦区"},
            {districtCode:310103, districtName:"卢湾区"},
            {districtCode:310104, districtName:"徐汇区"},
            {districtCode:310105, districtName:"长宁区"},
            {districtCode:310106, districtName:"静安区"},
            {districtCode:310107, districtName:"普陀区"},
            {districtCode:310108, districtName:"闸北区"},
            {districtCode:310109, districtName:"虹口区"},
            {districtCode:310110, districtName:"杨浦区"},
            {districtCode:310112, districtName:"闵行区"},
            {districtCode:310113, districtName:"宝山区"},
            {districtCode:310114, districtName:"嘉定区"},
            {districtCode:310115, districtName:"浦东新区"},
            {districtCode:310116, districtName:"金山区"},
            {districtCode:310117, districtName:"松江区"},
            {districtCode:310118, districtName:"青浦区"},
            {districtCode:310119, districtName:"南汇区"},
            {districtCode:310120, districtName:"奉贤区"},
            {districtCode:310230, districtName:"崇明县"},
        ];
        return districtArray;
    };

    this.CustomerAddViewModel.prototype.getAreaArray = function(params) {
        var condition = JSON.parse(params);
        var self = this;
        util.HttpUtil.getAreaList(condition.districtCode, function(response){
            self.notify(util.Constant.NOTIFY_NATIVE_GET_AREA_RESULT, response);
        });
    };

    this.CustomerAddViewModel.prototype.checkPhoneNORepeated = function(params) {
        var condition = JSON.parse(params);
        var self = this;
        util.HttpUtil.checkPhoneNORepeated(condition.phone, function(response){
            print("notify:" + util.Constant.NOTIFY_NATIVE_CHECK_PNONENO);
            self.notify(util.Constant.NOTIFY_NATIVE_CHECK_PNONENO, response);
        });
    };

}.call(this));