(function(){

    this.HouseDetailViewModel = function() {
        this.name = "HouseDetailViewModel";
        this.init();
    };
    this.HouseDetailViewModel.prototype = new unify.ViewModel();
    this.HouseDetailViewModel.prototype.constructor = this.HouseDetailViewModel;

    // get house detail 
    this.HouseDetailViewModel.prototype.getHouseDetail = function(params){
        var condition = JSON.parse(params);
        var self = this;
        this.getHouseDetailRequest(self, condition.delCode);
    };

    this.HouseDetailViewModel.prototype.getHouseDetailRequest = function(self, delCode){
        util.HttpUtil.getHouseDetail(delCode, function(response) {
            if (response.content.square == "" || response.content.square == null) {
                response.content.unitprice = "" + response.content.price;
            } else 
                response.content.unitprice = "" + response.content.price / response.content.square;
            response.content.price = "" + response.content.price;
            response.content.prieviewImg = response.content.img;
            self.notify(util.Constant.NOTIFY_NATIVE_HOU_DETAIL_RESULT, response);
        });
    };

    // get contact list
    this.HouseDetailViewModel.prototype.getContactList = function(params){
        var condition = JSON.parse(params);
        var self = this;
        util.HttpUtil.getContactList(condition.delCode, function(response) {
            self.notify(util.Constant.NOTIFY_NATIVE_CONTACT_LIST_RESULT, response);
        });
    };

    // get track list
    this.HouseDetailViewModel.prototype.getTrackList = function(params){
        var condition = JSON.parse(params);
        var self = this;
        util.HttpUtil.getTrackList(condition.delCode, condition.page, condition.pageSize, function(response) {
            self.notify(util.Constant.NOTIFY_NATIVE_TRACK_LIST_RESULT, response);
        });
    };

    // claim house
    this.HouseDetailViewModel.prototype.claimHouse = function(params){
        var condition = JSON.parse(params);
        var self = this;
        util.HttpUtil.claimHouse(condition.delCode, function(response) {
            self.notify(util.Constant.NOTIFY_NATIVE_CLAIM_HOUSE_RESULT, response);
        });
    };

    // getShareLink
    this.HouseDetailViewModel.prototype.getShareLink = function(params){
        var condition = JSON.parse(params);
        var self = this;
        // util.HttpUtil.getShareLink(condition.delCode, function(response) {
        //     if(response.isSuccess){
        //         response.content.link += "?empId=" + util.Models.currUser.empId;
        //     }
        //     self.notify(util.Constant.NOTIFY_NATIVE_GET_SHARE_LINK_RESULT, response);
        // });
        var response = util.HttpUtil.createResponseData();
        response.content = "http://123.59.54.180:8082/sales-web/mobile/houShare/" 
            + condition.delCode + "/" + util.Models.CurrUser.empId;
        self.notify(util.Constant.NOTIFY_NATIVE_GET_SHARE_LINK_RESULT, response);
    };

    // borrow key from shops
    this.HouseDetailViewModel.prototype.borrowKeyFromShop = function(params) {
        var condition = JSON.parse(params);
        var self = this;
        util.HttpUtil.borrowKeyFromShop(condition.delCode, function(response){
            self.notify(util.Constant.NOTIFY_NATIVE_BORROW_KEY_FROM_SHOP_RESULT, response);
        });
    };

    // 录入跟进带看，需要用户信息和房源信息
    this.HouseDetailViewModel.prototype.addHouCustTrack = function(params) {
        var condition = JSON.parse(params);
        var self = this;
        util.HttpUtil.addHouCustTrack(condition.delCode, condition.custCode, condition.lookCode, condition.remark, function(response){
            self.notify(util.Constant.NOTIFY_NATIVE_ADD_HOU_CUST_TRACK_RESULT, response);
            if(response.isSuccess){
                self.getHouseDetailRequest(self, condition.delCode);
            }
        });
    };

    // 房源的跟进录入
    this.HouseDetailViewModel.prototype.addHouTrack = function(params) {
        var condition = JSON.parse(params);
        var self = this;
        util.HttpUtil.addHouTrack(condition.delCode, condition.content, function(response){
            self.notify(util.Constant.NOTIFY_NATIVE_ADD_HOU_TRACK_RESULT, response);
            if(response.isSuccess){
                self.getHouseDetailRequest(self, condition.delCode);
            }
        });
    };

    //解析url，获取房源编号
    //返回房源详情
    this.HouseDetailViewModel.prototype.getHouseInfoByQRCode = function(params) {
        var condition = JSON.parse(params);
        var self = this;
        var url = condition.url;
        var urlArray = url.split("/");
        var delCode = "";
        for(var i=0;i<urlArray.length;i++) {
            var urlContent = urlArray[i];
            if(urlContent == "houShare") {
                delCode = urlArray[i+1];
                break;
            }
        }
        print("delCode is " + delCode);
        this.getHouseDetailRequest(self, delCode);
    };
}.call(this));