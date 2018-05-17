package cn.comico.activity.api.handler;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import cn.comico.activity.model.PersonModel;
import cn.comico.activity.service.IPersonService;
import cn.comico.activity.util.annotation.RequestMapping;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

@Controller
@RequestMapping(value = "/person")
public class PersonHandler extends BaseHandler{
	
	@Autowired
	private IPersonService personService;

	@RequestMapping(value = "/get", method = HttpMethod.GET)
	public void saveArea(RoutingContext routingContext) {
		String strCollection = routingContext.getBodyAsString();
		routingContext.vertx()
			.executeBlocking((Handler<Future<List<PersonModel>>>)future -> {
				try {
					//1/解析，获取参数
					//2/校验
					
					//3/业务处理
					
					//service -> dao数据访问
					PersonModel person = personService.getPerson(1);
					
					//4/完成处理
					future.complete(Arrays.asList(person));
				} catch (Exception e) {
					future.fail(e);
				}
			}, res -> asyncResultHandle(routingContext,res, PersonModel.class));
	}
	
}
