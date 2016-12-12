package cn.magicstudio.mblog.base.framework.model;

import cn.magicstudio.mblog.base.framework.utils.CodingRuleUtil;

import com.yougou.logistics.base.common.model.CodingRule;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;


public class CodingRuleHolder {
	private CodingRule codingRule;
	private boolean isAllowBreakNo;
	private int currentSerialNo;
	private int maxSerialNo;
	private String formatedDate;
	private String prefix;
	private long cacheKeepTime = 0L;

	private int serialLength = 6;

	private LinkedList<String> recycledSerialNoList = new LinkedList();

	private long initLocalTime;

	private AtomicBoolean isUseRecycle = new AtomicBoolean(false);

	public AtomicBoolean getIsUseRecycle() {
		return this.isUseRecycle;
	}

	public CodingRule getCodingRule() {
		return this.codingRule;
	}

	public void setCodingRule(CodingRule codingRule) {
		this.codingRule = codingRule;
	}

	public CodingRuleHolder(CodingRule codingRule, long initLocalTime) {
		this.codingRule = codingRule;
		this.initLocalTime = initLocalTime;
		init();
	}

	private void init() {
		this.currentSerialNo = this.codingRule.getCurrentSerialNo().intValue();
		if (this.codingRule.getAllowBreakNo().booleanValue()) {
			this.maxSerialNo = (this.currentSerialNo + 10 - 1);
		} else {
			this.maxSerialNo = this.currentSerialNo;
		}
		this.formatedDate = CodingRuleUtil.getFormatedDate(this.codingRule
				.getResetMode().intValue(), this.codingRule.getDbTime());
		this.prefix = (this.codingRule.getPrefix() == null ? ""
				: this.codingRule.getPrefix());
		initCacheKeepTime(this.codingRule.getResetMode().intValue());
		this.serialLength = this.codingRule.getSerialNoLength().intValue();
	}

	private void initCacheKeepTime(int resetMode) {
		Date dbTime = this.codingRule.getDbTime();

		this.cacheKeepTime = Long.MAX_VALUE;
		Calendar cal = Calendar.getInstance();
		cal.setTime(dbTime);
		switch (resetMode) {
		case 1:
			this.cacheKeepTime = 1L;
			cal.set(cal.get(1), cal.get(2), cal.get(5), 24, 0, 0);
			break;

		case 2:
			this.cacheKeepTime = 1L;
			cal.set(cal.get(1), cal.get(2), cal.getActualMaximum(5), 24, 0, 0);
			break;

		case 3:
			this.cacheKeepTime = 1L;
			cal.set(cal.get(1), cal.get(2), cal.getActualMaximum(5), 24, 0, 0);
			cal.set(2, cal.getActualMaximum(2));
			cal.set(5, cal.getActualMaximum(5));
			break;
		default:
			return;
		}

		this.cacheKeepTime = (cal.getTimeInMillis() - dbTime.getTime());
	}

	public String nextSerialNo() {
		if (System.currentTimeMillis() - this.initLocalTime >= this.cacheKeepTime) {
			return "need reset";
		}

		String no = pop();
		if (!no.equals("")) {
			this.isUseRecycle.getAndSet(true);
			return no;
		}

		int nextNo = this.currentSerialNo++;

		if (nextNo > this.maxSerialNo) {
			return "no cached no";
		}

		return format(this.serialLength, nextNo);
	}

	private static String format(int serialLength, int intValue) {
		String str = String.format("%0" + serialLength + "d",
				new Object[] { Integer.valueOf(intValue) });
		return str;
	}

	public boolean isAllowBreakNo() {
		return this.isAllowBreakNo;
	}

	public void recycleSerialNo(String serialNo) {
		if (isLegalSerialNo(serialNo)) {
			push(serialNo);
		}
	}

	private void push(String serialNo) {
		this.recycledSerialNoList.addLast(serialNo);
	}

	private String pop() {
		if (this.recycledSerialNoList.size() > 0) {
			String no = (String) this.recycledSerialNoList.pollFirst();
			return no == null ? "" : no;
		}
		return "";
	}

	private boolean isLegalSerialNo(String serialNo) {
		if ((serialNo == null) || (serialNo.equals(""))) {
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		Date dbTime = new Date();

		long cacheKeepTime = Long.MAX_VALUE;
		Calendar cal = Calendar.getInstance();
		cal.setTime(dbTime);

		int resetMode = 0;
		switch (resetMode) {
		case 1:
			cacheKeepTime = 1L;
			cal.set(cal.get(1), cal.get(2), cal.get(5), 24, 0, 0);
			break;

		case 2:
			cacheKeepTime = 1L;
			cal.set(cal.get(1), cal.get(2), cal.getActualMaximum(5), 24, 0, 0);
			break;

		case 3:
			cacheKeepTime = 1L;
			cal.set(cal.get(1), cal.get(2), cal.getActualMaximum(5), 24, 0, 0);
			cal.set(2, cal.getActualMaximum(2));
			cal.set(5, cal.getActualMaximum(5));
			break;
		}

		cacheKeepTime = cal.getTimeInMillis() - dbTime.getTime();
		System.out.println("cacheKeepTime:" + cacheKeepTime);
	}

	public String getPrefix() {
		return this.prefix;
	}

	public String getFormatedDate() {
		return this.formatedDate;
	}
}
