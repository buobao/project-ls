(function(){
    print("HttpUitl init");
    var HttpUtil = {};

    //before callback
    //if responseData is null， failed
    HttpUtil.beforeCallback = function(response, statusCode, callback){
        var responseData = {};
        if(statusCode == 200){
            var data = JSON.parse(response);
            responseData = data;
            responseData.params = {type:0, action:""};

            if(responseData.isSuccess) {
                // 更新自己的token
                util.Models.currUser.token = responseData.token;
                // if errors, put msg to response.msg
                if (util.AppUtil.checkValueIsNull(responseData, "content") == false 
                        && responseData.content.isSuccess == false) {
                    responseData.isSuccess = responseData.content.isSuccess;
                    if (responseData.content.hasOwnProperty("msg")) {
                        responseData.msg = responseData.content.msg;
                    }
                }
            } else {

            }
                
        }else{
            responseData = {};
            responseData.content = null;
            responseData.params = {type:0, action:""};
            responseData.empId = util.Models.currUser.empId;
            responseData.token = util.Models.currUser.token;
            responseData.isSuccess = false;
            responseData.msg = "net work error";
        }
        print("HttpUtil response, status is " + statusCode);
        print("HttpUtil response is " + response);
        return responseData;
    };

    HttpUtil.createResponseData = function(){
        responseData = {};
        responseData.empId = util.Models.currUser.empId;
        responseData.token = util.Models.currUser.token;
        responseData.isSuccess = true;
        responseData.content = null;
        responseData.params = {type:0, action:""};
        return responseData;
    }

	HttpUtil.login = function(userName, passWord, callback){
        print("HttpUtil-login, start login, username is " + userName + ", password is " + passWord);
        http.post(util.Constant.NET_DOMAIN + util.Constant.NET_LOGIN_URL,
            {
                source:"agencyApp",
                username:userName,
                password:passWord,
            },
            function(statusCode, response) {
                var responseData = HttpUtil.beforeCallback(response, statusCode, callback);
                callback.call(null, responseData);
            }
        );
    };

    HttpUtil.getServerTime = function(callback){
        print("HttpUtil-getServerTime, start get data")
        http.post(util.Constant.NET_DOMAIN + util.Constant.NET_GET_SERVER_TIME,
            {
                empId : util.Models.currUser.empId,
                token : util.Models.currUser.token,
            },
            function(statusCode, response) {
                var responseData = HttpUtil.beforeCallback(response, statusCode, callback);
                callback.call(null, responseData);
            }
        );
    }


    ///////////////////////////////////////////////
    ///   house 
    ///////////////////////////////////////////////

	HttpUtil.getHouseListByMap = function(latMin, latMax, attMin, attMax, delType, callback){
    	print("HttpUtil-getRentHouseList, start get data");
        http.post(util.Constant.NET_DOMAIN + util.Constant.NET_GET_HOUSE_MAP_LIST,
            {
                //params
                empId : util.Models.currUser.empId,
                token : util.Models.currUser.token,
                latMin : latMin,
                latMax : latMax,
                attMin : attMin,
                attMax : attMax,
                delType : delType,
            },
            function(statusCode, response) {
                var responseData = HttpUtil.beforeCallback(response, statusCode, callback);
                callback.call(null, responseData);
            }
        );
    };

    //get rent house list
    HttpUtil.getHouseList= function(params, callback) {
        params.empId = util.Models.currUser.empId;
        params.token = util.Models.currUser.token;
		
		var p = {
	                empId : params.empId,
	                token : params.token,
	                listType : params.listType,
	                lat : params.lat,
	                att : params.att,
	                searchId : params.searchId,
	                searchType : params.searchType,
	                delType : params.delType,
	                price : params.price,
	                square : params.square,
	                frame : params.frame,
	                tag : params.tag,
	                page : params.page,
	                pageSize : params.pageSize,
	                sidx : params.sidx,
	                sord : params.sord,
	                type : params.type,
	            };
	    print("wanggsx HttpUtil.getHouseList, params is " + JSON.stringify(p));
        http.post(util.Constant.NET_DOMAIN + util.Constant.NET_GET_HOUSE_RENT_LIST,
            p,
            function(statusCode, response) {
                print("house response is " + response);
                var responseData = HttpUtil.beforeCallback(response, statusCode, callback);
                callback.call(null, responseData);
            }
        );
    };

    HttpUtil.getHouseDetail = function(delCode, callback) {
    	print("HttpUtil-getHouseDetail, start get data");
        http.post(util.Constant.NET_DOMAIN + util.Constant.NET_GET_HOUSE_DETAIL,
            {
                //params
                empId : util.Models.currUser.empId,
                token : util.Models.currUser.token,
                delCode : delCode
            },
            function(statusCode, response) {
                var responseData = HttpUtil.beforeCallback(response, statusCode, callback);
                callback.call(null, responseData);
            }
        );
    };

    HttpUtil.claimHouse = function(delCode, callback) {
        print("HttpUtil-claimHouse, start get data")
        http.post(util.Constant.NET_DOMAIN + util.Constant.NET_CLAIM_HOUSE,
            {
                //params
                empId : util.Models.currUser.empId,
                token : util.Models.currUser.token,
                delCode : delCode
            },
            function(statusCode, response) {
                var responseData = HttpUtil.beforeCallback(response, statusCode, callback);
                callback.call(null, responseData);
            }
        );
    };
	
	HttpUtil.getContactList = function(delCode, callback) {
    	print("HttpUtil-getContactList, start get data")
        http.post(util.Constant.NET_DOMAIN + util.Constant.NET_GET_HOUSE_CONTACT_LIST,
            {
                //params
                empId : util.Models.currUser.empId,
                token : util.Models.currUser.token,
                delCode : delCode
            },
            function(statusCode, response) {
                var responseData = HttpUtil.beforeCallback(response, statusCode, callback);
                callback.call(null, responseData);
            }
        );
    };

    HttpUtil.getTrackList = function(delCode, page, pageSize) {
    	print("HttpUtil-getTrackList, start get data")
        http.post(util.Constant.NET_DOMAIN + util.Constant.NET_GET_HOUSE_TRACK_LIST,
            {
                //params
                empId : util.Models.currUser.empId,
                token : util.Models.currUser.token,
                delCode : delCode,
                page : page,
                pageSize : pageSize
            },
            function(statusCode, response) {
                var responseData = HttpUtil.beforeCallback(response, statusCode, callback);
                callback.call(null, responseData);
            }
        );
    };

    HttpUtil.addHouTrack = function (delCode, content, callback) {
    	print("HttpUtil-addHouTrack, start send data");
    	http.post(util.Constant.NET_DOMAIN + util.Constant.NET_SET_HOUSE_TRACK,
            {
                //params
                empId : util.Models.currUser.empId,
                token : util.Models.currUser.token,
                delCode : delCode,
                content : content,
            },
            function(statusCode, response) {
                var responseData = HttpUtil.beforeCallback(response, statusCode, callback);
                callback.call(null, responseData);
            }
        );
    };

    HttpUtil.addHouCustTrack = function (delCode, custCode, lookCode, remark, callback) {
        print("HttpUtil-addHouCustTrack, start send data");
        http.post(util.Constant.NET_DOMAIN + util.Constant.NET_HOU_CUSTOMER_TRACK,
            {
                //params
                empId : util.Models.currUser.empId,
                token : util.Models.currUser.token,
                delCode : delCode,
                custCode : custCode,
                lookCode : lookCode,
                remark : remark,
            },
            function(statusCode, response) {
                var responseData = HttpUtil.beforeCallback(response, statusCode, callback);
                callback.call(null, responseData);
            }
        );
    };

    HttpUtil.getShareLink = function(delCode, callback) {
        print("HttpUtil-getShareLink, start send data");
        http.post(util.Constant.NET_DOMAIN + util.Constant.NET_GET_SHARE_LINK,
            {
                //params
                empId : util.Models.currUser.empId,
                token : util.Models.currUser.token,
                delCode : delCode,
            },
            function(statusCode, response) {
                var responseData = HttpUtil.beforeCallback(response, statusCode, callback);
                callback.call(null, responseData);
            }
        );
    };

    // HttpUtil.searchEstataName = function (name, page, pageSize, callback) {
    //     print("HttpUtil-searchEstataName, start send data, name is " + name);
    //     print("HttpUtil-searchEstataName, URL IS  " + util.Constant.NET_DOMAIN + util.Constant.NET_SEARCH_ESTATE_NAME);
    //     http.post(util.Constant.NET_DOMAIN + util.Constant.NET_SEARCH_ESTATE_NAME,
    //         {
    //             //params
    //             empId : util.Models.currUser.empId,
    //             token : util.Models.currUser.token,
    //             name : util.AppUtil.urlencode(name),
    //             page : page,
    //             pageSize : pageSize,
    //         },
    //         function(statusCode, response) {
    //             var responseData = HttpUtil.beforeCallback(response, statusCode, callback);
    //             callback.call(null, responseData);
    //         }
    //     );
    // };

    HttpUtil.searchEstataName = function (name, page, pageSize, callback) {
        print("HttpUtil-searchEstataName, start send data, name is " + name);
        print("HttpUtil-searchEstataName, URL IS  " + util.Constant.NET_DOMAIN + util.Constant.NET_SEARCH_ESTATE_NAME);
        var params = "?token=" + util.Models.currUser.token;
        params += "&name=" + util.AppUtil.urlencode(name) + "&page=" + page + "&pageSize=" + pageSize;
        print(util.Constant.NET_DOMAIN + util.Constant.NET_SEARCH_ESTATE_NAME + params);
        http.get(util.Constant.NET_DOMAIN + util.Constant.NET_SEARCH_ESTATE_NAME + params,
            function(statusCode, response) {
                var responseData = HttpUtil.beforeCallback(response, statusCode, callback);
                callback.call(null, responseData);
            }
        );
    };

    HttpUtil.addPhotos = function (delCode, pics, callback) {
        print("HttpUtil-addPhotos, start send data");
        var params = "?token=" + util.Models.currUser.token + "&delCode=" + delCode;
        for(var i=0;i<pics.length;i++) {
            params += "&pics[" + i + "].type=" + pics[i].type;
            params += "&";
            params += "pics[" + i + "].pic=" + pics[i].pic;
        }
        print("url is " + util.Constant.NET_DOMAIN + util.Constant.NET_ADD_IMGS + params);
        http.get(util.Constant.NET_DOMAIN + util.Constant.NET_ADD_IMGS + params,
            function(statusCode, response) {
                var responseData = HttpUtil.beforeCallback(response, statusCode, callback);
                callback.call(null, responseData);
            }
        );
    };
   


    ///////////////////////////////////////////////
    ///   customer 
    ///////////////////////////////////////////////

    HttpUtil.getCustomerList = function (type, page, pageSize, callback) {
        print("HttpUtil-getCustomerList, start send data");
        print("customerList, "+type+", "+page+", "+pageSize)
        http.post(util.Constant.NET_DOMAIN + util.Constant.NET_GET_CUSTOMER_PUBLIC,
            {
                //params
                empId : util.Models.currUser.empId,
                token : util.Models.currUser.token,
                page : page,
                pageSize : pageSize,
                type : type,
            },
            function(statusCode, response) {
                var responseData = HttpUtil.beforeCallback(response, statusCode, callback);
                callback.call(null, responseData);
            }
        );
    };

    HttpUtil.getCustomerInfo = function (custCode, callback) {
        print("HttpUtil-getCustomerInfo, start send data");
        http.post(util.Constant.NET_DOMAIN + util.Constant.NET_GET_CUSTOMER_DETAIL,
            {
                //params
                empId : util.Models.currUser.empId,
                token : util.Models.currUser.token,
                custCode : custCode,
            },
            function(statusCode, response) {
                var responseData = HttpUtil.beforeCallback(response, statusCode, callback);
                callback.call(null, responseData);
            }
        );
    };

    HttpUtil.claimCustomer = function (custCode, callback) {
        print("HttpUtil-claimCustomer, start send data");
        http.post(util.Constant.NET_DOMAIN + util.Constant.NET_CLAIM_CUSTOMER,
            {
                //params
                empId : util.Models.currUser.empId,
                token : util.Models.currUser.token,
                custCode : custCode,
            },
            function(statusCode, response) {
                var responseData = HttpUtil.beforeCallback(response, statusCode, callback);
                callback.call(null, responseData);
            }
        );
    };

    HttpUtil.addCustomer = function (params, callback) {
        print("HttpUtil-addCustomer, start send data");
        http.post(util.Constant.NET_DOMAIN + util.Constant.NET_ADD_CUSTOMER,
            {
                //params
                empId : util.Models.currUser.empId,
                token : util.Models.currUser.token,
                name : params.name,
                phone : params.phone,
                qq : params.qq,
                wechat : params.wechat,
                reqType : params.reqType,
                area : params.area,
                acreage : params.acreage,
                price : params.price,
                other : params.other,
                //cust info
            },
            function(statusCode, response) {
                var responseData = HttpUtil.beforeCallback(response, statusCode, callback);
                callback.call(null, responseData);
            }
        );
    };

    HttpUtil.addCustomerTrack = function (custCode, content, callback) {
        print("HttpUtil-addCustomerTrack, start send data");
        http.post(util.Constant.NET_DOMAIN + util.Constant.NET_ADD_CUSTOMER_TRACK,
            {
                //params
                empId : util.Models.currUser.empId,
                token : util.Models.currUser.token,
                //cust info
                custCode : custCode,
                remark : content,
            },
            function(statusCode, response) {
                var responseData = HttpUtil.beforeCallback(response, statusCode, callback);
                callback.call(null, responseData);
            }
        );
    };

    HttpUtil.getAreaList = function (districtCode, callback) {
        print("HttpUtil-getAreaList, start send data");
        http.post(util.Constant.NET_DOMAIN + util.Constant.NET_AREA_LIST,
            {
                empId : util.Models.currUser.empId,
                token : util.Models.currUser.token,
                districtCode : districtCode,
            },
            function(statusCode, response) {
                var responseData = HttpUtil.beforeCallback(response, statusCode, callback);
                callback.call(null, responseData);
            }
        );
    };

    ///////////////////////////////////////////////
    ///   key 
    ///////////////////////////////////////////////

    HttpUtil.getMyKeyList = function(callback){
        print("HttpUtil-getMyKeyList, start send data");
        http.post(util.Constant.NET_DOMAIN + util.Constant.NET_GET_KEY_LIST_MY,
            {
                //params
                empId : util.Models.currUser.empId,
                token : util.Models.currUser.token,
            },
            function(statusCode, response) {
                var responseData = HttpUtil.beforeCallback(response, statusCode, callback);
                callback.call(null, responseData);
            }
        );
    };

    HttpUtil.uploadPincode = function(keyNum, pinCode, callback){
        print("HttpUtil-uploadPincode, start send data, keyNum is " + keyNum + ", pinCode is " + pinCode);
        http.post(util.Constant.NET_DOMAIN + util.Constant.NET_SET_KEY_PINCODE,
            {
                //params
                empId : util.Models.currUser.empId,
                token : util.Models.currUser.token,
                keyNum : keyNum,
                pinCode : pinCode,
            },
            function(statusCode, response) {
                var responseData = HttpUtil.beforeCallback(response, statusCode, callback);
                callback.call(null, responseData);
            }
        );
    };

    HttpUtil.confirmKeyPassToReceiver = function(keyNum, callback){
        print("HttpUtil-confirmKeyPassToReceiver, start send data");
        http.post(util.Constant.NET_DOMAIN + util.Constant.NET_CONFIRM_PASS,
            {
                empId : util.Models.currUser.empId,
                token : util.Models.currUser.token,
                keyNum : keyNum,
            },
            function(statusCode, response) {
                var responseData = HttpUtil.beforeCallback(response, statusCode, callback);
                callback.call(null, responseData);
            }
        );
    };

    HttpUtil.cancelKeyPassToReceiver = function(keyNum, callback){
        print("HttpUtil-cancelKeyPassToReceiver, start send data");
        http.post(util.Constant.NET_DOMAIN + util.Constant.NET_CALCE_PASS,
            {
                empId : util.Models.currUser.empId,
                token : util.Models.currUser.token,
                keyNum : keyNum,
            },
            function(statusCode, response) {
                var responseData = HttpUtil.beforeCallback(response, statusCode, callback);
                callback.call(null, responseData);
            }
        );
    };

    HttpUtil.confirmPinCode = function (pincode, callback) {
        print("HttpUtil-comfirmPinCode, start send data");
        http.post(util.Constant.NET_DOMAIN + util.Constant.NET_CONFIRM_PINCODE,
            {
                //params
                empId : util.Models.currUser.empId,
                token : util.Models.currUser.token,
                pinCode : pincode,
            },
            function(statusCode, response) {
                var responseData = HttpUtil.beforeCallback(response, statusCode, callback);
                callback.call(null, responseData);
            }
        );
    };

    HttpUtil.borrowKeyFromShop = function (delCode, callback) {
        print("HttpUtil-borrowKeyFromShop, start send data");
        http.post(util.Constant.NET_DOMAIN + util.Constant.NET_BORROW_KEY_FROM_SHOP,
            {
                //params
                empId : util.Models.currUser.empId,
                token : util.Models.currUser.token,
                delCode : delCode,
            },
            function(statusCode, response) {
                var responseData = HttpUtil.beforeCallback(response, statusCode, callback);
                callback.call(null, responseData);
            }
        );
    };

    HttpUtil.returnKey = function (keyNum, delCode, callback) {
        print("HttpUtil-returnKey, start send data");
        http.post(util.Constant.NET_DOMAIN + util.Constant.NET_RETURN_KEY,
            {
                //params
                empId : util.Models.currUser.empId,
                token : util.Models.currUser.token,
                keyNum : keyNum,
                delCode : delCode,
            },
            function(statusCode, response) {
                var responseData = HttpUtil.beforeCallback(response, statusCode, callback);
                  print("hucx add");
                  print(responseData);
                  print("hucx add");
                callback.call(null, responseData);
            }
        );
    };

    HttpUtil.keyPassCheck = function(type, callback){
        print("HttpUtil-keyPassCheck, start send data");
        http.post(util.Constant.NET_DOMAIN + util.Constant.NET_CHECK_PASS,
            {
                //params
                empId : util.Models.currUser.empId,
                token : util.Models.currUser.token,
                type : type,
            },
            function(statusCode, response) {
                var responseData = HttpUtil.beforeCallback(response, statusCode, callback);
                callback.call(null, responseData);
            }
        );
    };

    ///////////////////////////////////////////////
    ///   event 
    ///////////////////////////////////////////////

    HttpUtil.getEventList = function (type, timeStamp, pageSize, callback) {
        //print("HttpUtil-getEventListNew, start send data, " + timeStamp + type);
        http.post(util.Constant.NET_DOMAIN + util.Constant.NET_GET_EVENT_LIST,
            {
                //params
                empId : util.Models.currUser.empId,
                token : util.Models.currUser.token,
                timeStamp : timeStamp,
                type : type,
                pageSize : pageSize,
            },
            function(statusCode, response) {
                var responseData = HttpUtil.beforeCallback(response, statusCode, callback);
                callback.call(null, responseData);
            }
        );
    };

    HttpUtil.setEventRead = function (msgId, callback) {
        print("HttpUtil-setEventRead, start send data");
        http.post(util.Constant.NET_DOMAIN + util.Constant.NET_SET_EVENT_READ,
            {
                //params
                empId : util.Models.currUser.empId,
                token : util.Models.currUser.token,
                msgId : msgId,
            },
            function(statusCode, response) {
                var responseData = HttpUtil.beforeCallback(response, statusCode, callback);
                callback.call(null, responseData);
            }
        );
    };

    this.HttpUtil = HttpUtil;
}.call(this));