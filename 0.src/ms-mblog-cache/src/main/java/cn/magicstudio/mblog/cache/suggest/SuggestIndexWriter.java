package cn.magicstudio.mblog.cache.suggest;

import cn.magicstudio.mblog.cache.DataEvents;
import cn.magicstudio.mblog.cache.EventArgs;
import cn.wonhigh.retail.backend.model.EntityStatus;
import com.yougou.logistics.base.common.event.IEvent;
import com.yougou.logistics.base.common.event.IEventHandler;
import com.yougou.logistics.base.common.event.disruptor.BelleEventService;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;

public abstract class SuggestIndexWriter implements IEventHandler<IEvent>,
		IndexWriter {
	private DataEvents event = DataEvents.DataChanged;
	protected int status = 0;
	protected Date startDate;
	protected Date endDate;
	protected AtomicInteger itemCount = new AtomicInteger();

	public int getStatus() {
		return this.status;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public int getIndex() {
		return this.itemCount.get();
	}

	public void reset() {
		this.status = 0;
	}

	@Resource
	private BelleEventService belleEventService;

	protected void init() {
		if (this.belleEventService != null) {
			this.belleEventService.register(this.event.getClass(), this);
		}
	}

	public abstract String name();

	protected boolean supportType(String type) {
		return false;
	}

	public void handle(IEvent event) {
		EventArgs args = (EventArgs) event.getData();
		String type = args.getType();
		EntityStatus status = args.getStatus();
		Object value = args.getData();
		if ((type.equals(name())) || (supportType(type)))
			syncIndex(value, status);
	}

	public void start() {
		this.startDate = new Date();
		this.endDate = null;
		this.status = 0;
		initialize();
	}

	protected void finished() {
		this.endDate = new Date();
		this.status = 2;
	}

	public abstract void initialize();

	public abstract void syncIndex(Object paramObject,
			EntityStatus paramEntityStatus);
}
