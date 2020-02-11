/**
 * Copyright 2014-2019  the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.webank.fabric.node.manager.common.pojo.chaincode;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * deploy chainCode info entity.
 */
@Data
public class DeployInputParam {
    @NotNull
    private Integer channelId;
    @NotNull
    private Integer chainCodePk;
    @NotBlank
    private String chainCodeName;
    @NotBlank
    private String chainCodeVersion;
    @NotBlank
    private String chainCodeSource;
    private String chainCodeLang;
    private List<Object> initParams;
}
