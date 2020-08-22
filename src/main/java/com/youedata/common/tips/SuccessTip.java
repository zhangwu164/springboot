package com.youedata.common.tips;

import com.youedata.common.exception.ExceptionEnum;

public class SuccessTip extends Tip {
	
	public SuccessTip(){
		super.code = 200;
		super.message = "操作成功";
	}

	public static Tip<Object> success() {
		Tip<Object> tip = new Tip<Object>();
		tip.setCode(ExceptionEnum.SUCCESS.getCode());
		tip.setMessage(ExceptionEnum.SUCCESS.getMessage());
		return tip;
	}

	public static <T> Tip<T> success(T result) {
		Tip<T> tip = new Tip<>();
		tip.setCode(ExceptionEnum.SUCCESS.getCode());
		tip.setMessage(ExceptionEnum.SUCCESS.getMessage());
		tip.result = result;
		return tip;
	}

	public static <T> Tip<T> success(int code, String message, T result) {
		Tip<T> tip = new Tip<>();
		tip.setCode(code);
		tip.setMessage(message);
		tip.result = result;
		return tip;
	}
}
