(function(){

    this.HouseMapViewModel = function() {
        this.name = "HouseMapViewModel";
        this.init();
    };
    this.HouseMapViewModel.prototype = new unify.ViewModel();
    this.HouseMapViewModel.prototype.constructor = this.HouseMapViewModel;

        // find house in map
    this.HouseMapViewModel.prototype.getHouseInMap = function(params){
        var condition = JSON.parse(params);
        print(condition);
    print("HouseMapViewModel, condition  latMin is " + condition.latMin +"====");
        var self = this;
        util.HttpUtil.getHouseListByMap(condition.latMin, condition.latMax, 
            condition.attMin, condition.attMax, condition.delType, function(response) {
            self.notify(util.Constant.NOTIFY_NATIVE_HOU_LIST_INMAP_RESULT, response);
        });
    };
    
}.call(this));


