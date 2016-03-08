(function(){
	var AppUtil = {};
	AppUtil.name = "AppUtil";

	AppUtil.checkEmail = function(email) {
        var result = { ok: false, message: "" };
        if (email.length === 0) {
            result.message = "用户名不能为空！";
            return result;
        }

        var re = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i;
        var ok = re.test(email);
        if (!ok) {
            result.message = "用户名不是有效的邮箱地址！";
            return result;
        }

        result.ok = true;
        return result;
    };

    AppUtil.checkPassword = function(password) {
        var result = { ok: false, message: "" };
        if (password.length === 0) {
            result.message = "密码不能为空！";
            return result;
        }

        if (password.length < 6) {
            result.message = "密码长度不能小于6位！";
            return result;
        }

        result.ok = true;
        return result;
    };

    AppUtil.checkIsArray = function(obj) {
        return Object.prototype.toString.call(obj) === "[object Array]";
    };

    AppUtil.checkValueIsNull = function(obj, property) {
        if( obj.hasOwnProperty(property) == false )
            return true;
        if(obj[property] == null || obj[property] == ""){
            return true;
        }
        return false;
    }

    AppUtil.urlencode = function(str) {
        str = (str + '')
        .toString();

      // Tilde should be allowed unescaped in future versions of PHP (as reflected below), but if you want to reflect current
      // PHP behavior, you would need to add ".replace(/~/g, '%7E');" to the following.
      return encodeURIComponent(str)
        .replace(/!/g, '%21')
        .replace(/'/g, '%27')
        .replace(/\(/g, '%28')
        .
      replace(/\)/g, '%29')
        .replace(/\*/g, '%2A')
        .replace(/%20/g, '+');
    }
    
    this.AppUtil = AppUtil;
}.call(this));