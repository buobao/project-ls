(function(){
	this.MessageListViewModel = function() {
		this.name = "MessageListViewModel";
        this.init();

        this.messageList = null;

        this.lastCondition = null;
    };
    this.MessageListViewModel.prototype = new unify.ViewModel();
    this.MessageListViewModel.prototype.constructor = this.MessageListViewModel;

    this.MessageListViewModel.prototype.getMessageList = function(params) {
        var condition = JSON.parse(params);
        var self = this;
        self.lastCondition = condition;

        if (condition.type == "new") {
            //如果是新的，则直接返回当前存在的列表
            if( this.messageList != null) {
                var response = util.HttpUtil.createResponseData();
                response.content = this.messageList;
                self.notify(util.Constant.NOTIFY_NATIVE_MESSAGE_LIST_RESULT, response);
                return;
            }
            condition.timeStamp = 0;
        } else if(self.messageList != null){
            condition.timeStamp = 0;
            for(var i=0;i<self.messageList.length;i++) {
                if(condition.timeStamp == 0) {
                    condition.timeStamp = self.messageList[i].msgTime;
                } else {
                    if(condition.timeStamp > self.messageList[i].msgTime)
                        condition.timeStamp = self.messageList[i].msgTime;
                }
            }
            condition.timeStamp -= 1;
        }
        this.getMessageRequest(condition.type, condition.timeStamp, 20, self, false);
    };

    this.MessageListViewModel.prototype.setMessageRead = function(params) {
        var condition = JSON.parse(params);
        var self = this;
        util.HttpUtil.setEventRead(condition.msgId, function(response){
            self.notify(util.Constant.NOTIFY_NATIVE_MESSAGE_READ_RESULT, response);
        });
    };

    // set timer, get newer message every 60s
    this.MessageListViewModel.prototype.startGetNewMessageTimer = function(self) {
        self.timer = setInterval(function(){
            var timestamp = 0;
            for(var i=0;i<self.messageList.length;i++) {
                if(timestamp < self.messageList[i].msgTime)
                    timestamp = self.messageList[i].msgTime;
            }
            timestamp += 1;
            self.getMessageRequest("new", timestamp, 20, self, true);
        }, 60000);
    };

    //客户端药缓存消息列表，UI层要的时候直接给。
    //需要定时查询更新数据
    //分页的地方，根据需要，做分页
    this.MessageListViewModel.prototype.getMessageRequest = function(type, timestamp, pageSize, self, isTimer) {
        util.HttpUtil.getEventList(type, timestamp, pageSize, function(response){

            // is msgType is null, delete it
            if(response.content != null && response.isSuccess){
                var eventLength = response.content.length -1;
                for(var i=eventLength;i>=0;i--){
                    var message = response.content[i];
                    if(message.msgType==null || message.msgType=="") {
                        response.content.splice(i, 1);
                    }
                }
            }

            if(isTimer) {
                if(response.isSuccess) {
                    var messageDict = {};
                    for(var k=0;k<self.messageList.length;k++) {
                        var msgId = self.messageList[k].msgId;
                        messageDict[msgId] = self.messageList[k];
                    }

                    for(var i=0;i<response.content.length;i++) {
                        var message = response.content[i];
                        if(messageDict[message.msgId] != null 
                            && messageDict[message.msgId] != undefined)
                            continue;
                        self.messageList.insert(0, message);
                    }
                }
                return;
            }

            if(response.content != null && response.isSuccess) {
                if(self.messageList == null) {
                    self.messageList = [];
                }

                //做一层过滤，防止出错
                var messageDict = {};
                for(var k=0;k<self.messageList.length;k++) {
                    var msgId = self.messageList[k].msgId;
                    messageDict[msgId] = self.messageList[k];
                }

                for(var i=0;i<response.content.length;i++) {
                    var message = response.content[i];
                    if(messageDict[message.msgId] != null 
                        && messageDict[message.msgId] != undefined)
                        continue;
                    self.messageList.push(message);
                }

                if(type == "new") {
                    response.params.action = "refresh";
                    response.content = self.messageList;
                } else {
                    response.params.action = "append";
                }
            }

            self.notify(util.Constant.NOTIFY_NATIVE_MESSAGE_LIST_RESULT, response);

            if (self.timer == null) {
                self.startGetNewMessageTimer(self);
            }
        });
    };

    this.MessageListViewModel.prototype.clearData = function() {
        // this.newestEvent = null;
        // this.oldestEvent = null;
    };
}.call(this));