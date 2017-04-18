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

package org.elasticsearch.search.aggregations.pipeline;

import org.elasticsearch.search.DocValueFormat;
import org.elasticsearch.search.aggregations.InternalAggregationTestCase;
import org.elasticsearch.search.aggregations.ParsedAggregation;

import java.util.List;
import java.util.Map;

public class InternalSimpleValueTests extends InternalAggregationTestCase<InternalSimpleValue>{

    @Override
    protected InternalSimpleValue createTestInstance(String name, List<PipelineAggregator> pipelineAggregators,
            Map<String, Object> metaData) {
        DocValueFormat formatter = randomNumericDocValueFormat();
        double value = frequently() ? randomDoubleBetween(0, 100000, true)
                : randomFrom(new Double[] { Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NaN });
        return new InternalSimpleValue(name, value, formatter, pipelineAggregators, metaData);
    }

    @Override
    protected void assertFromXContent(InternalSimpleValue simpleValue, ParsedAggregation parsedAggregation) {
        ParsedSimpleValue parsed = ((ParsedSimpleValue) parsedAggregation);
        if (Double.isInfinite(simpleValue.getValue()) == false && Double.isNaN(simpleValue.getValue()) == false) {
            assertEquals(simpleValue.getValue(), parsed.value(), 0);
            assertEquals(simpleValue.getValueAsString(), parsed.getValueAsString());
        } else {
            // we write Double.NEGATIVE_INFINITY, Double.POSITIVE amd Double.NAN to xContent as 'null', so we
            // cannot differentiate between them. Also we cannot recreate the exact String representation
            assertEquals(parsed.value(), Double.NaN, 0);
        }
    }
}
