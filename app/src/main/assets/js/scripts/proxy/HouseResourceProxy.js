(function(){

    var HouseResourceProxy = function(){
    	this.name = "HouseResourceProxy";
    };
    
    // get house list
    //  params:
    //      出租： type:1 price square frame tag usageType page pageSize sidx(acre/price) sord(asc/desc) searchId searchType
    //      出售:  type:2 price square frame tag usageType page pageSize sidx sord searchId searchType
    //      预约:  type:3 price square frame tag usageType page pageSize sidx sord searchId searchType
    //      我的:  type:4 price square frame tag usageType page pageSize sidx sord searchId searchType
    //      公房:  type:5 price square frame tag usageType page pageSize sidx sord searchId searchType
    //      钥匙:  type:6 price square frame tag usageType page pageSize sidx sord searchId searchType
    // notify:
    //      response.params:
    //          type 1 2 3 4 5 6 
    //          action refresh append
    HouseResourceProxy.prototype.getHouseList = function(params){
        unify.ViewModelManager.retrieveViewModel("HouseListViewModel").getHouseList(params);
    	// this.houseListViewModel.getHouseList(params);
    };

    // get search items
    //  params:
    //      name page pageSize
    HouseResourceProxy.prototype.searchEstateName = function(params){
        unify.ViewModelManager.retrieveViewModel("HouseListViewModel").searchEstateName(params);
    	// this.houseListViewModel.searchEstateName(params);
    };

    // get house in map
    //  params:
    //      delType latMin latMax attMin attMax
    HouseResourceProxy.prototype.getHouseInMap = function(params){
        unify.ViewModelManager.retrieveViewModel("HouseMapViewModel").getHouseInMap(params);
        // this.houseMapViewModel.getHouseInMap(params);
    };

    // search hou list
    // params:
    //      page pageSize searchId searchType sidx sord
    HouseResourceProxy.prototype.searchHouList = function(params){
        unify.ViewModelManager.retrieveViewModel("HouseListViewModel").searchHouList(params);
        // this.houseListViewModel.searchHouList(params);
    };

    // User click estate in the 
    // params:
    //      page pageSize searchId searchType
    HouseResourceProxy.prototype.getMapHouseList = function(params){
        unify.ViewModelManager.retrieveViewModel("HouseListViewModel").searchHouList(params);
        // this.houseListViewModel.searchHouList(params);
    };

    // get house detail info
    // params:
    //      delCode
    HouseResourceProxy.prototype.getHouseDetail = function(params){
        unify.ViewModelManager.retrieveViewModel("HouseDetailViewModel").getHouseDetail(params);
        // this.houseDetailViewModel.getHouseDetail(params);
    };

    // get contact list
    // params:
    //      delCode
    HouseResourceProxy.prototype.getContactList = function(params){
        unify.ViewModelManager.retrieveViewModel("HouseDetailViewModel").getContactList(params);
        // this.houseDetailViewModel.getContactList(params);
    };

    // get track list
    // params:
    //      delCode page pageSize
    HouseResourceProxy.prototype.getTrackList = function(params){
        unify.ViewModelManager.retrieveViewModel("HouseDetailViewModel").getTrackList(params);
        // this.houseDetailViewModel.getTrackList(params);
    };

    // claim house
    // params"
    //      delCode
    HouseResourceProxy.prototype.claimHouse = function(params){
        unify.ViewModelManager.retrieveViewModel("HouseDetailViewModel").claimHouse(params);
        // this.houseDetailViewModel.claimHouse(params);
    };

    // get house share link
    // params:
    //      delCode
    // notify:
    //      NOTIFY_NATIVE_GET_SHARE_LINK_RESULT
    //      content: url
    HouseResourceProxy.prototype.getShareLink = function(params){
        unify.ViewModelManager.retrieveViewModel("HouseDetailViewModel").getShareLink(params);
        // this.houseDetailViewModel.getShareLink(params);
    };

    // 跟进带看
    // params:
    //      delCode custCode lookCode remark
    // notify:
    //      NOTIFY_NATIVE_ADD_HOU_CUST_TRACK_RESULT = "notify_native_hou_cust_track_result";
    HouseResourceProxy.prototype.addHouCustomerTrack = function(params){
        unify.ViewModelManager.retrieveViewModel("HouseDetailViewModel").addHouCustTrack(params);
        // this.houseDetailViewModel.getShareLink(params);
    };

    // 房源跟进
    // params:
    //      delCode content
    // notify:
    //      NOTIFY_NATIVE_ADD_HOU_TRACK_RESULT = "notify_native_hou_add_track_result";
    HouseResourceProxy.prototype.addHouTrack = function(params){
        unify.ViewModelManager.retrieveViewModel("HouseDetailViewModel").addHouTrack(params);
        // this.houseDetailViewModel.getShareLink(params);
    };

    // scan QR coce, get house info
    // params:
    //      url
    // notify:
    //      NOTIFY_NATIVE_HOU_DETAIL_RESULT
    HouseResourceProxy.prototype.getHouseInfoByQRCode = function(params){
        unify.ViewModelManager.retrieveViewModel("HouseDetailViewModel").getHouseInfoByQRCode(params);
        // this.houseDetailViewModel.getShareLink(params);
    };

    // upload photos
    // params:
    //      delCode, pics:[{pic, type, desc},{pic, type, desc}]
    // notify:
    //      NOTIFY_NATIVE_UPLOAD_IMGS_RESULT = "notify_native_upload_imgs_result";
    HouseResourceProxy.prototype.uploadImages = function(params){
        unify.ViewModelManager.retrieveViewModel("HouseProspectingViewModel").uploadImages(params);
    };

    this.HouseResourceProxy = HouseResourceProxy;
}).call(this);	