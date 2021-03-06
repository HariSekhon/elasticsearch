/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.action.search;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.ActionRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.Scroll;

/**
 * A search scroll action request builder.
 */
public class SearchScrollRequestBuilder extends ActionRequestBuilder<SearchScrollRequest, SearchResponse, SearchScrollRequestBuilder, Client> {

    private SearchScrollSourceBuilder sourceBuilder;

    public SearchScrollRequestBuilder(Client client) {
        super(client, new SearchScrollRequest());
    }

    public SearchScrollRequestBuilder(Client client, String scrollId) {
        super(client, new SearchScrollRequest());
        if (sourceBuilder == null) {
            sourceBuilder = new SearchScrollSourceBuilder();
        }
        sourceBuilder.scrollId(scrollId);
    }

    private SearchScrollSourceBuilder sourceBuilder() {
        if (sourceBuilder == null) {
            sourceBuilder = new SearchScrollSourceBuilder();
        }
        return sourceBuilder;
    }

    /**
     * Should the listener be called on a separate thread if needed.
     */
    public SearchScrollRequestBuilder listenerThreaded(boolean threadedListener) {
        request.listenerThreaded(threadedListener);
        return this;
    }

    /**
     * The scroll id to use to continue scrolling.
     */
    public SearchScrollRequestBuilder setScrollId(String scrollId) {
        sourceBuilder().scrollId(scrollId);
        return this;
    }

    /**
     * If set, will enable scrolling of the search request.
     */
    public SearchScrollRequestBuilder setScroll(Scroll scroll) {
        sourceBuilder().scroll(scroll);
        return this;
    }

    /**
     * If set, will enable scrolling of the search request for the specified timeout.
     */
    public SearchScrollRequestBuilder setScroll(TimeValue keepAlive) {
        sourceBuilder().scroll(keepAlive);
        return this;
    }

    /**
     * If set, will enable scrolling of the search request for the specified timeout.
     */
    public SearchScrollRequestBuilder setScroll(String keepAlive) {
        sourceBuilder().scroll(keepAlive);
        return this;
    }


    public SearchScrollRequestBuilder setSource(String source) {
        request.source(source);
        return this;
    }

    public SearchScrollRequestBuilder setSource(BytesReference source) {
        request.source(source);
        return this;
    }

    @Override
    protected void doExecute(ActionListener<SearchResponse> listener) {
        if (sourceBuilder != null) {
            request.source(sourceBuilder);
        }
        client.searchScroll(request, listener);
    }
}
