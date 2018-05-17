package cn.comico.activity.api.verticle;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;

import cn.comico.activity.api.config.MySpringConfiguration;
import cn.comico.activity.util.annotation.RequestMapping;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

public class ServiceVerticle extends AbstractVerticle{

	private ApplicationContext context;
	
	public ServiceVerticle() {
		if (this.context == null) {
			this.context = new AnnotationConfigApplicationContext(MySpringConfiguration.class);
//			Arrays.asList(context.getBeanDefinitionNames()).forEach(System.out::println);
		}

	}

	@Override
	public void start(Future<Void> future) throws Exception {
		super.start(future);
		Router router = Router.router(vertx);
		
		init();
		buildHelloWorld(router);
		buildApiResource(router);
	}


	private void init() {
		
		vertx.executeBlocking(future -> {
			future.complete();
		}, ar -> {
			
		});
		
		
	}
	
	private void buildHelloWorld(Router router) {
		router.route("/")
				.handler(ctx -> {
					StringBuffer data = new StringBuffer();
					data.append("<h1>hello world!</h1>");
					data.append("<hr>");
					
					ctx.response().end(data.toString());
				});
		
		vertx.createHttpServer().requestHandler(router::accept).listen(8080);
	}
	
	private void buildApiResource(Router router) {
		Map<String, Object> beanMap = context.getBeansWithAnnotation(Controller.class);
		for (Entry<String, Object> entry : beanMap.entrySet()) {
			Class<?> clazz = entry.getValue().getClass();
			RequestMapping typeRequestMapping = clazz.getAnnotation(RequestMapping.class);
			Method[] methods = clazz.getDeclaredMethods();
			if (null != methods) {
				Arrays.asList(methods).stream().forEach(method -> {
					if (!method.isAccessible()) {
						RequestMapping methodRequestMapping = method.getAnnotation(RequestMapping.class);
						if (null != methodRequestMapping) {
							String path = (null != typeRequestMapping ? typeRequestMapping.value() : "") + methodRequestMapping.value();
							System.out.println(path);
							//设置路由路径
							Route route = router.route(path);
							//方法允许的HTTP METHOD
							Arrays.asList(methodRequestMapping.method()).stream().forEach(httpMethod -> {
								route.method(httpMethod);
							});
							
							
							if (null != typeRequestMapping && 0 == methodRequestMapping.method().length) {
								Arrays.asList(typeRequestMapping.method()).stream().forEach(httpMethod -> {
									route.method(httpMethod);
								});
								
								if (0 == typeRequestMapping.method().length) {
									route.method(HttpMethod.GET);
								}
							}
							//设置请求执行的方法
							route.handler(ctx -> {try {
								method.invoke(entry.getValue(), ctx);
							} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
								e.printStackTrace();
							}});
						}
					}
					
				});
			}
		}
	}
}
