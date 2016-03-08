(function() {
	print("models.js init");

	this.Models = this;

    //User
    this.User = function() {
		this.empId = "0";
		this.token = "";
	};
	this.User.prototype = new unify.Model();
	this.User.prototype.constructor = this.User;

	//User cache
	this.currUser = new this.User();

	//Employee
	this.Employee = function(){
		this.empId = "";
		this.picUrl = "";
		this.name = "";
	};
	this.Employee.prototype = new unify.Model();
	this.Employee.prototype.constructor = this.Employee;

	//Hosue 
	this.House = function(){
		this.delCode = "";
		this.img = "123"; //hucx update
		this.addr = "";
		this.frame = "";
		this.square = "";
		this.floor = "";
		this.orient = "";
		this.price = "";
		this.unitprice = "";
		this.tag = "";
		this.activeTime = "";
		this.lat = 0;
		this.att = 0;
		this.track = [];
		this.exclude = [];
		this.isPublic = false;

		//key
		this.keyNum = 0;
		this.keyStatus = ""; //hucx update
		this.keyUser = "";
		this.keyUserPhone = ""; //hucx update
		this.keyCount = 0;		
	};
	this.House.prototype = new unify.Model();
	this.House.prototype.constructor = this.House;

	//Customer
	this.Customer = function(){
		this.custCode = "";
		this.name = "";
		this.phone = "";
		this.qq = "";
		this.wechat = "";
		this.requests = {};
		this.tracks = [];
		this.isPay = false;
		this.paymentType = "";
		this.isPublic = false;

		this.area = "";
		this.acreage = "";
		this.price = "";
		this.other = "";
		this.frame = "";
		this.relativeDate = "";
	};
	this.Customer.prototype = new unify.Model();
	this.Customer.prototype.constructor = this.Customer;

	//Customer list
	this.CustomerList = function(){
		this.listType = "";
		this.page = 0;
		this.total = 20;
		this.records = 0;
		this.rows = [];
	};
	this.CustomerList.prototype = new unify.Model();
	this.CustomerList.prototype.constructor = this.CustomerList;

	//Hosue list
	this.HouseList = function(){
		this.listType = "";
		this.page = 0;
		this.total = 20;
		this.records = 0;
		this.rows = [];
	};
	this.HouseList.prototype = new unify.Model();
	this.HouseList.prototype.constructor = this.HouseList;

	//Estate
	this.Estate = function(){
		this.id = 0;
		this.name = "";
		this.type = "";
	};
	this.Estate.prototype = new unify.Model();
	this.Estate.prototype.constructor = this.Estate;

	//House's contactor
	this.Contactor = function(){
		this.partiInfoId = 0;
		this.contactType = "";
		this.name = "";
		this.tel = "";
		this.frequency = 0;
	};
	this.Contactor.prototype = new unify.Model();
	this.Contactor.prototype.constructor = this.Contactor;

	//House Map
	this.MapPoint = function(){
		this.att = 0;
		this.lat = 0;
		this.count = 0;
		this.entityId = 0;
		this.name = "";
	};
	this.MapPoint.prototype = new unify.Model();
	this.MapPoint.prototype.constructor = this.MapPoint;
	//MapPoint List
	this.MapPointList = function(){
		this.type = "";
		this.mapPoint = [];
	};
	this.MapPointList.prototype = new unify.Model();
	this.MapPointList.prototype.constructor = this.MapPointList;

	//Key Pass Check
	this.KeyPassInfo = function(){
		this.type = "";
		this.keyNum = "";
		this.receiverInfo = {};
		this.result = 0;
		this.expireTime = 0;
		this.empId = 0;
	};
	this.KeyPassInfo.prototype = new unify.Model();
	this.KeyPassInfo.prototype.constructor = this.KeyPassInfo;

	//KeyHosueInfo
	this.KeyInfo = function(){
		this.keyNum = "";
		this.img = "";
		this.addr = "";
		this.delCode = "";
		this.borrowTime = "";
		this.store = "";
		this.isWaitingConfirm = false;
	}
	this.KeyInfo.prototype = new unify.Model();
	this.KeyInfo.prototype.constructor = this.KeyInfo;

	//House trach info
	this.HouseTrack = function(){
		this.content = "";
		this.trackTime = "";
		this.tractCat = "";
	};
	this.HouseTrack.prototype = new unify.Model();
	this.HouseTrack.prototype.constructor = this.HouseTrack;

	//Message
	this.Message = function(){
		this.msgId = "";
		this.msgType = "";
		this.msgContent = "";
		this.msgTime = "";
	};
	this.Message.prototype = new unify.Model();
	this.Message.prototype.constructor = this.Message;

	//城区
	this.District = function() {
		this.districtCode = 0;
		this.districtName = "";
	}
	this.District.prototype = new unify.Model();
	this.District.prototype.constructor = this.District;

	//area
	this.Area = function() {
		this.areaCode = 0;
		this.areaName = "";
	}
	this.Area.prototype = new unify.Model();
	this.Area.prototype.constructor = this.Area;

}.call(this));

