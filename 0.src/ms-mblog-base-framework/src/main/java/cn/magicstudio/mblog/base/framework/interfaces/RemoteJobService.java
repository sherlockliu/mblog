package cn.magicstudio.mblog.base.framework.interfaces;


import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;

import cn.magicstudio.mblog.base.framework.enums.JobBizStatusEnum;

public abstract interface RemoteJobService
{
  public abstract void initializeJob(String paramString1, String paramString2);
  
  @ManagedOperation(description="")
  @ManagedOperationParameters({@org.springframework.jmx.export.annotation.ManagedOperationParameter(name="triggerName", description="quartz trigger name"), @org.springframework.jmx.export.annotation.ManagedOperationParameter(name="groupName", description="quartz trigger group name")})
  public abstract void executeJob(String paramString1, String paramString2);
  
  @ManagedOperation(description="")
  @ManagedOperationParameters({@org.springframework.jmx.export.annotation.ManagedOperationParameter(name="triggerName", description="quartz trigger name"), @org.springframework.jmx.export.annotation.ManagedOperationParameter(name="groupName", description="quartz trigger group name")})
  public abstract void pauseJob(String paramString1, String paramString2);
  
  @ManagedOperation(description="")
  @ManagedOperationParameters({@org.springframework.jmx.export.annotation.ManagedOperationParameter(name="triggerName", description="quartz trigger name"), @org.springframework.jmx.export.annotation.ManagedOperationParameter(name="groupName", description="quartz trigger group name")})
  public abstract void resumeJob(String paramString1, String paramString2);
  
  @ManagedOperation(description="")
  @ManagedOperationParameters({@org.springframework.jmx.export.annotation.ManagedOperationParameter(name="triggerName", description="quartz trigger name"), @org.springframework.jmx.export.annotation.ManagedOperationParameter(name="groupName", description="quartz trigger group name")})
  public abstract void stopJob(String paramString1, String paramString2);
  
  @ManagedOperation(description="")
  @ManagedOperationParameters({@org.springframework.jmx.export.annotation.ManagedOperationParameter(name="triggerName", description="quartz trigger name"), @org.springframework.jmx.export.annotation.ManagedOperationParameter(name="groupName", description="quartz trigger group name")})
  public abstract void restartJob(String paramString1, String paramString2);
  
  @ManagedOperation(description="get trigger's job running status")
  @ManagedOperationParameters({@org.springframework.jmx.export.annotation.ManagedOperationParameter(name="triggerName", description="quartz trigger name"), @org.springframework.jmx.export.annotation.ManagedOperationParameter(name="groupName", description="quartz trigger group name")})
  public abstract JobBizStatusEnum getJobStatus(String paramString1, String paramString2);
  
  @ManagedOperation(description="get trigger was job ran logs")
  @ManagedOperationParameters({@org.springframework.jmx.export.annotation.ManagedOperationParameter(name="triggerName", description="quartz trigger name"), @org.springframework.jmx.export.annotation.ManagedOperationParameter(name="groupName", description="quartz trigger group name"), @org.springframework.jmx.export.annotation.ManagedOperationParameter(name="lastDate", description="ran of last time")})
	public abstract String getLogs(String paramString1, String paramString2,
			long paramLong);
}
