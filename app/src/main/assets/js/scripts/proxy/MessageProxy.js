(function(){

	var MessageProxy = function(){
		this.name = "MessageProxy";
	};

	// get messgae list
	// params:
	//		type(new or old) page pageSize
	MessageProxy.prototype.getMessageList = function(params){
		unify.ViewModelManager.retrieveViewModel("MessageListViewModel").getMessageList(params);
		// this.messageListViewModel.getMessageList(params);
	}

	// set message read
	// params:
	//		msgId
	MessageProxy.prototype.setMessageRead = function(params){
		unify.ViewModelManager.retrieveViewModel("MessageListViewModel").setMessageRead(params);
		// this.messageListViewModel.setMessageRead(params);
	}

	// clean up MessageList
	// params:
	//		name
	MessageProxy.prototype.cleanupMessageList = function(){
		unify.ViewModelManager.retrieveViewModel("MessageListViewModel").clearData();
	}

	this.MessageProxy = MessageProxy;
}.call(this));