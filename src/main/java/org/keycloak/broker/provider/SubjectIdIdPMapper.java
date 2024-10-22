/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.keycloak.broker.provider;

import org.keycloak.models.IdentityProviderMapperModel;
import org.keycloak.models.IdentityProviderSyncMode;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.utils.KeycloakModelUtils;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class SubjectIdIdPMapper extends AbstractIdentityProviderMapper {

    public static final String[] COMPATIBLE_PROVIDERS = {
            IdentityProviderMapper.ANY_PROVIDER
    };

    private static final List<ProviderConfigProperty> configProperties = new ArrayList<>();

    private static final String SCOPE = "scope";
    private static final String USER_ATTRIBUTE = "user.attribute";
    private static final String USERNAME = "username";
    private static final String DEFAULT_SCOPE = "example.org";
    private static final Set<IdentityProviderSyncMode> IDENTITY_PROVIDER_SYNC_MODES = new HashSet<>(Arrays.asList(IdentityProviderSyncMode.values()));


    static {
        ProviderConfigProperty property;

        property = new ProviderConfigProperty();
        property.setName(USER_ATTRIBUTE);
        property.setLabel("Subject Id Attribute Name");
        property.setHelpText("User attribute name to store the Subject Identifier for the user. Defaults to 'username'");
        property.setDefaultValue(USERNAME);
        property.setType(ProviderConfigProperty.STRING_TYPE);
        configProperties.add(property);

        property = new ProviderConfigProperty();
        property.setName(SCOPE);
        property.setLabel("Scope");
        property.setHelpText("Append a @scope value.");
        property.setType(ProviderConfigProperty.STRING_TYPE);
        configProperties.add(property);

    }

    public static final String PROVIDER_ID = "subject-id-idp-mapper";

    @Override
    public boolean supportsSyncMode(IdentityProviderSyncMode syncMode) {
        return IDENTITY_PROVIDER_SYNC_MODES.contains(syncMode);
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return configProperties;
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public String[] getCompatibleProviders() {
        return COMPATIBLE_PROVIDERS;
    }

    @Override
    public String getDisplayCategory() {
        return "Subject ID Mapper";
    }

    @Override
    public String getDisplayType() {
        return "Subject ID Mapper";
    }


    @Override
    public void preprocessFederatedIdentity(KeycloakSession session, RealmModel realm, IdentityProviderMapperModel mapperModel, BrokeredIdentityContext context) {

        String scope = mapperModel.getConfig().getOrDefault(SCOPE, DEFAULT_SCOPE);
        String attribute = mapperModel.getConfig().getOrDefault(USER_ATTRIBUTE, USERNAME);
        if (USERNAME.equalsIgnoreCase(attribute)) {
            context.setUsername(KeycloakModelUtils.generateId() + "@" + scope);
        } else {
            context.setUserAttribute(attribute, KeycloakModelUtils.generateId() + "@" + scope);
        }
    }


    @Override
    public void updateBrokeredUser(KeycloakSession session, RealmModel realm, UserModel user, IdentityProviderMapperModel mapperModel, BrokeredIdentityContext context) {

    }

    @Override
    public String getHelpText() {
        return "Generate a new Subject Identifier attribute according to OASIS Subject-ID to be used as a unique identifier for the user. The mapper works only in import sync mode. Any other sync mode will be ignored";
    }


}
