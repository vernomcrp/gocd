/*
 * Copyright 2018 ThoughtWorks, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.thoughtworks.go.apiv1.serverhealthmessages;


import com.thoughtworks.go.api.ApiController;
import com.thoughtworks.go.api.ApiVersion;
import com.thoughtworks.go.api.spring.ApiAuthenticationHelper;
import com.thoughtworks.go.api.util.GsonTransformer;
import com.thoughtworks.go.apiv1.serverhealthmessages.representers.ServerHealthMessagesRepresenter;
import com.thoughtworks.go.serverhealth.ServerHealthService;
import com.thoughtworks.go.serverhealth.ServerHealthStates;
import com.thoughtworks.go.spark.Routes;
import spark.Request;
import spark.Response;

import static spark.Spark.*;

public class ServerHealthMessagesControllerDelegate extends ApiController {
    private final ServerHealthService serverHealthService;
    private final ApiAuthenticationHelper apiAuthenticationHelper;

    public ServerHealthMessagesControllerDelegate(ServerHealthService serverHealthService, ApiAuthenticationHelper apiAuthenticationHelper) {
        super(ApiVersion.v1);
        this.serverHealthService = serverHealthService;
        this.apiAuthenticationHelper = apiAuthenticationHelper;
    }

    @Override
    public String controllerBasePath() {
        return Routes.ServerHealthMessages.BASE;
    }

    @Override
    public void setupRoutes() {
        path(Routes.ServerHealthMessages.BASE, () -> {
            before("", this::setContentType);
            before("/*", this::setContentType);

            before("", apiAuthenticationHelper::checkUserAnd401);
            before("/*", apiAuthenticationHelper::checkUserAnd401);

            get("", this::show, GsonTransformer.getInstance());
            head("", this::show, GsonTransformer.getInstance());
        });
    }

    public Object show(Request request, Response response) {
        ServerHealthStates allLogs = serverHealthService.getAllLogs();
        return ServerHealthMessagesRepresenter.toJSON(allLogs, null);
    }
}