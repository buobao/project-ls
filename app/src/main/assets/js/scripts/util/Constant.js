(function(){
 
 
	//net define
	this.NET_DOMAIN = "http://61.152.255.241:8082/sales-web/";	

	this.NET_LOGIN_URL = "mobile/login";							//login
	this.NET_GET_SERVER_TIME = "mobile/serverTime/";	

	this.NET_GET_HOUSE_RENT_LIST = "mobile/hou/houList/";		//rent house list
	this.NET_GET_HOUSE_PUBLIC_LIST = "mobile/hou/houlist";
	this.NET_GET_HOUSE_MAP_LIST = "mobile/hou/houInMap/";		//find house in map	
	this.NET_GET_HOUSE_DETAIL = "mobile/hou/houInfo/";			//get house info
	this.NET_CLAIM_HOUSE = "mobile/hou/claim";
	this.NET_GET_HOUSE_CONTACT_LIST = "mobile/hou/contactList/";	//get house contact's list
	this.NET_GET_HOUSE_TRACK_LIST = "mobile/hou/track/show";		//get track list by house delcode
	this.NET_SET_HOUSE_TRACK = "mobile/hou/track/add";			//add track info 
	this.NET_GET_SHARE_LINK = "mobile/hou/houShow";
	this.NET_SEARCH_ESTATE_NAME = "mobile/hou/estateName";
	this.NET_HOU_CUSTOMER_TRACK = "mobile/cust/look/add";

	this.NET_ADD_IMGS = "mobile/hou/explore/add";
	
	this.NET_GET_CUSTOMER_LIST = "mobile/cust/custList";
	this.NET_GET_CUSTOMER_PUBLIC = "mobile/cust/custlist";
	this.NET_ADD_CUSTOMER = "mobile/cust/add";
	this.NET_GET_CUSTOMER_DETAIL = "mobile/cust/custInfo";
	this.NET_ADD_CUSTOMER_TRACK = "mobile/cust/track/add";
	this.NET_CLAIM_CUSTOMER = "mobile/cust/claim";
	this.NET_AREA_LIST = "mobile/hou/areas";
	this.NET_CHECK_PHONENO = "/mobile/cust/checkMpNo";

	this.NET_CONFIRM_PINCODE = "mobile/hou/key/receive";		//receiver input pincode
	this.NET_SET_KEY_PINCODE = "mobile/hou/key/pass";			//get pincode 
	this.NET_GET_KEY_LIST_MY = "mobile/hou/key/mtn";
	this.NET_BORROW_KEY_FROM_SHOP = "mobile/hou/key/borrow";
	this.NET_RETURN_KEY = "mobile/hou/key/return";
	this.NET_CONFIRM_PASS = "mobile/hou/keyPass/confirm";		// pass to receiver
	this.NET_CALCE_PASS = "mobile/hou/keyPass/cancel"			// cancel pass
	this.NET_CHECK_PASS = "mobile/hou/keyPass/check";			// check pass result

	this.NET_GET_EVENT_LIST = "mobile/msg/list";					//get evnet list
	this.NET_GET_EVENT_NEW = "mobile/msg";
	this.NET_SET_EVENT_READ = "mobile/msg/read";

	//notify defined
	//notify to native
	this.NOTIFY_NATIVE_LOGIN_RESULT = "natify_native_login_result";

	this.NOTIFY_NATIVE_SEARCH_ITEM_RESULT = "notify_native_search_estate_result";				// get searchbox list when user input searchname
	this.NOTIFY_NATIVE_HOU_LIST_RESULT = "notify_native_get_houstlist_result";					// get house list when user enter houseListView
	this.NOTIFY_NATIVE_HOU_LIST_INMAP_RESULT = "notify_native_get_hou_InMap_result";			// get estate list when user enter map
	this.NOTIFY_NATIVE_HOU_LIST_SEARCH_RESULT = "notify_native_get_hou_search_result";			// get search result when user choose a search item
	this.NOTIFY_NATIVE_HOU_LIST_CLICK_MAP_RESULT = "notify_native_get_hou_click_map_result";	// get house list when user click item in the map
	this.NOTIFY_NATIVE_HOU_DETAIL_RESULT = "notify_native_house_detail_result";					// get hosue detai when user click cell in list
	this.NOTIFY_NATIVE_CONTACT_LIST_RESULT = "notify_native_contact_list_result";				// get contact list when user click contactBtn
	this.NOTIFY_NATIVE_TRACK_LIST_RESULT = "notify_native_track_list_result";					// get track list, just ignore it
	this.NOTIFY_NATIVE_CLAIM_HOUSE_RESULT = "notify_native_claim_house_result";					// claim house when user click the claimBtn in HouseDetailView
	this.NOTIFY_NATIVE_GET_SHARE_LINK_RESULT = "notify_native_share_link_result";				// get share link when user wanna share the houseinfo
	this.NOTIFY_NATIVE_ADD_HOU_CUST_TRACK_RESULT = "notify_native_hou_cust_track_result";		// add houCust track
	this.NOTIFY_NATIVE_ADD_HOU_TRACK_RESULT = "notify_native_hou_add_track_result";

	this.NOTIFY_NATIVE_GET_CUSTOMER_LIST_RESULT = "notify_native_customerlist_result";			// get customer list when user enter the CustomerListView
	this.NOTIFY_NATIVE_GET_CUSTOMER_DETAIL_RESULT = "notify_native_customerinfo_result";		// get customer info when user click the cell in the customer list
	this.NOTIFY_NATIVE_ADD_CUSTOMER_RESULT = "notify_native_add_customer_result";				// add customer
	this.NOTIFY_NATIVE_CLAIM_CUSTOMER_RESULT = "notify_native_claim_customer_result";			// claim customer when user click the cliamBtn in CustomerDetailView
	this.NOTIFY_NATIVE_GET_AREA_RESULT = "notify_native_get_area_result";
	this.NOTIFY_NATIVE_CHECK_PNONENO = "notify_native_check_pnoneno";
 
	this.NOTIFY_NATIVE_CUST_TRACK_RESULT = "notify_native_cust_track_result";
 
	this.NOTIFY_NATIVE_CONFIRM_PINCODE = "notify_native_confirm_pincode_result";				// confirm the pincode when receiver input the code in the PinCodeView
	this.NOTIFY_NATIVE_PASS_CHECK = "notify_native_pass_check_result";							// check pass result, call by owner
	this.NOTIFY_NATIVE_PASS_CHECK_RECEIVER = "notify_native_pass_check_by_receiver_result";		// check pass result, call by receiver 
	this.NOTIFY_NATIVE_KEY_LIST_RESULT = "notify_native_key_list_result";						// get my key list when user enter KeyListView
	this.NOTIFY_NATIVE_CONFIRM_KEY_TO_RECEIVER_RESULT = "notify_native_confirm_key_2_receiver";	// comfirm pass key to receiver, click by key owner
	this.NOTIFY_NATIVE_CALCEL_KEY_TO_RECEIVER = "notify_native_cancel_key_2_receiver";			// cancel key pass, click by key owner
	this.NOTIFY_NATIVE_BORROW_KEY_FROM_SHOP_RESULT = "notify_native_borrow_key_from_shop_result";// borrow key from shop
	this.NOTIFY_NATIVE_RETURN_KEY_RESULT = "notify_native_return_key_result";					// return key to shop
	this.NOTIFY_NATIVE_SET_PINCODE_RESULT = "notify_native_set_pincode_result";					// set pincode to this key via server, triggered when user slide the key cell
	this.NOTIFY_NATIVE_GET_PINCODE_RESULT = "notify_native_get_pincode_result";

	this.NOTIFY_NATIVE_MESSAGE_READ_RESULT = "notify_native_message_read_result";				// set the message read, call when user click the cell in the message list
	this.NOTIFY_NATIVE_MESSAGE_LIST_RESULT = "notify_native_message_list_result";				// get message list

	this.NOTIFY_NATIVE_UPLOAD_IMGS_RESULT = "notify_native_upload_imgs_result";

	// message type
	this.MESSAGE_TYPE_LOSS = 10064001;
	this.MESSAGE_TYPE_DEAL = 10064002;
	this.MESSAGE_TYPE_NEW = 10064003;

	// pic type
	this.PIC_TYPE_HOUSE = 100531001;
	this.PIC_TYPE_ROOM = 100531002;
	this.PIC_TYPE_LIVINGROOMT = 100531003;
	this.PIC_TYPE_TOILET = 100531005;
	this.PIC_TYPE_BALCONY = 100531006;
	this.PIC_TYPE_OTHER = 100531008;
 

	// delta between server time and local time
	this.DELTA_SERVER_TIME = 0;

	this.Constant = this;
}.call(this));
