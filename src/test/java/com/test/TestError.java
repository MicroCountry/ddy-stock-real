package com.test;

import com.ddy.stock.real.domain.BusError;
import com.ddy.stock.real.domain.TokenError;
import com.ddy.stock.real.util.JacksonUtils;

public class TestError {
	public static void main(String[] args) {
		TokenError error = JacksonUtils.json2Object("{\"error_no\":-1,\"error_info\":\"failed\",\"error_code\":1,\"error_extinfo\":\"sort_field_name: 必须输入\"}", TokenError.class);
		System.out.println(error==null);
		BusError buserror = JacksonUtils.json2Object("{\"error_no\":-1,\"error_info\":\"failed\",\"error_code\":1,\"error_extinfo\":\"sort_field_name: 必须输入\"}", BusError.class);
		System.out.println(buserror==null);
	}
}
