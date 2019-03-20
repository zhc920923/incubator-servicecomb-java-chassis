/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.servicecomb.swagger.invocation.arguments.consumer;

import java.util.ArrayList;
import java.util.List;

import org.apache.servicecomb.foundation.common.utils.bean.Getter;
import org.apache.servicecomb.swagger.invocation.SwaggerInvocation;
import org.apache.servicecomb.swagger.invocation.arguments.ArgumentMapper;

/**
 * <pre>
 * consumer: void add(QueryWrapper query)
 *           class QueryWrapper {
 *             int x;
 *             int y;
 *           }
 * contract; void add(int x, int y)
 * </pre>
 */
public final class ConsumerArgumentExtractConsumerFields implements ArgumentMapper {
  private class FieldMeta {
    int swaggerIdx;

    Getter<Object, Object> getter;

    public FieldMeta(int swaggerIdx, Getter<Object, Object> getter) {
      this.swaggerIdx = swaggerIdx;
      this.getter = getter;
    }
  }

  private int consumerIdx;

  private List<FieldMeta> fields = new ArrayList<>();

  public ConsumerArgumentExtractConsumerFields(int consumerIdx) {
    this.consumerIdx = consumerIdx;
  }

  public void addField(int swaggerIdx, Getter<Object, Object> getter) {
    fields.add(new FieldMeta(swaggerIdx, getter));
  }

  @Override
  public void mapArgument(SwaggerInvocation invocation, Object[] consumerArguments) {
    Object consumerArgument = consumerArguments[consumerIdx];
    if (consumerArgument == null) {
      return;
    }

    Object[] contractArguments = invocation.getSwaggerArguments();
    for (FieldMeta fieldMeta : fields) {
      contractArguments[fieldMeta.swaggerIdx] = fieldMeta.getter.get(consumerArgument);
    }
  }
}