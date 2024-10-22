# keycloak-subject-id-attribute-mapper
An Identity Provider (IdP) attribute mapper for Keycloak designed to generate a unique Subject Identifier. 
The identifier is constructed from two components: a “unique ID” and a “scope,” which are concatenated using an "@" symbol (ASCII 64) as a delimiter. 
By default, the generated identifier is mapped to the user's username, but this mapping can be customised through the mapper configuration settings

### Installation instructions:

1. Compile the plugin jar i.e. 'mvn clean install' or just get a built one from the "Releases" link on the right sidebar.
2. Drop the jar into the folder $KEYCLOAK_BASE/providers/ and let all the hot-deploy magic commence.

### Use instructions

If the installation is successful, you will be able to use the SAML IdentityProvider mapper **Subject ID Mapper**  as named in the UI droplist of the mappers type selection. 
