(function(){

    this.KeyReceivePinViewModel = function() {
        this.name = "KeyReceivePinViewModel";
        this.init();
    };
    this.KeyReceivePinViewModel.prototype = new unify.ViewModel();
    this.KeyReceivePinViewModel.prototype.constructor = this.KeyReceivePinViewModel;

    // confirm pincode
    this.KeyReceivePinViewModel.prototype.confirmPincode = function(params){
    	var condition = JSON.parse(params);
        var self = this;
        util.HttpUtil.confirmPinCode(condition.pinCode, function(response){
//             if(response.isSuccess){
//                 var confirmResult = response;
//                 // combine the keyhouse and my house list
//                 util.HttpUtil.getMyKeyHouseList(function(response){
//                     if(response.isSuccess){
//                         response.content.rows.unshift(confirmResult.keyInfo);
//                     }
//                 });
//             }
            self.notify(util.Constant.NOTIFY_NATIVE_CONFIRM_PINCODE, response);

            // 这里只需要通知UI，是否成功。至于钥匙管理列表，这里不获取，之后轮训再获取
            // 通知钥匙管理列表，获取最新的列表
            if(response.isSuccess){
                unify.ViewModelManager.retrieveViewModel("KeyListViewModel").getKeyListNConfirmedList(response.content.keyInfo);
            }
        });
    };

}.call(this));