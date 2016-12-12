 package cn.magicstudio.mblog.utils;
 
 import java.util.HashMap;
 import java.util.Map;
 import org.springframework.beans.BeansException;
 import org.springframework.beans.factory.config.BeanPostProcessor;
 
 
 public class BeanMockInjector
   implements BeanPostProcessor
 {
   private static Map<String, Object> MOCKS = new HashMap();
   
   public Object postProcessBeforeInitialization(Object bean, String beanName)
     throws BeansException
   {
     return bean;
   }
   
   public Object postProcessAfterInitialization(Object bean, String beanName)
     throws BeansException
   {
     if (MOCKS.containsKey(beanName)) {
       return MOCKS.get(beanName);
     }
     return bean;
   }
   
   public static void addMock(String beanName, Object mock) {
     MOCKS.put(beanName, mock);
   }
   
   public static void clear() {
     MOCKS.clear();
   }
 }
