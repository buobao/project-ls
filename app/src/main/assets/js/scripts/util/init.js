(function(){
	global.util = {};

	try{
		var util = global.util;
		util.Models = require("model/models").Models;
		util.Constant = require("util/Constant").Constant;
		util.AppUtil = require("util/AppUtil").AppUtil;
		util.HttpUtil = require("util/HttpUtil").HttpUtil;
		util.JSONUtil = require("util/JSONUtil").JSONUtil;
		util.HttpFileUtil = require("util/HttpFileUtil").HttpFileUtil;
	}catch(e){
		print(e.stack);
	}
	


}.call(this));