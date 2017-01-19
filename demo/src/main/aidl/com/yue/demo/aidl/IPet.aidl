package com.yue.demo.aidl;

import com.yue.demo.aidl.Pet;
import com.yue.demo.aidl.Person;

interface IPet {
	// 定义一个Person对象作为传入参数
	List<Pet> getPets(in Person owner);
}
