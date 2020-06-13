package com.zbw.espluginbasic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import org.elasticsearch.cluster.metadata.IndexNameExpressionResolver;
import org.elasticsearch.cluster.node.DiscoveryNodes;
import org.elasticsearch.common.inject.Module;
import org.elasticsearch.common.settings.ClusterSettings;
import org.elasticsearch.common.settings.IndexScopedSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.SettingsFilter;
import org.elasticsearch.plugins.ActionPlugin;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.rest.RestController;
import org.elasticsearch.rest.RestHandler;

import com.zbw.espluginbasic.module.HelloModule;

public class HelloPlugin extends Plugin implements ActionPlugin {
	
	/**
     * Rest handlers added by this plugin.
     */
	@Override
	public List<RestHandler> getRestHandlers(Settings settings, RestController restController,
			ClusterSettings clusterSettings, IndexScopedSettings indexScopedSettings, SettingsFilter settingsFilter,
			IndexNameExpressionResolver indexNameExpressionResolver, Supplier<DiscoveryNodes> nodesInCluster) {
		return Collections.singletonList(new HelloHandler(settings, restController));
	}

	@Override
	public Collection<Module> createGuiceModules() {
		HelloModule hm = new HelloModule();
		List moduleCollection = new ArrayList<Module>(); 
		moduleCollection.add(hm);
		return moduleCollection;
	}
	
}
