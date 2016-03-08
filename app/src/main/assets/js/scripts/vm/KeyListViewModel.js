(function(){
    this.KeyListViewModel = function() {
        this.name = "KeyListViewModel";
        this.init();
        this.keyPassCanceled = false;
        this.receiverTimer = null;
        this.senderTimer = null;

        this.receiverUserCache = {};
        this.senderUserCache = {};
    };
    this.KeyListViewModel.prototype = new unify.ViewModel();
    this.KeyListViewModel.prototype.constructor = this.KeyListViewModel;

    // this.KeyListViewModel.prototype.checkKeyPass = function(params) {
    //     var condition = JSON.parse(params);
    //     var self = this;
    //     this.keyPassCanceled = false;
    //     this.keyPassCheckHttpRequest(condition.type, self);
    // };

    // this.KeyListViewModel.prototype.keyPassCheckHttpRequest = function(type, self) {
    //  if(this.keyPassCanceled)
    //      return;

    //     util.HttpUtil.keyPassCheck(type, function(response){
    //      if(response.isSuccess && response.content.length>0){
    //          // 0: success
    //          // 1: waiting 
    //             // 2: cancel
    //             var weHaveAResult = false;
    //             for(var i=0;i<response.content.length;i++) {
    //                 var keyInfo = response.content[i];
    //                 if(keyInfo.result == 0 || keyInfo.result == 2){
    //                     weHaveAResult = true;
    //                     break;
    //                 }
    //             }
    //             if(weHaveAResult){
    //                 self.notify(util.Constant.NOTIFY_NATIVE_PASS_CHECK, response);
    //             }
    //      } else {
    //          // net error or waiting for owner agree
    //          self.timer = setTimeout(function(){
    //                 clearTimeout(timeout);
    //              this.keyPassCheckHttpRequest(type, self);
    //          }, 2000);
    //      }
    //     });
    // };

    this.KeyListViewModel.prototype.senderCheckKeyPassTimer = function(self) {
        util.HttpUtil.keyPassCheck("send", function(response){
            if(response.isSuccess){
                if(response.content.length > 0){
                    // 0: success
                    // 1: waiting 
                    // 2: cancel
                    var weHaveAResult = false;
                    var weNeedToCheckAgain = false;
                    for(var i=0;i<response.content.length;i++) {
                        var keyInfo = response.content[i];
                        if(keyInfo.type == "receive")
                            continue;
                        if(keyInfo.result == 1){
                            var empKey = keyInfo.receiverInfo.empId + "" + keyInfo.keyNum;
                            if (self.receiverUserCache && self.receiverUserCache[empKey]) {
                                print("receiver found");
                            }else {
                                print("receiver not found");
                                weHaveAResult = true;
                                self.receiverUserCache[empKey] = keyInfo;
                            }
                        } else {
                            weNeedToCheckAgain = true;
                        }
                    }
                    if(weHaveAResult){
                        for(var i=response.content.length-1;i>=0;i--) {
                            var keyInfo = response.content[i];
                            keyInfo.receiverInfo.empId = "" + keyInfo.receiverInfo.empId;
                            if(keyInfo.type == "receive")
                                response.content.splice(i, 1);
                        }

                        self.notify(util.Constant.NOTIFY_NATIVE_PASS_CHECK, response);
                    } 
                } 
                if (!self.keyPassCanceled)
                    self.startOwnerTimer(self);
            } else if (!this.keyPassCanceled) {
                // net error or waiting for owner agree
                self.startOwnerTimer(self);
            }
        });
    };

    this.KeyListViewModel.prototype.startOwnerTimer = function(self) {
        if(self.senderTimer == null){
            self.senderTimer = setTimeout(function(){
                // clearTimeout(self.senderTimer);
                self.senderTimer = null;
                this.keyPassCanceled = false;
                self.senderCheckKeyPassTimer(self);
            }, 2000);       
        }
    };

    // cancel pass check when user left the view
    this.KeyListViewModel.prototype.cancelKeyPassCheck = function() {
        var self = this;
        this.keyPassCanceled = true;
        this.receiverUserCache = {};
        this.senderUserCache = {};
    };

    // get key house list 
    this.KeyListViewModel.prototype.getMyKeyList = function() {
        var self = this;
        util.HttpUtil.getMyKeyList(function(response){
            self.notify(util.Constant.NOTIFY_NATIVE_KEY_LIST_RESULT, response);
        });
    };

    // confirm key pass to receiver
    this.KeyListViewModel.prototype.confirmKeyPassToReceiver = function(params) {
        var condition = JSON.parse(params);
        var self = this;
        util.HttpUtil.confirmKeyPassToReceiver(condition.keyNum, function(response){
            self.notify(util.Constant.NOTIFY_NATIVE_CONFIRM_KEY_TO_RECEIVER_RESULT, response);
        });
    };

    // cancel keypass to receiver
    this.KeyListViewModel.prototype.cancelKeyPassToReceiver = function(params) {
        var condition = JSON.parse(params);
        var self = this;
        util.HttpUtil.cancelKeyPassToReceiver(condition.keyNum, function(response){
            self.notify(util.Constant.NOTIFY_NATIVE_CALCEL_KEY_TO_RECEIVER, response);
        });
    };

    // return key to shop
    this.KeyListViewModel.prototype.returnKeyToShop = function(params) {
        var condition = JSON.parse(params);
        var self = this;
        util.HttpUtil.returnKey(condition.keyNum, condition.delCode, function(response){
            self.notify(util.Constant.NOTIFY_NATIVE_RETURN_KEY_RESULT, response);
        });
    };

    // generate pincode
    this.KeyListViewModel.prototype.getPinCode = function(params) {
        var condition = JSON.parse(params);
        var keyNum = condition.keyNum;
        var random = Math.random() * 100000000000;
        var pincode = (random.toString()).substr(0, 4);
        var self = this;
        self.keyPassCanceled = false;
 
        util.HttpUtil.uploadPincode(keyNum, pincode, function(response){
            // cal local expiredtime 
            if (response.isSuccess) {
                var time = response.content.expireTime;
                response.content.expireTime -= util.Constant.DELTA_SERVER_TIME;
                response.params.data = pincode;
                self.receiverUserCache = {};
                self.startOwnerTimer(self);
            }
            self.notify(util.Constant.NOTIFY_NATIVE_SET_PINCODE_RESULT, response);
        });
    };

    // 获取钥匙管理列表，并且同时获取待确认的钥匙列表
    this.KeyListViewModel.prototype.getKeyListNConfirmedList = function(keyInfo) {
        var self = this;
        util.HttpUtil.getMyKeyList(function(responseData){
            if(responseData.isSuccess){
                keyInfo.isWaitingConfirm = true;
                                   print(keyInfo);
                responseData.content.splice(0, 0, keyInfo);
            }
            self.notify(util.Constant.NOTIFY_NATIVE_KEY_LIST_RESULT, responseData);

            //同时开启定时器，一段时间不断轮训获取结果
            if(keyInfo){
                this.senderUserCache = {};
                self.startReceiverTimer(self);
            }
        });
    };

    // receiver，接收钥匙，check result
    this.KeyListViewModel.prototype.receiverCheckKeyPassTimer = function(self) {
        util.HttpUtil.keyPassCheck("receive", function(response){
            if(response.isSuccess){
                if(response.content.length > 0){
                    // 0: success
                    // 1: waiting 
                    // 2: cancel
                    var weHaveAResult = false;
                    var weNeedToCheckAgain = false;
                    for(var i=0;i<response.content.length;i++) {
                        var keyInfo = response.content[i];
                        if(keyInfo.type == "send")
                            continue;
                        var empKey = keyInfo.keyNum + "" + keyInfo.result + keyInfo.receiverInfo.empId;
                        if(keyInfo.result == 0 || keyInfo.result == 2){
                            weNeedToCheckAgain = true;
                            if (self.senderUserCache && self.senderUserCache[empKey]) {

                            } else {
                                weHaveAResult = true;
                                self.senderUserCache[empKey] = true;
                            }
                            break;
                        } else {
                            weHaveAResult = false;
                            weNeedToCheckAgain = true;
                        }
                        weNeedToCheckAgain = true;
                    }
                    print("weHaveAResult is " + weHaveAResult);
                    if(weHaveAResult){
                        for(var i=response.content.length-1;i>=0;i--) {
                            var keyInfo = response.content[i];
                            if(keyInfo.type == "send")
                                response.content.splice(i, 1);
                        }
                        self.notify(util.Constant.NOTIFY_NATIVE_PASS_CHECK_RECEIVER, response);
                    } 
                    if(weNeedToCheckAgain && !self.keyPassCanceled) {

                    }  
                    if(weNeedToCheckAgain) {
                        self.startReceiverTimer(self);
                    }
                } else {
                    //waiting
                     self.startReceiverTimer(self);
                }
            } else {
                // net error or waiting for owner agree
                self.startReceiverTimer(self);
            }
        });
    };

    this.KeyListViewModel.prototype.startReceiverTimer = function(self) {
        if(self.receiverTimer == null){
            self.receiverTimer = setTimeout(function(){
                print(self.receiverTimer);
                print("wanggsx unify JS")
                // clearTimeout(self.receiverTimer);
                print(self.receiverTimer);
                print("wanggsx unify JS")
                self.receiverTimer = null;
                self.receiverCheckKeyPassTimer(self);
            }, 2000);       
        }
    };

}.call(this));