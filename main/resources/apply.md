Auth pipeline
 
1. Get EMV Master Token:
    Send POST request to {url}/oauth/token
        with "Multipart Form" body - key=grant_type, value=client_credentials
        with empty headers
        with "Basic Auth" - username={ids_emv_master_username}, password={ids_emv_master_pass}
    then assert that response.status is 200 and response.body.{access_token} is present
    then response.body.{access_token} is stored as 'ids_emv_master_token' to use in next steps
 
 
2. Create EMV new identity:
    Send POST request to {url}/clients
        with pre-request (taken from Bruno):
        {
            const { v4: uuidv4 } = require('uuid');
            const subuuid = uuidv4().substring(0,10);
            bru.setEnvVar("subuuid", subuuid)
        }
 
        with "JSON" body:
        {
            "identityName": "id-for-emv-{{subuuid}}",
            "description": "Sample Description",
            "scopes": [
                "emvtok.create",
                "emvtok.read",
                "emvtok.update",
                "emvtok.delete"
            ],
            "resources": [
                "http://tokenization-gateway/"
            ],
            "additionalInfo": {
            }
        }
 
    with empty headers
    with bearer token - {ids_emv_master_token} - stored in step 1
    then assert that response.status is 201 and (response.body.{accessKeySecret}) and response.body.{accessKeyId} is present
    then response.body.{accessKeyId} is stored as 'emv_username' to use in next steps
    then response.body.{accessKeySecret} is stored as 'emv_password' to use in next steps
 
 
3. Get EMV identity JWT token:
    Send POST request to {url}/oauth/token
        with "Multipart Form" body - key=grant_type, value=client_credentials
        with empty headers
        with "Basic Auth" - username={emv_username} - stored in step 2, password={emv_password} - stored in step 2
    then assert that response.status is 200 and response.body.{access_token} is present
    then response.body.{access_token} is stored as 'emv_jwt' to use in next steps
 
 
4. Get GATEWAY Master Token:
    Send POST request to {url}/oauth/token
        with "Multipart Form" body - key=grant_type, value=client_credentials
        with empty headers
        with "Basic Auth" - username={ids_gateway_master_username}, password={ids_gateway_master_pass}
    then assert that response.status is 200 and response.body.{access_token} is present
    then response.body.{access_token} is stored as 'ids_gateway_master_token' to use in next steps
 
 
5. Create GATEWAY new identity:
    Send POST request to {url}/clients
        with pre-request (taken from Bruno):
        {
            const { v4: uuidv4 } = require('uuid');
            const subuuid = uuidv4().substring(0,10);
            bru.setEnvVar("subuuid", subuuid)
        }
 
        with "JSON" body:
        {
            "identityName": "gateway-{{subuuid}}-client",
            "description": "Sample Description",
            "scopes": [
                "tokgw.create",
                "tokgw.read",
                "tokgw.update",
                "tokgw.delete"
            ],
            "resources": [
                "http://test-framework/"
            ],
            "additionalInfo": {
            }
        }
 
    with empty headers
    with bearer token - {ids_gateway_master_token} - stored in step 4
    then assert that response.status is 201 and (response.body.{accessKeySecret}) and response.body.{accessKeyId} is present
    then response.body.{accessKeyId} is stored as 'gateway_username' to use in next steps
    then response.body.{accessKeySecret} is stored as 'gateway_password' to use in next steps
 
 
6. Get GATEWAY identity JWT token:
    Send POST request to {url}/oauth/token
        with "Multipart Form" body - key=grant_type, value=client_credentials
        with empty headers
        with "Basic Auth" - username={gateway_username} - stored in step 5, password={gateway_password} - stored in step 5
    then assert that response.status is 200 and response.body.{access_token} is present
    then response.body.{access_token} is stored as 'gateway_jwt' to use in next steps
 
 
In the result - successfull retreived {emv_jwt} - STEP-3 and {gateway_jwt} - STEP-6
And its should be used for Bearer auth in emv/gateway requests
