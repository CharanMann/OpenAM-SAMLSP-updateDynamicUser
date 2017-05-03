/*
 * Copyright © 2017 ForgeRock, AS.
 *
 * The contents of this file are subject to the terms of the Common Development and
 * Distribution License (the License). You may not use this file except in compliance with the
 * License.
 *
 * You can obtain a copy of the License at legal/CDDLv1.0.txt. See the License for the
 * specific language governing permission and limitations under the License.
 *
 * When distributing Covered Software, include this CDDL Header Notice in each file and include
 * the License file at legal/CDDLv1.0.txt. If applicable, add the following below the CDDL
 * Header, with the fields enclosed by brackets [] replaced by your own identifying
 * information: "Portions copyright [year] [name of copyright owner]".
 *
 * Portions Copyrighted 2017 Charan Mann
 *
 * OpenAM-SAMLSP-updateDynamicUser: Created by Charan Mann on 4/27/17 , 9:39 AM.
 */

package org.forgerock.openam.saml2.plugins.examples;

import com.sun.identity.plugin.datastore.DataStoreProvider;
import com.sun.identity.plugin.datastore.DataStoreProviderException;
import com.sun.identity.saml2.assertion.Attribute;
import com.sun.identity.saml2.common.SAML2Exception;
import com.sun.identity.saml2.common.SAML2Utils;
import com.sun.identity.saml2.plugins.DefaultSPAttributeMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class extends {@link DefaultSPAttributeMapper} to update attributes present in SAML assertion to OpenAM datastore
 */
public class UpdateDynamicUserSPAttMapper extends DefaultSPAttributeMapper {

    private static DataStoreProvider dataStoreProvider = null;

    static {
        try {
            // Initialize dataStoreProvider
            dataStoreProvider = SAML2Utils.getDataStoreProvider();
        } catch (SAML2Exception e) {
            debug.error("UpdateDynamicUserSPAttMapper.static initialization failed " +
                    e);
        }
    }

    /**
     * Constructor.
     */
    public UpdateDynamicUserSPAttMapper() {
        debug.message("UpdateDynamicUserSPAttMapper.constructor");
    }

    /**
     * Returns attribute map for the given list of <code>Attribute</code>
     * objects. Also, updates the attributes in OpenAM database if user is already present
     *
     * @param attributes     list <code>Attribute</code>objects.
     * @param userID         universal identifier or distinguished name(DN) of the user.
     * @param hostEntityID   <code>EntityID</code> of the hosted provider.
     * @param remoteEntityID <code>EntityID</code> of the remote provider.
     * @param realm          realm name.
     * @return a map of mapped attribute value pair. This map has the
     * key as the attribute name and the value as the attribute value
     * @throws SAML2Exception if any failure.
     */
    @Override
    public Map<String, Set<String>> getAttributes(
            List<Attribute> attributes,
            String userID,
            String hostEntityID,
            String remoteEntityID,
            String realm
    ) throws SAML2Exception {
        Map<String, Set<String>> samlUserAttrMap = super.getAttributes(attributes, userID, hostEntityID, remoteEntityID, realm);

        try {
            // Check if the user exists
            if (dataStoreProvider.isUserExists(userID)) {
                // Get user existing attributes that are present in SAML assertion
                Map<String, Set<String>> existingUserAttrMap = dataStoreProvider.getAttributes(userID, samlUserAttrMap.keySet());
                Map<String, Set<String>> updatedUserAttrMap = new HashMap<>();

                for (Map.Entry<String, Set<String>> entry : samlUserAttrMap.entrySet()) {
                    String attribute = entry.getKey();

                    // If the attribute is not already there in User store, then add the SAML assertion values
                    if (!existingUserAttrMap.containsKey(attribute)) {
                        if(debug.messageEnabled()) {
                            debug.message("UpdateDynamicUserSPAttMapper.getAttribute: New attribute in SAML assertion that will be persisted: " + attribute);
                        }
                        updatedUserAttrMap.put(attribute, samlUserAttrMap.get(attribute));
                    }

                    // If existingUserAttrMap attribute are different from samlUserAttrMap attribute values
                    else if (!existingUserAttrMap.get(attribute).equals(samlUserAttrMap.get(attribute))) {
                        if(debug.messageEnabled()) {
                            debug.message("UpdateDynamicUserSPAttMapper.getAttribute: Updated attribute in SAML assertion that will be persisted: " + attribute);
                        }
                        updatedUserAttrMap.put(attribute, samlUserAttrMap.get(attribute));
                    }
                }

                // If updatedUserAttrMap is not empty, then persist attributes
                if (!updatedUserAttrMap.isEmpty()) {
                    if(debug.messageEnabled()) {
                        debug.message("UpdateDynamicUserSPAttMapper.getAttribute: Persisting user attributes");
                    }
                    dataStoreProvider.setAttributes(userID, updatedUserAttrMap);
                }
            }
        } catch (DataStoreProviderException e) {
            debug.error("UpdateDynamicUserSPAttMapper.getAttributes", e);
            throw new SAML2Exception(e.getMessage());
        }

        return samlUserAttrMap;
    }
}
