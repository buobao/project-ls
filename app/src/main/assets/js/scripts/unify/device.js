(function() {

    device = this;

    /**
    ios: ios
    android: android
    */
    var platform = PlatformHelper.getTargetPlatform();
    if (platform == 1) {
        device.platform = "ios";
    } else if (platform == 2) {
        device.platform = "android";
    }


    /**
    获取当前设备的语言
    */
    device.getCurrentLanguage = function() {
        return PlatformHelper.getCurrentLanguage();
    };






}.call(this));

