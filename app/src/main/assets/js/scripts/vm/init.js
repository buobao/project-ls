(function(){
	try{
		var CustomerAddViewModel = require("vm/CustomerAddViewModel").CustomerAddViewModel;
		unify.ViewModelManager.registerViewModel(new CustomerAddViewModel());

		var CustomerDetailViewModel = require("vm/CustomerDetailViewModel").CustomerDetailViewModel;
		unify.ViewModelManager.registerViewModel(new CustomerDetailViewModel());

		var CustomerListViewModel = require("vm/CustomerListViewModel").CustomerListViewModel;
		unify.ViewModelManager.registerViewModel(new CustomerListViewModel());

		var KeyListViewModel = require("vm/KeyListViewModel").KeyListViewModel;
		unify.ViewModelManager.registerViewModel(new KeyListViewModel());

		var KeyReceivePinViewModel = require("vm/KeyReceivePinViewModel").KeyReceivePinViewModel;
		unify.ViewModelManager.registerViewModel(new KeyReceivePinViewModel());

		var KeyResourceViewModel = require("vm/KeyResourceViewModel").KeyResourceViewModel;
		unify.ViewModelManager.registerViewModel(new KeyResourceViewModel());

		var HouseDetailViewModel = require("vm/HouseDetailViewModel").HouseDetailViewModel;
		unify.ViewModelManager.registerViewModel(new HouseDetailViewModel());

		var HouseListViewModel = require("vm/HouseListViewModel").HouseListViewModel;
		unify.ViewModelManager.registerViewModel(new HouseListViewModel());

		var HouseMapViewModel = require("vm/HouseMapViewModel").HouseMapViewModel;
		unify.ViewModelManager.registerViewModel(new HouseMapViewModel());

		var HouseProspectingViewModel = require("vm/HouseProspectingViewModel").HouseProspectingViewModel;
		unify.ViewModelManager.registerViewModel(new HouseProspectingViewModel());

		var LoginViewModel = require("vm/LoginViewModel").LoginViewModel;
		unify.ViewModelManager.registerViewModel(new LoginViewModel());

		var MessageListViewModel = require("vm/MessageListViewModel").MessageListViewModel;
		unify.ViewModelManager.registerViewModel(new MessageListViewModel());
	}catch(e){
		print(e.stack);
	}
}.call(this));