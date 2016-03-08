package com.vocinno.centanet.apputils.cst;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CST_Wheel_Data {
	public static enum WheelType {
		squareStart, squareEnd, priceChuzuStart, priceChuzuEnd, priceChushouStart, priceChushouEnd, huXing, louXing, biaoQian, area
	};

	private static ArrayList<String> mListStrs = new ArrayList<String>();

	public static ArrayList<String> getListDatas(WheelType type) {
		mListStrs.clear();
		switch (type) {
		case squareStart:
			mListStrs.add("0平米");
			mListStrs.add("50平米");
			mListStrs.add("70平米");
			mListStrs.add("90平米");
			mListStrs.add("110平米");
			mListStrs.add("130平米");
			mListStrs.add("150平米");
			mListStrs.add("200平米");
			mListStrs.add("300平米");
			mListStrs.add("500平米");
			break;
		case squareEnd:
			mListStrs.add("50平米");
			mListStrs.add("70平米");
			mListStrs.add("90平米");
			mListStrs.add("110平米");
			mListStrs.add("130平米");
			mListStrs.add("150平米");
			mListStrs.add("200平米");
			mListStrs.add("300平米");
			mListStrs.add("500平米");
			mListStrs.add("不限");
			break;
		case priceChuzuStart:
			mListStrs.add("0元");
			mListStrs.add("800元");
			mListStrs.add("1500元");
			mListStrs.add("2000元");
			mListStrs.add("3000元");
			mListStrs.add("5000元");
			mListStrs.add("6500元");
			mListStrs.add("8000元");
			mListStrs.add("10000元");
			mListStrs.add("15000元");
			break;
		case priceChuzuEnd:
			mListStrs.add("800元");
			mListStrs.add("1500元");
			mListStrs.add("2000元");
			mListStrs.add("3000元");
			mListStrs.add("5000元");
			mListStrs.add("6500元");
			mListStrs.add("8000元");
			mListStrs.add("10000元");
			mListStrs.add("15000元");
			mListStrs.add("不限");
			break;
		case priceChushouStart:
			mListStrs.add("0万");
			mListStrs.add("100万");
			mListStrs.add("150万");
			mListStrs.add("200万");
			mListStrs.add("250万");
			mListStrs.add("300万");
			mListStrs.add("400万");
			mListStrs.add("500万");
			mListStrs.add("700万");
			mListStrs.add("1000万");
			break;
		case priceChushouEnd:
			mListStrs.add("100万");
			mListStrs.add("150万");
			mListStrs.add("200万");
			mListStrs.add("250万");
			mListStrs.add("300万");
			mListStrs.add("400万");
			mListStrs.add("500万");
			mListStrs.add("700万");
			mListStrs.add("1000万");
			mListStrs.add("不限");
			break;
		case huXing:
			mListStrs.add("不限");
			mListStrs.add("1");
			mListStrs.add("2");
			mListStrs.add("3");
			mListStrs.add("4");
			break;
		case louXing:
			mListStrs.add("全部类型");
			mListStrs.add("多类型");
			mListStrs.add("多层");
			mListStrs.add("高层");
			mListStrs.add("小高层");
			mListStrs.add("多层复式");
			mListStrs.add("高层复式");
			mListStrs.add("多层跃式");
			mListStrs.add("高层跃式");
			mListStrs.add("裙楼");
			mListStrs.add("别墅");
			mListStrs.add("独栋高层");
			mListStrs.add("独栋小高层");
			mListStrs.add("独栋多层");
			break;
		case biaoQian:
			mListStrs.add("满五");
			mListStrs.add("满二");
			mListStrs.add("唯一");
			mListStrs.add("地铁房");
			mListStrs.add("学区房");
			mListStrs.add("钥匙");
			mListStrs.add("签赔");
			mListStrs.add("租售");
			break;
		case area:
			mListStrs.add("黄浦区");
			mListStrs.add("卢湾区");
			mListStrs.add("徐汇区");
			mListStrs.add("长宁区");
			mListStrs.add("静安区");
			mListStrs.add("普陀区");
			mListStrs.add("闸北区");
			mListStrs.add("虹口区");
			mListStrs.add("闵行区");
			mListStrs.add("宝山区");
			mListStrs.add("金山区");
			mListStrs.add("松江区");
			mListStrs.add("青浦区");
			mListStrs.add("南汇区");
			mListStrs.add("奉贤区");
			mListStrs.add("崇明县");
			mListStrs.add("嘉定区");
			mListStrs.add("杨浦区");
			break;
		default:
			break;
		}
		return mListStrs;
	}

	public static String getCodeForArea(String area) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("黄浦区", "310101");
		map.put("卢湾区", "310103");
		map.put("徐汇区", "310104");
		map.put("长宁区", "310105");
		map.put("静安区", "310106");
		map.put("普陀区", "310107");
		map.put("闸北区", "310108");
		map.put("虹口区", "310109");
		map.put("杨浦区", "310110");
		map.put("闵行区", "310112");
		map.put("宝山区", "310113");
		map.put("金山区", "310116");
		map.put("嘉定区", "310114");
		map.put("浦东新区", "310115");
		map.put("松江区", "310117");
		map.put("青浦区", "310118");
		map.put("南汇区", "310119");
		map.put("奉贤区", "310120");
		map.put("崇明县", "310230");
		return map.get(area);
	}

	public static String getCodeForLouXing(String louxing) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("全部类型", "");
		map.put("多类型", "10013001");
		map.put("多层", "10013002");
		map.put("高层", "10013003");
		map.put("小高层", "10013004");
		map.put("多层复式", "10013005");
		map.put("高层复式", "10013006");
		map.put("多层跃式", "10013007");
		map.put("高层跃式", "10013008");
		map.put("裙楼", "10013009");
		map.put("别墅", "10013010");
		map.put("独栋高层", "10013011");
		map.put("独栋小高层", "10013012");
		map.put("独栋多层", "10013013");
		return map.get(louxing);
	}

}
