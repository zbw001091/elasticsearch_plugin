package com.zbw.espluginbasic;

import java.io.IOException;
import java.util.Date;

import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.rest.BaseRestHandler;
import org.elasticsearch.rest.BytesRestResponse;
import org.elasticsearch.rest.RestController;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.rest.RestStatus;

public class HelloHandler extends BaseRestHandler {
	
	protected HelloHandler(Settings settings, RestController restController) {
        restController.registerHandler(RestRequest.Method.GET, "/_hello", this);
    }

    @Override
    protected BaseRestHandler.RestChannelConsumer prepareRequest(RestRequest restRequest, NodeClient nodeClient) throws IOException {
        //接收的参数
        System.out.println("params==" + restRequest.params());

        long t1 = System.currentTimeMillis();

        String name = restRequest.param("name");

        long cost = System.currentTimeMillis() - t1;
        //返回内容，这里返回一个处理业务逻辑的花费时间，前端传的name，以及当前时间。
        return channel -> {
            XContentBuilder builder = channel.newBuilder();
            builder.startObject();
            builder.field("cost", cost);
            builder.field("name", name);
            builder.field("time", new Date());
            builder.endObject();
            channel.sendResponse(new BytesRestResponse(RestStatus.OK, builder));
        };
    }

	@Override
	public String getName() {
		return null;
	}
}
