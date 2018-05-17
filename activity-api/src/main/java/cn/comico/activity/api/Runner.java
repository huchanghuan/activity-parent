package cn.comico.activity.api;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.comico.activity.api.verticle.ServiceVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class Runner {
	
	private static final Logger log = LoggerFactory.getLogger(Runner.class);

	public static void main(String[] args) {
		 //创建vertx容器实例
		final Vertx vertxInstance = Vertx.vertx();
        //部署verticle组件
        DeploymentOptions options = new DeploymentOptions();
        JsonObject config = new JsonObject();
        config.put("http.port",8081);
        options.setConfig(config);
        options.setInstances(1);
        vertxInstance.deployVerticle(new ServiceVerticle(),options);
      
        log.info("start activity-api successful ~");
	}
}
