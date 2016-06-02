package com.vocinno.centanet.apputils.utils.imageutils.selector.tools;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//没价值了。。
public class SortList<E> {
	@SuppressWarnings("unchecked")
	public void Sort(List<E> list, final String method, final String sort) {
		Collections.sort(list, new Comparator() {
			@Override
			public int compare(Object a, Object b) {
				int ret = 0;
				try {
					Method m1 = ((E) a).getClass().getMethod(method, (Class<?>)null);
					Method m2 = ((E) b).getClass().getMethod(method, (Class<?>)null);
					if (sort != null && "desc".equals(sort))// 倒序
						ret = m2.invoke(((E) b), (Class<?>)null).toString().compareTo(m1.invoke(((E) a), (Class<?>)null).toString());
					else
						// 正序
						ret = m1.invoke(((E) a), (Class<?>)null).toString().compareTo(m2.invoke(((E) b), (Class<?>)null).toString());
				} catch (NoSuchMethodException ne) {
					System.out.println(ne);
				} catch (IllegalAccessException ie) {
					System.out.println(ie);
				} catch (InvocationTargetException it) {
					System.out.println(it);
				}
				return ret;
			}
		});
	}

	

}
