package cn.comico.activity.api.handler;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import pcitc.imp.common.ettool.baseresrep.ErrorInfo;
import pcitc.imp.common.ettool.utils.RestfulTool;

public class BaseHandler implements Handler<RoutingContext>{

	@Override
	public void handle(RoutingContext arg0) {
		
	}

	protected void returnCollection(RoutingContext routingContext, String collection) {
		if (collection == null) {
			routingContext.response().putHeader("content-type", "application/json; charset=utf-8").end();
		} else {
			routingContext.response().putHeader("content-type", "application/json; charset=utf-8").end(collection);
		}
	}
	
	protected <T extends Serializable> void asyncResultHandle(RoutingContext routingContext, AsyncResult<List<T>> res, Class<T> clazz) {
		String collectionStr = "";
		try {
			if (res.failed()) {
				collectionStr = res.cause().getMessage();
//				collectionStr = RestfulTool.buildCollection(new ErrorInfo("", "", res.cause().getMessage()), routingContext.request().uri());
			} else {
				List<T> list = (List<T>) res.result();
				collectionStr = JSON.toJSONString(list);
//				collectionStr = RestfulTool.buildCollection(list, routingContext.request().uri(), clazz);
			}
		} catch (Exception e) {
			collectionStr = e.getMessage();
//			collectionStr = RestfulTool.buildCollection(new ErrorInfo("", "", e.getMessage()), routingContext.request().uri());
		}
		returnCollection(routingContext, collectionStr);
	}
}
