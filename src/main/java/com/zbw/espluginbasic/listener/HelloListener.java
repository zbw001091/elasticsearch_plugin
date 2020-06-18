package com.zbw.espluginbasic.listener;

import org.elasticsearch.cluster.ClusterChangedEvent;
import org.elasticsearch.cluster.ClusterStateListener;
import org.elasticsearch.cluster.metadata.MetaData;
import org.elasticsearch.cluster.node.DiscoveryNodes;
import org.elasticsearch.cluster.service.ClusterService;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.transport.TransportService;

public class HelloListener implements ClusterStateListener {
	private final ClusterService clusterService;
	private final TransportService transportService;
	
	@Inject
    public HelloListener(ClusterService clusterService, TransportService transportService) {
        this.clusterService = clusterService;
        this.clusterService.addListener(this);
        
        // inject transportService, to record every ClusterState into ES index
        this.transportService = transportService;
    }
	
	/**
	 * in ClusterState,
	 * only metadata() is persisted on disk
	 * other structure including nodes() is built dynamically in memory
	 */
	@Override
	public void clusterChanged(ClusterChangedEvent event) {
		System.out.println("***********************");
		System.out.println("zbw clusterState catched");
		System.out.println(event.state().getClusterName());
		System.out.println(event.state().getVersion());
		System.out.println(event.state().blocks());
		printNodes(event.state().nodes(),event.previousState().nodes());
		printMeta(event.state().metaData());
		System.out.println(event.state().routingTable());
		System.out.println(event.state().getRoutingNodes());
		System.out.println("***********************");
	}
	
	private void printNodes(DiscoveryNodes nodes, DiscoveryNodes prevNodes) {
		System.out.println(nodes.getMasterNodeId());
		System.out.println(nodes.getDataNodes());
		System.out.println(nodes.getSize() + " nodes in this cluster");
		System.out.println("local node: " + nodes.getLocalNodeId());
		System.out.println("local node == master ? " + nodes.isLocalNodeElectedMaster());
		System.out.println("node diff: " + nodes.diff(prevNodes));
		System.out.println("prevMaster: " + nodes.delta(prevNodes).previousMasterNode());
		System.out.println("newMaster: " + nodes.delta(prevNodes).newMasterNode());
		System.out.println("prevMaster: " + nodes.delta(prevNodes).previousMasterNode());
		System.out.println("removedNodes: " + nodes.delta(prevNodes).removedNodes());
		System.out.println("addedNodes: " + nodes.delta(prevNodes).addedNodes());
	}

	private void printMeta(MetaData meta) {
		System.out.println("*** Cluster-Level-Meta");
		System.out.println(meta.getTotalNumberOfShards());
		System.out.println(meta.getTemplates());
		System.out.println(meta.persistentSettings());
		System.out.println(meta.settings());
		System.out.println(meta.transientSettings());
		System.out.println("*** Index-Level-Meta");
		System.out.println(meta.indices());
	}
}
