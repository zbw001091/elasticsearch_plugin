package com.zbw.espluginbasic.listener;

import org.elasticsearch.cluster.ClusterChangedEvent;
import org.elasticsearch.cluster.ClusterStateListener;
import org.elasticsearch.cluster.service.ClusterService;
import org.elasticsearch.common.inject.Inject;

public class HelloListener implements ClusterStateListener {
	private final ClusterService clusterService;
	
	@Inject
    public HelloListener(ClusterService clusterService) {
        this.clusterService = clusterService;
        this.clusterService.addListener(this);
    }
	
	@Override
	public void clusterChanged(ClusterChangedEvent event) {
		System.out.println("zbw clusterState catched: " + event);
	}

}
