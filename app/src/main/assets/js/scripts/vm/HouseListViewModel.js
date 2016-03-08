(function(){

    this.HouseListViewModel = function() {
        this.name = "HouseListViewModel";
        this.init();
    };
    this.HouseListViewModel.prototype = new unify.ViewModel();
    this.HouseListViewModel.prototype.constructor = this.HouseListViewModel;

    //get rent house list
    this.HouseListViewModel.prototype.getHouseList= function(houseConditionMap) {
        var condition = JSON.parse(houseConditionMap);

        print("HoustListViewModel, condition  type is " + condition.type);
        condition.type = "" + condition.type;
        switch(condition.type){
            case "1":
                //出租
                condition.delType = "r";
                condition.listType = "NEAR_BY";
                this.houstListRequest(condition, util.Constant.NOTIFY_NATIVE_HOU_LIST_RESULT);
                break;
            case "2":
                //出售
                condition.delType = "s";
                condition.listType = "NEAR_BY";
                this.houstListRequest(condition, util.Constant.NOTIFY_NATIVE_HOU_LIST_RESULT);
                break;
            case "3":
                condition.listType = "APPO_HOULIST";
                this.houstListRequest(condition, util.Constant.NOTIFY_NATIVE_HOU_LIST_RESULT);
                break;
            case "4":
                condition.listType = "MY_HOULIST";
                this.houstListRequest(condition, util.Constant.NOTIFY_NATIVE_HOU_LIST_RESULT);
                break;
            case "5":
                condition.listType = "POOL_HOULIST";
                this.houstListRequest(condition, util.Constant.NOTIFY_NATIVE_HOU_LIST_RESULT);
                break;
            case "6":
                condition.listType = "KEY_HOULIST";
                this.houstListRequest(condition, util.Constant.NOTIFY_NATIVE_HOU_LIST_RESULT);
                break;
        }
    };

    // get http request: get houselist
    this.HouseListViewModel.prototype.houstListRequest = function(params, notifyName){
        var self = this;

        var temp = params.type;
		params.type = params.usageType;

        util.HttpUtil.getHouseList(params, function(response){
            //response.content.houseListType = params.houseListType;
            for(var i = 0; i<response.content.rows.length; i++)
            {
                if (response.content.rows[i].square == "" || response.content.rows[i].square == null) {
                    response.content.rows[i].unitprice = response.content.rows[i].price;
                } else
                    response.content.rows[i].unitprice = "" + (response.content.rows[i].price / ( response.content.rows[i].square));
                response.content.rows[i].price = "" + response.content.rows[i].price;
                response.content.rows[i].prieviewImg = response.content.rows[i].img;

                var imgObj = {type:"", url:response.content.rows[i].img};
                response.content.rows[i].img = [];
                response.content.rows[i].img.push(imgObj);
            }

            //add params: type action 
            response.params.type = temp;
            if (params.page == 1) {
                response.params.action = "refresh";
            } else {
                response.params.action = "append";
            }
            self.notify(notifyName, response);
        });
    };

    // search hou
    this.HouseListViewModel.prototype.searchHouList = function(params){
        var condition = JSON.parse(params);
        this.houstListRequest(condition, util.Constant.NOTIFY_NATIVE_HOU_LIST_SEARCH_RESULT);
    };

    // get house list when user click btn in the map
    this.HouseListViewModel.prototype.getHouListClickMap = function(params){
        var condition = JSON.parse(params);
        this.houstListRequest(condition, util.Constant.NOTIFY_NATIVE_HOU_LIST_CLICK_MAP_RESULT);
    };

    // get search items
    this.HouseListViewModel.prototype.searchEstateName = function(params){
        var condition = JSON.parse(params);
        var self = this;
        util.HttpUtil.searchEstataName(condition.name, condition.page, condition.pageSize, function(response) {
            self.notify(util.Constant.NOTIFY_NATIVE_SEARCH_ITEM_RESULT, response);
        });
    };

}.call(this));