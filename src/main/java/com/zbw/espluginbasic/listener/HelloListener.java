package com.zbw.espluginbasic.listener;

import java.io.IOException;
import java.util.Date;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.cluster.ClusterChangedEvent;
import org.elasticsearch.cluster.ClusterStateListener;
import org.elasticsearch.cluster.metadata.MetaData;
import org.elasticsearch.cluster.node.DiscoveryNodes;
import org.elasticsearch.cluster.service.ClusterService;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.mapper.ObjectMapper;
import org.elasticsearch.transport.TransportService;

public class HelloListener implements ClusterStateListener {
	private final ClusterService clusterService;
	private final TransportService transportService;
	private final NodeClient client;
	
	/**
	 * 在Node.java中，已经向Guice中注入了bean。所以，可以在本Plugin构造器中注入该NodeClient对象。
	 * b.bind(Client.class).toInstance(client);
	 * b.bind(NodeClient.class).toInstance(client);
	 * 
	 * @param clusterService
	 * @param transportService
	 * @param client
	 */
	@Inject
    public HelloListener(ClusterService clusterService, TransportService transportService, NodeClient client) {
        this.clusterService = clusterService;
        this.clusterService.addListener(this);
        
        // inject transportService, to record every ClusterState into ES index
        this.transportService = transportService;
        this.client = client;
    }
	
	/**
	 * in ClusterState,
	 * only metadata() is persisted on disk
	 * other structure including nodes() is built dynamically in memory
	 */
	@Override
	public void clusterChanged(ClusterChangedEvent event) {
//		System.out.println("***********************");
//		System.out.println("zbw clusterState catched");
//		System.out.println(event.state().getClusterName());
//		System.out.println(event.state().getVersion());
//		System.out.println(event.state().blocks());
//		printNodes(event.state().nodes(),event.previousState().nodes());
//		printMeta(event.state().metaData());
//		System.out.println(event.state().routingTable());
//		System.out.println(event.state().getRoutingNodes());
//		System.out.println("***********************");
		
		client.index(buildIndexRequest(event));
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
	
	private IndexRequest buildIndexRequest(ClusterChangedEvent event) {
		XContentBuilder source = null;
		try {
			source = XContentFactory.jsonBuilder()
			        .startObject()
			            .field("version", event.state().getVersion())
			            .field("cluster_name", event.state().getClusterName().value())
			            .field("master_node", event.state().nodes().getMasterNodeId())
			        .endObject();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		IndexRequest indexRequest = new IndexRequest(".zbwclusterstate");
		indexRequest.source(source);
		System.out.println(event.toString());
		return indexRequest;
	}
}
