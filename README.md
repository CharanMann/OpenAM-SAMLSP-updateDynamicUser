# OpenAM-SAMLSP-updateDynamicUser

* OpenAM SP SAML Attribute extension  <br />
* OpenAM Dynamic user profile feature allows OpenAM (acting as SAML SP) to dynamically create users but these users attributes are not updated if they are updated on IdP side. More details in *[OPENAM-8340](https://bugster.forgerock.org/jira/browse/OPENAM-8340)* <br />
* This extension resolves this issue. Note that a better solution may include to have something like IDM to sync attributes between IdP and SP. 

Disclaimer of Liability :
=========================
This repository contain code created and maintained by the community of forgerock users and other open source contributors.
In some cases the code may have been created by ForgeRock for community development subject to the license for specific
language governing permission and limitations under the license.

The code is provided on an "as is" basis, without warranty of any kind, to the fullest extent permitted by law. 

ForgeRock does not warrant or guarantee the individual success developers may have in implementing the code on their 
development platforms or in production configurations.

ForgeRock does not warrant, guarantee or make any representations regarding the use, results of use, accuracy, timeliness 
or completeness of any data or information relating to the alpha release of unsupported code. ForgeRock disclaims all 
warranties, expressed or implied, and in particular, disclaims all warranties of merchantability, and warranties related 
to the code, or any service or software related thereto.

ForgeRock shall not be liable for any direct, indirect or consequential damages or costs of any type arising out of any 
action taken by you or others related to the code.

Pre-requisites :
================
* Versions used for this project: OpenAM 13.5, OpenDJ 3.5 
1. OpenAM has been installed and configured.
2. Maven has been installed and configured.

OpenAM Configuration:
=====================
1. Build the custom plugin by using maven. 
2. Navigate to Federation > Entity Providers > (SP Hosted Entity) > Assertion Processing. Specify 'org.forgerock.openam.saml2.plugins.examples.UpdateDynamicUserSPAttMapper' under Attribute Mapper. 
3. Deploy openam-samlSP-updateDynamicUser-1.0.jar under (OpenAM-Tomcat)/webapps/openam/WEB-INF/lib
4. Restart OpenAM
  
Testing:
======== 
* Create dynamic user on OpenAM SP
* Update this user attributes at IdP side and initiate SAML flow. Updated attributes at IdP side should be persisted in OpenAM SP datastore.  


* * *

Copyright © 2017 ForgeRock, AS.

The contents of this file are subject to the terms of the Common Development and
Distribution License (the License). You may not use this file except in compliance with the
License.

You can obtain a copy of the License at legal/CDDLv1.0.txt. See the License for the
specific language governing permission and limitations under the License.

When distributing Covered Software, include this CDDL Header Notice in each file and include
the License file at legal/CDDLv1.0.txt. If applicable, add the following below the CDDL
Header, with the fields enclosed by brackets [] replaced by your own identifying
information: "Portions copyright [year] [name of copyright owner]".

Portions Copyrighted 2017 Charan Mann
