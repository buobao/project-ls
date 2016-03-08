(function(){

    this.KeyResourceViewModel = function() {
        this.name = "KeyResourceViewModel";
        this.init();
    };
    this.KeyResourceViewModel.prototype = new unify.ViewModel();
    this.KeyResourceViewModel.prototype.constructor = this.KeyResourceViewModel;

    this.KeyResourceViewModel.prototype.listNotificationInterests = function() {
        return [
            util.Constant.NOTIFY_GET_KEY_MY_RESULT,
            util.Constant.NET_GET_KEY_PINCODE,
        ];
    };

    this.KeyResourceViewModel.prototype.handleNotification = function(notification) {
        if (notification.name == util.Constant.NOTIFY_GET_KEY_MY_RESULT) {

        }else if(notification.name == util.Constant.NET_GET_KEY_PINCODE) {

        }
    };

    this.KeyResourceViewModel.prototype.getKeyListMy = function(page, pageSize) {

    };

    this.KeyResourceViewModel.prototype.getPinCode = function(keyNum) {

    };

}.call(this));