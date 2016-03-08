(function(){
	var ViewModelManager = this;
	ViewModelManager.viewModels = {};

	ViewModelManager.registerViewModel = function(viewmodel){
		if (!this.retrieveViewModel(viewmodel.name)) {
			print("viewmodel name is " + viewmodel.name);
			this.viewModels[viewmodel.name] = viewmodel;
		} else {
			print("Can't register viewmodel, (" + viewmodel.name + ") is registered! ");
		}
	};

	ViewModelManager.unregisterViewmodel = function(viewmodel) {
        if (!viewmodel || !this.retrieveViewModel(viewmodel.name)) return;

        if (typeof viewmodel.cleanup == "function") { viewmodel.cleanup(); }
        delete this.viewModels[viewmodel.name];
    };

	ViewModelManager.retrieveViewModel = function(viewmodelName) {
        return this.viewModels[viewmodelName];
    };
}.call(this));