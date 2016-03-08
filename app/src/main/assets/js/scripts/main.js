require("unify/init");
require("util/init");
require("vm/init");

var _ = require("unify/libs/lodash")._;
var AppProxy = require("proxy/AppProxy").AppProxy;
var LoginProxy = require("proxy/LoginProxy").LoginProxy;
var HouseResourceProxy = require("proxy/HouseResourceProxy").HouseResourceProxy;
var CustomerProxy = require("proxy/CustomerProxy").CustomerProxy;
var KeyProxy = require("proxy/KeyProxy").KeyProxy;
var MessageProxy = require("proxy/MessageProxy").MessageProxy;


function main() {
    unify.Facade.registerProxy(new AppProxy());
    unify.Facade.registerProxy(new LoginProxy());
    unify.Facade.registerProxy(new HouseResourceProxy());
    unify.Facade.registerProxy(new CustomerProxy());
    unify.Facade.registerProxy(new KeyProxy());
    unify.Facade.registerProxy(new MessageProxy());

    this.globalFunction = function(name) {
        var result = name + "::" + name;
        print("JS: ", result);
        return result;
    };

    // 提供给Native层调用Proxy的公用方法
    this.callProxy = function(proxyName, funcName, $) {
        try{
            var proxy = unify.Facade.retrieveProxy(proxyName);
            print("call proxy: proxy is " + proxyName + ", func is " + funcName);
            if (proxy && funcName in proxy) {
                return proxy[funcName].apply(proxy, _.slice(arguments, 2));
            } else {
                print("Proxy or function not found!", proxyName, funcName);
            }
        } catch(e) {
            print(e.stack);
        }
            
    };
}


try {
    main.call(this.global);
} catch(e) {
    print(e.stack);
}