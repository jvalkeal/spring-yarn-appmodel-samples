/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package hello.appmaster.support.jackson;

import org.apache.hadoop.yarn.api.records.Container;
import org.apache.hadoop.yarn.api.records.ContainerId;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * Jackson {@link Module} registering custom serializers
 * handling YARN proto buffer based records.
 *
 * @author Janne Valkealahti
 *
 */
public class YarnProtoModule extends SimpleModule {

	private static final long serialVersionUID = 3060130404108559404L;

	/**
	 * Instantiates a new yarn proto module.
	 */
	public YarnProtoModule() {
		addSerializer(Container.class, new ContainerSerializer());
		addSerializer(ContainerId.class, new ContainerIdSerializer());
	}

}
