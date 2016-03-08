(function() {

    var AppProxy = function() {
    	print("on AppProxy func");
        this.name = "AppProxy";
    };

    AppProxy.prototype.init = function(arg) {
        print("on AppProxy init:", arg);
    };


    this.AppProxy = AppProxy;

}.call(this));
