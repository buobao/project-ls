(function(){
	var JSONUtil = function(){};

	JSONUtil.prototype.toObj = function(clazz, jsonData){
		var instance = new clazz();
		for(var p in jsonData){
			if( instance.hasOwnProperty(p) ){
				instance[p] = jsonData[p];
			}
		}
		return instance;
	};

	JSONUtil.prototype.toArray = function(clazz, jsonData){
		var instanceArray = [];
		for(var i=0;i<jsonData.length;i++){
			var dataObj = jsonData[i];
			var instance = new clazz();
			for(var p in dataObj){
				if( instance.hasOwnProperty(p) ){
					instance[p] = dataObj[p];
				}
			}
			instanceArray.push(instance);
		}
		return instanceArray;
	}

	this.JSONUtil = JSONUtil;
	
}.call(this));