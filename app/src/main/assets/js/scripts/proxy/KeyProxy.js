(function(){

	var KeyProxy = function(){
		this.name = "KeyProxy";
	};

	// confirm pincode when receiver input pincode
	// params:
	//		pinCode
	// notify:
	// 		NOTIFY_NATIVE_CONFIRM_PINCODE = "notify_native_confirm_pincode_result";
	//		NOTIFY_NATIVE_PASS_CHECK_RECEIVER = "notify_native_pass_check_by_receiver_result" 列表的地方，判断某个cell的传递结果
	KeyProxy.prototype.confirmPincode = function(params){
		unify.ViewModelManager.retrieveViewModel("KeyReceivePinViewModel").confirmPincode(params);
	}

	// check keypass result
	// params:
	//		pincode type
//	KeyProxy.prototype.checkKeyPass = function(params){
//		unify.ViewModelManager.retrieveViewModel("KeyListViewModel").checkKeyPass(params);
//		// this.keyListViewModel.checkKeyPass(params);
//	}

	// cancel keypass check
	// params:
	//		null
	KeyProxy.prototype.cancelKeyPass = function(){
		unify.ViewModelManager.retrieveViewModel("KeyListViewModel").cancelKeyPassCheck();
		// this.keyListViewModel.cancelKeyPassCheck();
	}

	// get my key list
	//	params: null
	KeyProxy.prototype.getMyKeyList = function(){
		unify.ViewModelManager.retrieveViewModel("KeyListViewModel").getMyKeyList();
		// this.keyListViewModel.getMyKeyHouseList();
	}

	// owner click receiver avatr to pass key 
	// params:
	//		keyNum
    // notify:
    //      NOTIFY_NATIVE_CONFIRM_KEY_TO_RECEIVER_RESULT
	KeyProxy.prototype.confirmKeyPassToReceiver = function(params){
		unify.ViewModelManager.retrieveViewModel("KeyListViewModel").confirmKeyPassToReceiver(params);
		// this.keyListViewModel.confirmKeyPassToReceiver(params);
	}

	// cancel passkey to receiver
	//	params:
	//		keyNum
	KeyProxy.prototype.cancelKeyPassToReceiver = function(params){
		unify.ViewModelManager.retrieveViewModel("KeyListViewModel").cancelKeyPassToReceiver(params);
		// this.keyListViewModel.cancelKeyPassToReceiver(params);
	}

	// borrow key from shops
	//	params:
	//		delCode
	KeyProxy.prototype.borrowKeyFromShop = function(params){
		unify.ViewModelManager.retrieveViewModel("HouseDetailViewModel").borrowKeyFromShop(params);
		// this.houseDetailViewModel.borrowKeyFromShop(params);
	}

	// return key to shop
	// params:
	//		keyNum delCode
	KeyProxy.prototype.returnKeyToShop = function(params){
		unify.ViewModelManager.retrieveViewModel("KeyListViewModel").returnKeyToShop(params);
		// this.keyListViewModel.returnKeyToShop(params);
	}

	// get pincode, generate by local
	// params:
	//		keyNum
	// notify:
	//		NOTIFY_NATIVE_SET_PINCODE_RESULT
    //      NOTIFY_NATIVE_PASS_CHECK, receiver's info
	KeyProxy.prototype.getPinCode = function(params){
		unify.ViewModelManager.retrieveViewModel("KeyListViewModel").getPinCode(params);
	}

	this.KeyProxy = KeyProxy;
}.call(this));