(function(){

	var CustomerProxy = function(){
		this.name = "CustomerProxy";
	};

	// get customer list
	// params:
	//		type page pageSize
	CustomerProxy.prototype.getCustomerList = function(params){
		unify.ViewModelManager.retrieveViewModel("CustomerListViewModel").getCustomerList(params);
		// this.customerListViewModel.getCustomerList(params);
	}

	// get customer info
	// params:
	//		custCode
	CustomerProxy.prototype.getCustomerInfo = function(params){
		unify.ViewModelManager.retrieveViewModel("CustomerDetailViewModel").getCustomerInfo(params);
		// this.customerDetailViewModel.getCustomerInfo(params);
	}

	// claim customer
	// params:
	//		custCode
	CustomerProxy.prototype.claimCustomer = function(params){
		unify.ViewModelManager.retrieveViewModel("CustomerDetailViewModel").claimCustomer(params);
		// this.customerDetailViewModel.claimCustomer(params);
	}

	// add customer
	// params:
	//		name phone qq wechat reqType(rent or buy) area acreage price other
	CustomerProxy.prototype.addCustomer = function(params){
		unify.ViewModelManager.retrieveViewModel("CustomerAddViewModel").addCustomer(params);
		// this.customerAddViewModel.addCustomer(params);
    }
		
	// customer track
	// params:
    //       custCode content
    // notify:
    //		NOTIFY_NATIVE_CUST_TRACK_RESULT = "notify_native_cust_track_result";
	CustomerProxy.prototype.addTrackInfo = function(params){
		unify.ViewModelManager.retrieveViewModel("CustomerDetailViewModel").addTrackInfo(params);
	}

	// get district array
	// params:
	//		null
	CustomerProxy.prototype.getDistrictArray = function(){
		return unify.ViewModelManager.retrieveViewModel("CustomerAddViewModel").getDistrictArray();
	}

 
	// tirggered when user enter customer enter customerAddView
 
	// get area list when user choose district
 	// params:
	//		districtCode
	// notify:
	// 		NOTIFY_NATIVE_GET_AREA_RESULT = "notify_native_get_area_result";
	CustomerProxy.prototype.getAreaArray = function(params){
		unify.ViewModelManager.retrieveViewModel("CustomerAddViewModel").getAreaArray(params);
	}

	CustomerProxy.prototype.checkPhoneNORepeated = function(params){
    	unify.ViewModelManager.retrieveViewModel("CustomerAddViewModel").checkPhoneNORepeated(params);
    }

	this.CustomerProxy = CustomerProxy;
}.call(this));