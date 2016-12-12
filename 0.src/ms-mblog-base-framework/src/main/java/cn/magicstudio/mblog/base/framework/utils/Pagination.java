package cn.magicstudio.mblog.base.framework.utils;

import java.io.Serializable;
import java.util.List;
import com.yougou.logistics.base.common.utils.Pageable;
import com.yougou.logistics.base.common.utils.SimplePage;

public class Pagination<E> extends SimplePage implements Serializable, Pageable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<E> list;

	public Pagination() {
	}

	public Pagination(SimplePage simplePage, List<E> list) {
		super(simplePage.getPageNo(), simplePage.getPageSize(), simplePage
				.getTotalCount());
		this.list = list;
	}

	public Pagination(int pageNo, int pageSize, int totalCount) {
		super(pageNo, pageSize, totalCount);
	}

	public Pagination(int pageNo, int pageSize, int totalCount, List<E> list) {
		super(pageNo, pageSize, totalCount);
		this.list = list;
	}

	public int getFirstResult() {
		return (this.pageNo - 1) * this.pageSize;
	}

	public List<E> getList() {
		return this.list;
	}

	public void setList(List<E> list) {
		this.list = list;
	}
}
